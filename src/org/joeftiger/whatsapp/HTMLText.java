package org.joeftiger.whatsapp;

public class HTMLText implements HTMLContent {

	public static final String CONTAINER = "                      <span>\n" +
	                                       "                        %s\n" +
	                                       "                      </span>\n";

	private String content;

	public HTMLText() {
	}

	public HTMLText(String content) {
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
