package org.joeftiger.whatsapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

public class ExampleDataWriter {

	private static final Path LoremIpsum = Path.of("./res/example_data/lorem_ipsum.txt");
	private static final Path WriteOutChat = Path.of("./res/example_data/example.txt");
	private static final String[] user = { "Lorem", "Ipsum" };

	private static void createExample() throws IOException {
		var texts = Files.readAllLines(LoremIpsum);

		var date = LocalDate.now();
		var dateString = date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear();

		var time = LocalTime.now();
		var timeString = time.getHour() + ":" + time.getMinute();

		var format = dateString + ", " + timeString + " - %s: %s\n";

		var sb = new StringBuilder();
		for (int i = 0; i < texts.size(); i++) {
			var line = String.format(format, user[i % 2], texts.get(i));
			sb.append(line);
		}

		Files.writeString(WriteOutChat, sb.toString());
	}

	public static void main(String[] args) throws IOException {
		createExample();
	}
}
