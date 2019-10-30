package org.joeftiger.whatsapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HTMLMessage {

	private LocalDate date;
	private String user;

	private List<HTMLElement> elements;

	public HTMLMessage() {
		elements = new ArrayList<>();
	}

	public void addElement(HTMLElement e) {
		elements.add(e);
	}

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
