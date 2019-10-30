package org.joeftiger.whatsapp;

import org.joeftiger.whatsapp.Message;

import java.util.List;

public interface IConverter {

	public default void convert(List<Message> messages) {
		for (Message m : messages) {
			this.convert(m);
		}
	}

	public boolean convert(Message message);
}
