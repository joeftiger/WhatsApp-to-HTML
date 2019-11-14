package org.joeftiger.whatsapp.conversation;

import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Conversation {

	public static final String CONVERSATION_FILE_REGEX = "^WhatsApp Chat with (.*).txt$";

	private File file;
	private String chatName;
	private String content;

	public Conversation(File file) throws IOException {
		if (!file.isDirectory()) throw new IllegalArgumentException("File <" + file.toString() + "> is no directory");

		File[] files = file.listFiles();
		if (files != null) for (File f : files) {
			if (f.getName().matches(CONVERSATION_FILE_REGEX)) {
				this.file = f;
				chatName = file.getName().replace(CONVERSATION_FILE_REGEX, "$1");
				content = Files.readString(file.toPath());
				return;
			}
		}

		throw new IllegalStateException("File <" + file.toString() + "> has no conversation file matching <" + CONVERSATION_FILE_REGEX + ">");
	}

	public Element createConversationElement() {
		Element conversation = new Element("conversation")
				.attr("id", chatName);
		Element container = new Element("conversation-container").appendTo(conversation);

		ConversationParser parser = new ConversationParser.Builder()
											.setID(chatName)
											.setUserIn(chatName)
											.setContent(content)
											.build();

		for (var msg : parser.parse()) {
			container.appendChild(msg);
		}

		return conversation;
	}

	public File getFile() {
		return file;
	}
}
