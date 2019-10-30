package org.joeftiger.whatsapp;

import org.joeftiger.whatsapp.Converters;
import org.joeftiger.whatsapp.Message;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MessageParser {
	public static final int DATE_START = 0;
	public static final int DATE_END = DATE_START + 10;
	public static final int DATE_LENGTH = DATE_END - DATE_START;

	public static final int TIME_START = 12;
	public static final int TIME_END = TIME_START + 5;

	public static final int CONTACT_START = TIME_END + 3;

	public static final String REGEX_SLASH = "\\/";
	public static final String REGEX_DATE = "(0[1-9]|[12][0-9]|3[01])" + REGEX_SLASH +  // year
											"(0[1-9]|1[012])" + REGEX_SLASH +           // month
											"(19|2[0-9])[0-9]{2}";                      // day

	public static final String REGEX_TIME = "([01]?[0-9]|2[0-3])" + ":" +   // hour
											"[0-5][0-9]";                   // minute

	/**
	 * Parses the given String into a list of {@link Message}s.
	 *
	 * @param input chat history
	 * @return list of messages
	 */
	public static List<Message> parseAll(final String input, String lineSeparator) {
		List<Message> messages = new ArrayList<>();

		String[] allLines = input.split(System.lineSeparator());

		Converters converters = Converters.getInstance();
		for (int index = 0; index < allLines.length; index++) {
			String allDetails = allLines[index];

			// read next lines if not from next message
			while (index + 1 < allLines.length && !hasDate(allLines[index + 1])) {
				index++;
				allDetails += lineSeparator + allLines[index];
			}

			Message message = parseMessage(allDetails);
			converters.convert(message);
			messages.add(message);
		}

		return messages;
	}

	/**
	 * Parses the given String into a {@link Message}
	 * @param input detailed chat entry
	 * @return message
	 */
	public static Message parseMessage(final String input) {
		final LocalDate date = toDate(input.substring(DATE_START, DATE_END));
		final LocalTime time = toTime(input.substring(TIME_START, TIME_END));

		String trimmedInput = input.substring(CONTACT_START);
		String[] senderAndMsg = trimmedInput.split(": ", 2);
		assert senderAndMsg.length == 2;

		final String sender = senderAndMsg[0];
		final String message = senderAndMsg[1];

		return new Message(date, time, sender, message);
	}

	/**
	 * Returns whether the given input is in the WhatsApp date format ({@link #REGEX_DATE}).
	 * @param input String to check
	 * @return {@code true} if valid format. {@code false} otherwise.
	 */
	private static boolean isDate(String input) {
		return input.matches(REGEX_DATE);
	}

	/**
	 * Returns whether the given input has a date in WhatsApp date format at the beginning ({@link #REGEX_DATE}).
	 * @param input String to check
	 * @return {@code true} if valid format. {@code false} otherwise.
	 */
	private static boolean hasDate(String input) {
		return input.length() >= DATE_LENGTH && isDate(input.substring(DATE_START, DATE_END));
	}

	/**
	 * Converts the given String input to a {@link LocalDate} using following format:
	 * <code>dd/mm/yyyy</code>
	 *
	 * @param input String to format
	 * @return local date
	 */
	private static LocalDate toDate(String input) {
		assert isDate(input);

		String[] args = input.split(REGEX_SLASH);
		assert args.length == 3;

		int year = Integer.parseInt(args[2]);
		int month = Integer.parseInt(args[1]);
		int day = Integer.parseInt(args[0]);

		return LocalDate.of(year, month, day);
	}

	/**
	 * Returns whether the given input is in the WhatsApp time format ({@link #REGEX_TIME}).
	 * @param input String to check
	 * @return {@code true} if valid format. {@code false} otherwise.
	 */
	private static boolean isTime(String input) {
		return input.matches(REGEX_TIME);
	}

	/**
	 * Converts the given String input to a {@link LocalTime} using following format:
	 * <code>hh:mm</code>
	 *
	 * @param input String to format
	 * @return local time
	 */
	private static LocalTime toTime(String input) {
		assert input.matches(REGEX_TIME);

		String[] args = input.split(":");
		assert args.length == 2;

		int hour = Integer.parseInt(args[0]);
		int minutes = Integer.parseInt(args[1]);

		return LocalTime.of(hour, minutes);
	}
}
