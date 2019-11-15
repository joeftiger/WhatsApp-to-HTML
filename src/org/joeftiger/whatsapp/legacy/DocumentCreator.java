package org.joeftiger.whatsapp.legacy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

	public void appendChild(Element element, String id) {
		document.getElementById(id).appendChild(element);
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
