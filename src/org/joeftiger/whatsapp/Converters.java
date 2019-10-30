package org.joeftiger.whatsapp;

import java.util.HashSet;

public class Converters {

	private static final Converters instance = new Converters();

	private HashSet<IConverter> converters;

	public static Converters getInstance() {
		return instance;
	}

	private Converters() {
		converters = new HashSet<>();
	}

	public void addConverter(IConverter converter) {
		converters.add(converter);
	}

	public void convert(Message message) {
		for (IConverter converter : converters) {
			converter.convert(message);
		}
	}
}
