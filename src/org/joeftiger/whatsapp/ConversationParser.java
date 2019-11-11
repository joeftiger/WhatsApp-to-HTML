package org.joeftiger.whatsapp;

import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.util.Arrays;

public class ConversationParser {
	/** Used to extract image links from message */
	public static final String REGEX_IMAGE = "^IMG-(19|2[0-9])[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])-WA[0-9]{4}\\.jpg \\(file attached\\)";

	/** Used to extract video links from message */
	public static final String REGEX_VIDEO = "^VID-(19|2[0-9])[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])-WA[0-9]{4}\\.mp4 \\(file attached\\)";

	/** Used to extract code from message */
	public static final String REGEX_CODE = "```((.|\\R)*?)```";

	public static final String REGEX_URL = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

	/** Used to extract single messages from a file content */
	public static final String REGEX_MESSAGE_SPLIT = "(?=(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[012])\\/(19|2[0-9])[0-9]{2}, ([01]?[0-9]|2[0-3]):[0-5][0-9] - .*?: )";

	private Element conversationContainer;

	private int messageCounter = 0;

	private String outgoingUser;
	private LocalDate lastDate = LocalDate.MIN;

	public ConversationParser(String outgoingUser) {
		conversationContainer = new Element("div")
				.addClass("conversation-container");

		this.outgoingUser = outgoingUser;
	}

	public void appendMessagesFrom(String content) {
		String[] messages = content.split(REGEX_MESSAGE_SPLIT);
		messageCounter += messages.length;

		Arrays.stream(messages).forEach(i -> parseMessageFrom(new MessageData(i)));
	}

	private void parseMessageFrom(MessageData content) {
		Element date = checkDateChange(content.getDate());
		if (date != null) {
			date.appendTo(conversationContainer);
		}

		Element message = new Element("div")
				.addClass("message")
				.addClass(content.getUser().equalsIgnoreCase(outgoingUser) ? "sent" : "received")
				.appendTo(conversationContainer);

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
						             .appendText(content.getTime().toString())
				).appendTo(message);
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

			Element img = new Element("img")
					.attr("src", src);
			Element imgLink = new Element("a")
					.attr("href", src)
					.appendChild(img);

			message.appendChild(imgLink)
					.appendChild(new Element("br"));
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

			Element source = new Element("source")
					.attr("src", "../" + split[0].substring(0, split[0].length() - 16))
					.attr("type", "video/mp4");

			Element vid = new Element("vid")
					.attr("controls", true)
					.attr("loop", true)
					.appendChild(source);

			message.appendChild(vid);
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

	private Element checkDateChange(LocalDate date) {
		if (date.isAfter(lastDate)) {
			lastDate = date;

			return new Element("div")
					.addClass("date")
					.appendText(date.toString());
		}
		return null;
	}

	public Element getConversationContainer() {
		return conversationContainer;
	}

	public int getMessageCounter() {
		return messageCounter;
	}
}
