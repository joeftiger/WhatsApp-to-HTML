package org.joeftiger.whatsapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ExampleDataWriter {

	private static final Path LoremIpsum = Path.of("./res/example_data/lorem_ipsum.txt");
	private static final Path WriteOutChat = Path.of("./res/example_data/example.txt");
	private static final String[] user = { "Lorem", "Ipsum" };

	private static void createLoremIpsum() throws IOException {
		var texts = Files.readAllLines(LoremIpsum);

		var date = LocalDate.now();
		var dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		var time = LocalTime.now();
		var timeString = time.format(DateTimeFormatter.ofPattern("hh:mm"));

		var format = dateString + ", " + timeString + " - %s: %s\n";

		var sb = new StringBuilder();
		for (int i = 0; i < texts.size(); i++) {
			var line = String.format(format, user[i % 2], texts.get(i));
			sb.append(line);
		}

		Files.writeString(WriteOutChat, sb.toString());
	}

	private static void createCount() throws IOException {
		var date = LocalDate.now();
		var dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		var time = LocalTime.now();
		var timeString = time.format(DateTimeFormatter.ofPattern("hh:mm"));

		var format = dateString + ", " + timeString + " - %s: %s\n";

		var sb = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			var line = String.format(format, user[i % 2], i);
			sb.append(line);
		}

		Files.writeString(WriteOutChat, sb.toString());
	}

	public static void main(String[] args) throws IOException {
		createLoremIpsum();
//		createCount();
	}
}
