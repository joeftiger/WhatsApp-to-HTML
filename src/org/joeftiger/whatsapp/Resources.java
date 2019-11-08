package org.joeftiger.whatsapp;

import java.util.Scanner;

public class Resources {

	private static final Resources instance = new Resources();

	private final String style;
	private final String html;

	private Resources() {
		var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("style.css");
		var scn = new Scanner(stream).useDelimiter("\\A");
		style = scn.hasNext() ? scn.next() : "";
		scn.close();

		stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
		scn = new Scanner(stream).useDelimiter("\\A");
		html = scn.hasNext() ? scn.next() : "";
	}

	public String getHtml() {
		return html;
	}

	public String getStyle() {
		return style;
	}

	public static Resources getInstance() {
		return instance;
	}
}
