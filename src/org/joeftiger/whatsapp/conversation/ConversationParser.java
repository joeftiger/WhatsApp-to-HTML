package org.joeftiger.whatsapp.conversation;

import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class ConversationParser {
	/** Used to extract image links from message */
	public static final String REGEX_IMAGE = "^IMG-(19|2[0-9])[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])-WA[0-9]{4}\\.jpg \\(file attached\\)";

	/** Used to extract video links from message */
	public static final String REGEX_VIDEO = "^VID-(19|2[0-9])[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])-WA[0-9]{4}\\.mp4 \\(file attached\\)";

	/** Used to extract code from message */
	public static final String REGEX_CODE = "```((.|\\R)*?)```";

	public static final String REGEX_URL = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

	/** Used to extract single messages from a file content */
	public static final String REGEX_MESSAGE_SPLIT = "(?=(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[012])\\/(19|2[0-9])[0-9]{2}, ([01]?[0-9]|2[0-3]):[0-5][0-9] - .*?: )";

	/** ============================================================================================================ */

	private List<Element> messages;
	private String content;
	private Function<String, Boolean> checkUserOut;

	private LocalDate lastDate = LocalDate.MIN;

	/** ============================================================================================================ */

	ConversationParser(String content, Function<String, Boolean> checkUserOut) {
		messages = new ArrayList<>();
		this.content = content;
		this.checkUserOut = checkUserOut;
	}

	public List<Element> parse() {
		String[] splitMessages = content.split(REGEX_MESSAGE_SPLIT);

		for (String msg : splitMessages) {
			MessageData data = new MessageData(msg);
			parseMessageFrom(data);
		}

		return messages;
	}

	private void parseMessageFrom(MessageData content) {
		if (content.getDate().isAfter(lastDate)) {
			messages.add(createDate(content.getDate()));
		}
		Element message = new Element("div")
				.addClass("message")
				.addClass(checkUserOut.apply(content.getUser()) ? "sent" : "received");

		searchImage(content, message);
		searchVideo(content, message);
		searchCode(content);
		searchURL(content);

		if (!content.getMessage().isBlank()) {
			new Element("span")
					.appendText(content.getMessage())
					.appendTo(message);
		}

		new Element("span")
				.addClass("metadata")
				.appendChild(new Element("span")
						             .addClass("time")
						             .appendText(content.getTime().toString()))
				.appendTo(message);

		messages.add(message);
	}

	/**
	 * Searches the content for an image link and extracts the information to the given {@link Element}.
	 *
	 * @param content content to search for image link
	 * @param message message to store the HTML-ized image link
	 * @return {@code content} without the image link
	 */
	private void searchImage(MessageData content, Element message) {
		String[] split = content.getMessage().split("\\R", 2);

		if (split[0].matches(REGEX_IMAGE)) {
			content.setMessage(split[1]);

			String src = "../" + split[0].substring(0, split[0].length() - 16);

			new Element("a")
					.attr("href", src)
					.appendChild(new Element("img").attr("src", src))
					.appendTo(message);

			message.appendChild(new Element("br"));
		}
	}

	/**
	 * Searches the content for an image link and extracts the information to the given {@link Element}.
	 *
	 * @param content content to search for image link
	 * @param message message to store the HTML-ized image link
	 */
	private void searchVideo(MessageData content, Element message) {
		String[] split = content.getMessage().split("\\R", 2);

		if (split[0].matches(REGEX_VIDEO)) {
			content.setMessage(split[1]);

			new Element("vid")
					.attr("controls", true)
					.attr("loop", true)
					.appendChild(new Element("source")
							             .attr("src", "../" + split[0].substring(0, split[0].length() - 16))
							             .attr("type", "video/mp4"))
					.appendTo(message);
		}
	}

	/**
	 * Searches the message for code occurrences, HTML-izing them.
	 *
	 * @param message message to search for code
	 */
	private void searchCode(MessageData message) {
		message.setMessage(message.getMessage().replaceAll(REGEX_CODE, "<code>$1</code>"));
	}

	/**
	 * Searches the message for URL occurrences, HTML-izing them.
	 *
	 * @param message message to search for url
	 */
	private void searchURL(MessageData message) {
		message.setMessage(message.getMessage().replaceAll(REGEX_URL, "<a href=\"$1\">$1</a>"));
	}

	private Element createDate(LocalDate date) {
		this.lastDate = date;
		return new Element("div")
				.addClass("date")
				.appendText(date.toString());
	}

	public static class Builder {

		private String id;
		private String content;
		private String userOut;
		private String userIn;

		public Builder setID(String id) {
			this.id = id;
			return this;
		}

		/**
		 * Overwrites the content of the conversation.
		 *
		 * @param content conversation content
		 * @return this builder
		 */
		public Builder setContent(String content) {
			this.content = content;
			return this;
		}

		/**
		 * Overwrites the outgoing user (the user receiving messages).
		 *
		 * @param userOut outgoing user
		 * @return this builder
		 */
		public Builder setUserOut(String userOut) {
			this.userOut = userOut;
			return this;
		}

		/**
		 * Overwrites the incoming user (NOT the user receiving messages). Useful, if the {@link #userOut} is not known.
		 *
		 * @param userIn incoming user
		 * @return this builder
		 * @see #setUserOut(String)
		 */
		public Builder setUserIn(String userIn) {
			this.userIn = userIn;
			return this;
		}

		public boolean hasUserOut() {
			return userOut != null;
		}

		public boolean hasUserIn() {
			return userIn != null;
		}

		public ConversationParser build() {
			checkValidState();

			return new ConversationParser(content, createCheckUserOut());
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

		private void checkValidState() {
			if (content == null) {
				throw new IllegalStateException("Content not set");
			}
			if (userOut == null && userIn == null) {
				System.err.println("-- " + LocalTime.now() + "\tConversationBuilder " + id +
				                   ":\tNo incoming and outgoing user set. Assuming all messages incoming");
			}
		}
	}
}
