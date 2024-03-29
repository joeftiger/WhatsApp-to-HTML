package org.joeftiger.whatsapp.legacy;

import org.joeftiger.javahelp.Help;
import org.joeftiger.javahelp.Usage;
import org.joeftiger.whatsapp.Resources;

import java.io.IOException;
import java.nio.file.*;

public class WhatsAppToHTML {

	private Help help;

	private Path filePath;
	private String outgoingUser;

	private DocumentCreator creator;

	private WhatsAppToHTML(String[] args) throws IOException {
		init(args);

		System.out.println("=".repeat(10) + " INIT " + "=".repeat(10));
		System.out.println("Reading in file:\t[" + filePath + "]");
		var content = Files.readString(filePath);

		var parser = new ConversationParser(outgoingUser);
		parser.appendMessagesFrom(content);
		System.out.printf("-- Parsed %d messages\n", parser.getMessageCounter());

		creator.appendChild(parser.getConversationContainer(), "conversation");
		System.out.println("-- Created HTML");

		System.out.println("=".repeat(9) + " SAVING " + "=".repeat(9));

		createDirectory();
		saveCSS();
		saveHTML();
		System.out.println("=".repeat(8) + " FINISHED " + "=".repeat(8));
	}

	/**
	 * Initializes the file path etc. from the given arguments.
	 *
	 * @param args program arguments
	 */
	private void init(String[] args) {
		help = new Help().setUsage(new Usage().addTargets("outgoing user", "path to chat.txt"));

		if (args.length != 2) {
			showHelp();
			System.exit(1);
		}

		outgoingUser = args[0];
		filePath = Path.of(args[1]);
		creator = new DocumentCreator(Resources.getInstance().getHtml());

		if (outgoingUser == null || outgoingUser.isBlank() || filePath == null) {
			showHelp();
			System.exit(1);
		}
	}

	/**
	 * Creates the directory {@code <base>/html/}.
	 */
	private void createDirectory() {
		String directory = filePath.getParent() + "/html";
		System.out.println("Creating directory:\t[" + directory + "]");

		try {
			Files.createDirectory(Path.of(directory));
		} catch (FileAlreadyExistsException ignored) {
			printError("Directory already exists: [" + directory + "]", "Skipping ...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Copies the stylesheet file to {@code <base>/html/style.css}.
	 */
	private void saveCSS() {
		String css = filePath.getParent() + "/html/style.css";
		System.out.println("Writing CSS:\t\t[" + css + "]");

		try {
			Files.writeString(Path.of(css), Resources.getInstance().getStyle());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the created html file to {@code <base>/html/index.html}.
	 */
	private void saveHTML() {
		String html = filePath.getParent() + "/html/index.html";
		System.out.println("Writing HTML:\t\t[" + html + "]");

		try {
			Files.writeString(Path.of(html), creator.getDocument().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints the given messages in error format.
	 *
	 * @param errors error messages
	 */
	private void printError(String... errors) {
		for (String e : errors) {
			System.err.println("-- " + e);
		}
	}

	/**
	 * Prints basic help for usage of this program.
	 */
	private void showHelp() {
		System.out.println(help);
	}

	public static void main(String[] args) throws IOException {
		new WhatsAppToHTML(args);
	}
}
