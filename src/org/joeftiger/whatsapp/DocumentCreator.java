package org.joeftiger.whatsapp;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentCreator {

	private Document document;

	public DocumentCreator(Path uri) throws IOException {
		this(Files.readString(uri));
	}

	public DocumentCreator(String htmlContent) {
		this.document = Jsoup.parse(htmlContent);
	}

	public void addMessagesFrom(MessageParser parser) {
		Element element = document.getElementById("conversation-container");

		for (Element e : parser.parseMessages()) {
			element.appendChild(e);
		}
	}

	public Document getDocument() {
		return document;
	}
}
