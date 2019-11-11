package org.joeftiger.whatsapp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageData {
	private LocalDate date;
	private LocalTime time;
	private String user;
	private String message;

	public MessageData(String content) {
		date = LocalDate.parse(content.substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		time = LocalTime.parse(content.substring(12, 17));
		user = content.substring(20).split(":", 2)[0];
		message = content.substring(20 + user.length() + 2);
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}

	public String getUser() {
		return user;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
