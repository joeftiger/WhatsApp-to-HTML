package org.joeftiger.whatsapp;

import org.jsoup.nodes.Element;

import java.time.LocalDate;

public class Message {

	private LocalDate date;
	private String user;

	private Element message;

	public Message(Element element) {
		this(element, null, null);
	}

	public Message(Element message, LocalDate date, String user) {
		this.message = message;
		this.date = date;
		this.user = user;
	}

	public Element getMessage() {
		return message;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getUser() {
		return user;
	}
}
