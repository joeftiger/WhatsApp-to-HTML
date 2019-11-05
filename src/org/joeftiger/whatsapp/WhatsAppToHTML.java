package org.joeftiger.whatsapp;

import java.io.IOException;
import java.nio.file.*;

public class WhatsAppToHTML {

	private Path filePath;
	private String outgoingUser;

	private DocumentCreator creator;

	private WhatsAppToHTML(String[] args) throws IOException {
		init(args);

		System.out.println("Reading in file:\t[" + filePath + "] ...");
		MessageParser parser = new MessageParser(Files.readString(filePath), outgoingUser);
		creator.addMessagesFrom(parser);

		System.out.println("Saving ...");
		save();
	}

	private void init(String[] args) throws IOException {
		if (args.length != 2 || args[0].equals("-h") || args[0].equals("--help")) {
			showHelp();
			System.exit(-1);
		}

		outgoingUser = args[0];
		filePath = Path.of(args[1]);
		creator = new DocumentCreator(Path.of("./res/index.html"));

		if (outgoingUser == null || outgoingUser.isBlank() || filePath == null) {
			showHelp();
			System.exit(-1);
		}
	}

	private void save() {
		createDirectory();
		saveCSS();
		saveHTML();
	}

	private void createDirectory() {
		String directory = filePath.getParent() + "/html";
		System.out.println("Creating directory:\t[" + directory + "] ...");

		try {
			Files.createDirectory(Path.of(directory));
		} catch (FileAlreadyExistsException ignored) {
			printError("Directory already exists: [" + directory + "]", "Skipping ...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveCSS() {
		String css = filePath.getParent() + "/html/style.css";
		System.out.println("Writing CSS:\t\t[" + css + "] ...");

		try {
			Files.copy(Style.PATH, Path.of(css), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveHTML() {
		String html = filePath.getParent() + "/html/index.html";
		System.out.println("Writing HTML:\t\t[" + html + "] ...");

		try {
			Files.writeString(Path.of(html), creator.getDocument().toString());
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
		System.err.println("usage:\t<outgoing user> <path to chat.txt>");
	}

	public static void main(String[] args) throws IOException {
		new WhatsAppToHTML(args);
	}
}
