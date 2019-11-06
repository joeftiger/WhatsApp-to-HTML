package org.joeftiger.whatsapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentCreator {

	private Document document;

	/**
	 * Creates a new document from the given base html file.
	 *
	 * @param uri path to base html file
	 * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is
	 *                     read
	 */
	public DocumentCreator(Path uri) throws IOException {
		this(Files.readString(uri));
	}

	/**
	 * Creates a new document with given base content.
	 *
	 * @param htmlContent base html content
	 */
	public DocumentCreator(String htmlContent) {
		this.document = Jsoup.parse(htmlContent);
	}

	/**
	 * Adds all messages from the given parser to this document.
	 *
	 * @param parser message parser containing parsed messages
	 */
	public void addMessagesFrom(MessageParser parser) {
		Element element = document.getElementById("conversation-container");

		for (Element e : parser.parseMessages()) {
			element.appendChild(e);
		}
	}

	/**
	 * Returns the document.
	 *
	 * @return document
	 */
	public Document getDocument() {
		return document;
	}
}
