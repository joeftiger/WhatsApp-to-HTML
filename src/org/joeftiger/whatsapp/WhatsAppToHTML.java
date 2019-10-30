package org.joeftiger.whatsapp;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class WhatsAppToHTML {

	private Path filePath;
	private String outgoingUser;

	private Path mainDirectory;
	private Path htmlDirectory;

	private HTMLDocument htmlFile;

	private WhatsAppToHTML(String[] args) throws IOException {
		init(args);

		mainDirectory = filePath.getParent();

		System.out.println("Reading in file:\t[" + filePath + "] ...");
		String content = Files.readString(filePath);
		List<HTMLMessage> messages = new MessageParser().parseMessages(content);
		System.out.println("Parsed " + messages.size() + " messages.");

		htmlFile = new HTMLDocument();
		for (HTMLMessage m : messages) {
			if (m.getUser().equals(outgoingUser)) {
				htmlFile.addMessageOut(m);
			} else {
				htmlFile.addMessageIn(m);
			}
		}

		htmlFile.finish();

		save();
	}

	private void init(String[] args) {
		if (args.length != 2 || args[0].equals("-h") || args[0].equals("--help")) {
			showHelp();
			System.exit(-1);
		}

		outgoingUser = args[0];
		filePath = Path.of(args[1]);

		if (outgoingUser == null || outgoingUser.isBlank() || filePath == null) {
			showHelp();
			System.exit(-1);
		}
	}

	private void showHelp() {
		System.err.println("usage:\t<outgoing user> <path to chat.txt>");
	}

	private void save() {
		createDirectory();
		saveCSS();
		saveHTML();

		System.out.println("Successfully converted.");
	}

	private void saveHTML() {
		System.out.println("Writing HTML:\t\t[" + htmlDirectory + "/index.html] ...");

		Path html = Path.of(htmlDirectory.toString(), "index.html");
		try {
			Files.writeString(html, htmlFile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveCSS() {
		System.out.println("Writing CSS:\t\t[" + htmlDirectory + "/style.css] ...");

		Path css = Path.of(htmlDirectory.toString(), "style.css");
		try {
			Files.copy(Style.PATH, css, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDirectory() {
		System.out.println("Creating directory:\t[" + mainDirectory + "/html] ...");

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

	public static void main(String[] args) throws IOException {
		new WhatsAppToHTML(args);
	}
}
