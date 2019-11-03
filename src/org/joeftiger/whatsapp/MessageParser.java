package org.joeftiger.whatsapp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageParser {
	public static final int DATE_START = 0;
	public static final int DATE_END = DATE_START + 10;
	public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static final int TIME_START = 12;
	public static final int TIME_END = TIME_START + 5;

	public static final String REGEX_FILE_SPLIT = "(?=(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[012])\\/(19|2[0-9])[0-9]{2}, ([01]?[0-9]|2[0-3]):[0-5][0-9] - .*?: )";

	public static final String REGEX_USER = "(.*?): (.|\\R)*";

	public static final String REGEX_IMAGE = "^IMG-(19|2[0-9])[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])-WA[0-9]{4}\\.jpg \\(file attached\\)";

	public static final String REGEX_VIDEO = "^VID-(19|2[0-9])[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])-WA[0-9]{4}\\.mp4 \\(file attached\\)";

	public static final String REGEX_CODE = "```((.|\\R)*?)```";

	public List<HTMLMessage> parseMessages(String file) {
		String[] messageStrings = file.split(REGEX_FILE_SPLIT);

		List<HTMLMessage> messages = new ArrayList<>(messageStrings.length);
		for (String messageString : messageStrings) {
			messages.add(parse(messageString));
		}
		return messages;
	}

	private HTMLMessage parse(String content) {
		// extract date and user
		LocalDate date = LocalDate.parse(content.substring(DATE_START, DATE_END), dateTimeFormat);
		String user = content.substring(TIME_END + 3).replaceFirst(REGEX_USER, "$1");

		HTMLMessage htmlMessage = new HTMLMessage(date, user);

		String message = content.substring(TIME_END + 5 + user.length());
		
		message = searchImage(message, htmlMessage);
		message = searchVideo(message, htmlMessage);
		message = searchCode(message);

		// finalize
		message = replaceMessageLineBreaks(message);
		htmlMessage.addElement(new HTMLText(message));
		htmlMessage.addElement(new HTMLTime(content.substring(TIME_START, TIME_END)));

		return htmlMessage;
	}

	private String searchImage(String message, HTMLMessage htmlMessage) {
		String[] split = message.split("\\R", 2);
		if (split[0].matches(REGEX_IMAGE)) {
			message = split[1];
			String img = split[0].substring(0, split[0].length() - 16);     // remove " (file attached)"
			htmlMessage.addElement(new HTMLImage(img));
		}

		return message;
	}

	private String searchVideo(String message, HTMLMessage htmlMessage) {
		String[] split = message.split("\\R", 2);
		if (split[0].matches(REGEX_VIDEO)) {
			message = split[1];
			String vid = split[0].substring(0, split[0].length() - 16);     // remove " (file attached)"
			htmlMessage.addElement(new HTMLVideo(vid));
		}

		return message;
	}

	private String searchCode(String message) {
		return message.replaceAll(REGEX_CODE, "<code>$1</code>");
	}

	private String replaceMessageLineBreaks(String message) {
		if (!message.isBlank()) {
			message = message.replaceAll("\\R", "<br>");
			message = message.substring(0, message.length() - 4);
		}

		return message;
	}
}
