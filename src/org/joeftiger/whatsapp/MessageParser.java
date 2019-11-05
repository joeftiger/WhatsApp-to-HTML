package org.joeftiger.whatsapp;

import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageParser {
	public static final int DATE_START = 0;
	public static final int DATE_END = DATE_START + 10;
	public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static final int TIME_START = 12;
	public static final int TIME_END = TIME_START + 5;

	/** Used to extract single messages from a file content */
	public static final String REGEX_MESSAGE_SPLIT = "(?=(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[012])\\/(19|2[0-9])[0-9]{2}, ([01]?[0-9]|2[0-3]):[0-5][0-9] - .*?: )";

	/** Used to extract user from message */
	public static final String REGEX_MESSAGE_CONTENT = "(.*?): (.|\\R)*";

	/** Used to extract image links from message */
	public static final String REGEX_IMAGE = "^IMG-(19|2[0-9])[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])-WA[0-9]{4}\\.jpg \\(file attached\\)";

	/** Used to extract video links from message */
	public static final String REGEX_VIDEO = "^VID-(19|2[0-9])[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])-WA[0-9]{4}\\.mp4 \\(file attached\\)";

	/** Used to extract code from message */
	public static final String REGEX_CODE = "```((.|\\R)*?)```";

	public static final String REGEX_URL = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

	private String fileContent;
	private String outgoingUser;

	public MessageParser(String fileContent, String outgoingUser) {
		this.fileContent = fileContent;
		this.outgoingUser = outgoingUser;
	}

	/**
	 * Compiles a list of {@link Element}s.
	 *
	 * @return list of {@code Element}s
	 */
	public List<Element> parseMessages() {
		String[] messages = fileContent.split(REGEX_MESSAGE_SPLIT);

		return Arrays.stream(messages).map(this::parseMessageFrom).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * Parses an {@link Element} from the given raw content representing the message in the backup file.
	 *
	 * @param content raw message content
	 * @return resulting {@code Element}
	 */
	private Element parseMessageFrom(String content) {
		String user = content.substring(TIME_END + 3).replaceFirst(REGEX_MESSAGE_CONTENT, "$1");

		Element message = new Element("div")
				.addClass("message")
				.addClass(user.equalsIgnoreCase(outgoingUser) ? "sent" : "received");

		String text = content.substring(TIME_END + 5 + user.length());

		text = searchImage(text, message);
		text = searchVideo(text, message);
		text = searchCode(text);
		text = searchURL(text);
		text = replaceMessageLineBreaks(text);

		Element textElement = new Element("span")
				.appendText(text);
		message.appendChild(textElement);

		LocalDate date = LocalDate.parse(content.substring(DATE_START, DATE_END), dateTimeFormat);
		LocalTime time = LocalTime.parse(content.substring(TIME_START, TIME_END));
		Element metadata = new Element("span")
				.addClass("metadata")
				.appendChild(new Element("span")
				.addClass("time")
				.appendText(date.toString() + "\t" + time.toString()));
		message.appendChild(metadata);

		return message;
	}

	/**
	 * Searches the content for an image link and extracts the information to the {@link HTMLMessage}.
	 *
	 * @param content content to search for image link
	 * @param message message to store the HTML-ized image link
	 * @return {@code content} without the image link
	 */
	private String searchImage(String content, Element message) {
		String[] split = content.split("\\R", 2);

		if (split[0].matches(REGEX_IMAGE)) {
			content = split[1];

			String src = "../" + split[0].substring(0, split[0].length() - 16);
			Element img = new Element("img")
					.attr("src", src)
					.attr("href", src);

			message.appendChild(img);
		}

		return content;
	}

	/**
	 * Searches the content for a video link and extracts the information to the {@link HTMLMessage}.
	 *
	 * @param content content to search for image link
	 * @param message message to store the HTML-ized image link
	 * @return {@code content} without the image link
	 */
	private String searchVideo(String content, Element message) {
		String[] split = content.split("\\R", 2);

		if (split[0].matches(REGEX_VIDEO)) {
			content = split[1];

			Element source = new Element("source")
					.attr("src", "../" + split[0].substring(0, split[0].length() - 16))
					.attr("type", "video/mp4");

			Element vid = new Element("vid")
					.attr("controls", true)
					.attr("loop", true)
					.appendChild(source);

			message.appendChild(vid);
		}

		return content;
	}

	/**
	 * Searches the message for code occurrences, HTML-izing them.
	 *
	 * @param message message to search for code
	 * @return HTML-ized {@code message}
	 */
	private String searchCode(String message) {
		return message.replaceAll(REGEX_CODE, "<code>$1</code>");
	}

	/**
	 * Searches the message for URL occurrences, HTML-izing them.
	 *
	 * @param message message to search for url
	 * @return HTML-ized {@code message}
	 */
	private String searchURL(String message) {
		return message.replaceAll(REGEX_URL, "<a href=\"$1\">$1</a>");
	}

	/**
	 * HTML-izes line breaks in the message.
	 *
	 * @param message message to HTML-ize
	 * @return HTML-ized {@code message}
	 */
	private String replaceMessageLineBreaks(String message) {
		if (!message.isBlank()) {
			message = message.replaceAll("\\R", "<br>");
			message = message.substring(0, message.length() - 4);
		}

		return message;
	}
}
