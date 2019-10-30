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
		// extract date
		HTMLMessage htmlMessage = new HTMLMessage();
		htmlMessage.setDate(LocalDate.parse(content.substring(DATE_START, DATE_END), dateTimeFormat));

		// extract user
		String user = content.substring(TIME_END + 3).replaceFirst(REGEX_USER, "$1");
		htmlMessage.setUser(user);

		String message = content.substring(TIME_END + 5 + user.length());

		// search image
		String[] split = message.split("\\R", 2);
		if (split[0].matches(REGEX_IMAGE)) {
			System.out.println(split[0]);
			message = split[1];
			String img = split[0].substring(0, split[0].length() - 16);     // remove " (file attached)"
			htmlMessage.addElement(new HTMLImage(img));
		}

		// search video
		split = message.split("\\R", 2);
		if (split[0].matches(REGEX_VIDEO)) {
			message = split[1];
			String vid = split[0].substring(0, split[0].length() - 16);     // remove " (file attached)"
			htmlMessage.addElement(new HTMLVideo(vid));
		}

		// search code
		message = message.replaceAll(REGEX_CODE, "<code>$1</code>");

		message = message.replaceAll("\\R", "<br>");
		htmlMessage.addElement(new HTMLText(message));

		// add time
		htmlMessage.addElement(new HTMLTime(content.substring(TIME_START, TIME_END)));

		return htmlMessage;
	}
}
