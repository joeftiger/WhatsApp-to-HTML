package org.joeftiger.whatsapp.html5;

import org.joeftiger.whatsapp.IConverter;
import org.joeftiger.whatsapp.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoConverter implements IConverter {

	public static final String REGEX = "VID-" +
	                                   "(19|2[0-9])[0-9]{2}" +        // year
	                                   "(0[1-9]|1[012])" +            // month
	                                   "(0[1-9]|[12][0-9]|3[01])" +   // day
	                                   "-WA[0-9]{4}\\.mp4" +    // counter
	                                   " \\(file attached\\)";

	private static final Pattern pattern = Pattern.compile(REGEX);

	@Override
	public boolean convert(Message message) {
		String text = message.getMessage();
		Matcher matcher = pattern.matcher(text);

		if (!matcher.find()) return false;

		String left = text.substring(0, matcher.start());
		String imageLink = matcher.group();
		String right = text.substring(matcher.end());

		// convert
		if (!left.isBlank()) {
			left = HTML.toParagraph(left);
		}

		imageLink = imageLink.substring(0, imageLink.length() - 16); // remove " (file attached)"
		imageLink = HTML.toVideo(imageLink);

		if (!right.isBlank()) {
			right = HTML.toParagraph(right);
		}

		String newText = left + imageLink + right;
		message.setMessage(newText);

		return true;
	}
}
