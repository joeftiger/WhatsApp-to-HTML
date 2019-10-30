package org.joeftiger.whatsapp.html5;

import org.joeftiger.whatsapp.Converters;
import org.joeftiger.whatsapp.Message;
import org.joeftiger.whatsapp.MessageParser;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WhatsAppToHTML {

	private String filePath;
	private String outboundUser;
	private Path mainDirectory;
	private Path htmlDirectory;

	private HTMLFile htmlFile;

	public static void main(String[] args) throws IOException {
		new WhatsAppToHTML(args);
	}

	private WhatsAppToHTML(String[] args) throws IOException {
		init(args);

		mainDirectory = Path.of(filePath).getParent();

		String content = new String(Files.readAllBytes(Path.of(filePath)));
		List<Message> messages = MessageParser.parseAll(content, "<br>");

		htmlFile = new HTMLFile();
		for (Message m : messages) {
			if (m.getSender().equals(outboundUser)) {
				htmlFile.addMessageOut(m);
			} else {
				htmlFile.addMessageIn(m);
			}
		}

		htmlFile.finish();

		save();
	}

	private void init(String[] args) {
		if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
			showHelp();
			System.exit(-1);
		}

		var db = Converters.getInstance();
		for (int i = 0; i < args.length; i++) {
			switch (args[i].toLowerCase()) {
				case "-a":
				case "--all":
					db.addConverter(new VideoConverter());
					db.addConverter(new ImageConverter());
					break;
				case "-v":
				case "--video":
					db.addConverter(new VideoConverter());
					break;
				case "-i":
				case "--image":
					db.addConverter(new ImageConverter());
					break;
				case "--user":
					i++;
					outboundUser = args[i];
					break;
				default:
					if (args[i].matches(".*\\.txt")) {
						filePath = args[i];
					}
			}
		}

		if (filePath == null) {
			showHelp();
		}
	}

	private void save() {
		System.out.println("Saving ...");

		createDirectory();
		saveCSS();
		saveHTML();

		System.out.println("Successfully saved.");
	}

	private void saveHTML() {
		System.out.println("Saving html: [" + htmlDirectory + "/index.html] ...");

		Path html = Path.of(htmlDirectory.toString(), "index.html");
		try {
			Files.writeString(html, htmlFile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveCSS() {
		System.out.println("Saving style: [" + htmlDirectory + "/style.css] ...");

		Path css = Path.of(htmlDirectory.toString(), "style.css");
		try {
			Files.copy(Style.PATH, css);
		} catch (FileAlreadyExistsException ignored) {
			printError("File already exists: [" + css + "]",
			           "To overwrite, please delete the already existing file.",
			           "Skipping ...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDirectory() {
		System.out.println("Creating directory: [" + mainDirectory + "/html] ...");

		htmlDirectory = Path.of(mainDirectory.toString(), "html");
		try {
			Files.createDirectory(htmlDirectory);
		} catch (FileAlreadyExistsException ignored) {
			printError("Directory already exists: [" + htmlDirectory + "]", "Skipping ...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printError(String... errors) {
		for (String e : errors) {
			System.err.println("-- " + e);
		}
	}


	private void showHelp() {
		System.out.println("usage:\t [options] <path to chat.txt>" +
		                   "\noptions:" +
		                   "\n\t-h" +
		                   "\n\t--help\tshow this help" +
		                   "\n\t-a" +
		                   "\n\t--all\tuse all converters" +
		                   "\n\t-i" +
		                   "\n\t--image\tlink images" +
		                   "\n\t-v" +
		                   "\n\t--video\tlink videos" +
		                   "\n\t--user\tthe user on the right side of the table");
	}
}
