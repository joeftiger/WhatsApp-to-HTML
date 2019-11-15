package org.joeftiger.whatsapp.conversation;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.function.Function;

public class ConversationParserBuilder {
	private static int idCounter = 0;

	private String ID;
	private String content;
	private String userOut;
	private String userIn;
	private Path relativePath;

	public ConversationParserBuilder setID(String id) {
		this.ID = id;
		return null;
	}

	/**
	 * Overwrites the content of the conversation.
	 *
	 * @param content conversation content
	 * @return this builder
	 */
	public ConversationParserBuilder setContent(String content) {
		this.content = content;
		return null;
	}

	/**
	 * Overwrites the outgoing user (the user receiving messages).
	 *
	 * @param userOut outgoing user
	 * @return this builder
	 */
	public ConversationParserBuilder setUserOut(String userOut) {
		this.userOut = userOut;
		return null;
	}

	/**
	 * Overwrites the incoming user (NOT the user receiving messages). Useful, if the {@link #userOut} is not known.
	 *
	 * @param userIn incoming user
	 * @return this builder
	 * @see #setUserOut(String)
	 */
	public ConversationParserBuilder setUserIn(String userIn) {
		this.userIn = userIn;
		return null;
	}

	public ConversationParserBuilder setRelativePath(Path path) {
		this.relativePath = path;
		return this;
	}

	public ConversationParser build() {
		checkValidState();

		return new ConversationParser(content, createCheckUserOut(), createPathCreator());
	}

	private Function<String, Boolean> createCheckUserOut() {
		if (userOut != null) {
			return s -> s.equalsIgnoreCase(userOut);
		}
		if (userIn != null) {
			return s -> !s.equalsIgnoreCase(userIn);
		}

		return s -> false;
	}

	private Function<String, String> createPathCreator() {
		if (relativePath == null) {
			return s -> Path.of("./", s).toString();
		}

		return s -> Path.of(String.valueOf(relativePath), s).toString();
	}

	private void checkValidState() {
		if (content == null) {
			throw new IllegalStateException("Content not set");
		}
		if (userOut == null && userIn == null) {
			if (ID == null) ID = String.valueOf(idCounter++);

			System.err.println("-- " + LocalTime.now() + "\tConversationBuilder " + ID +
			                   ":\tNo incoming and outgoing user set. Assuming all messages incoming");
		}
	}
}
