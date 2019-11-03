package org.joeftiger.whatsapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HTMLMessage implements HTMLElement {

	private LocalDate date;
	private String user;

	private List<HTMLElement> elements;

	public HTMLMessage() {
		this(null, null);
	}

	public HTMLMessage(LocalDate date, String user) {
		this.date = date;
		this.user = user;
		this.elements = new ArrayList<>();
	}

	public void addElement(HTMLElement e) {
		elements.add(e);
	}

	@Override
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		for (HTMLElement e : elements) {
			sb.append(e.toHTML());
		}
		return sb.toString();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
