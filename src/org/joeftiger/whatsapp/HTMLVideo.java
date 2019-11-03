package org.joeftiger.whatsapp;

public class HTMLVideo implements HTMLContent {

	public static final String CONTAINER = "                      <video controls loop>\n" +
	                                       "                        <source src=\"../%s\" type=\"video/mp4\">\n" +
	                                       "                      </video>\n";

	private String content;

	public HTMLVideo() {
	}

	public HTMLVideo(String content) {
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
