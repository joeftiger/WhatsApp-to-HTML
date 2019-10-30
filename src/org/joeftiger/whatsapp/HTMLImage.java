package org.joeftiger.whatsapp;

public class HTMLImage implements HTMLElement {

	public static final String CONTAINER = "                      <a href=\"%s\">\n" +
	                                       "                        <img src=\"../%s\">\n" +
	                                       "                      </a>\n";

	private String content;

	public HTMLImage() {
	}

	public HTMLImage(String content) {
		setContent(content);
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toHTML() {
		return String.format(CONTAINER, content, content);
	}
}
