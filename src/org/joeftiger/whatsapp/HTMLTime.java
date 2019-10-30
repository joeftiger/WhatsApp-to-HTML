package org.joeftiger.whatsapp;

public class HTMLTime implements HTMLElement {

	public static final String CONTAINER = "                      <span class=\"metadata\">\n" +
	                                       "                        <span class=\"time\">\n" +
	                                       "                          %s\n" +
	                                       "                        </span>\n" +
	                                       "                      </span>\n";

	private String content;

	public HTMLTime() {
	}

	public HTMLTime(String content) {
		setContent(content);
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toHTML() {
		return String.format(CONTAINER, content);
	}
}
