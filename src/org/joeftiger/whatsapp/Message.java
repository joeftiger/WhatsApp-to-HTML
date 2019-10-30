package org.joeftiger.whatsapp;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A message consists of a {@link #date} and {@link #time} it got sent, a {@link #sender} and a {@link #message}.
 * The message may be a text link to an image / video / file. To convert it, use implementing classes of {@link IConverter}.
 */
public class Message {
	private LocalDate date;
	private LocalTime time;
	private String sender;
	private String message;

	public Message(LocalDate date, LocalTime time, String sender, String message) {
		this.date = date;
		this.time = time;
		this.sender = sender;
		this.message = message;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}

	public String getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message == null ? "" : message;
	}

	/**
	 * Returns the message in the format WhatsApp uses with its exports.
	 *
	 * @return message as String
	 */
	public String toWhatsAppString() {
		return date + ", " + time + " - " + sender + ": " + message;
	}

	/**
	 * Returns the message in the format WhatsApp uses with Markdown specific changes to improve readability.
	 *
	 * @return message as String
	 */
	public String toMarkdownString() {
		return "*" + date + ", " + time + "* - " + "**" + sender + "**: " + message + "\n";
	}
}
