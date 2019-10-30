package org.joeftiger.whatsapp.html5;

import org.joeftiger.whatsapp.Message;

import java.time.LocalDate;

public class HTMLFile {

	/**
	 * Indented block for the beginning of the document. Contains two {@code %s} placeholders for
	 * <ul>
	 *     <li>title (html)</li>
	 *     <li>name (conversation partner / group</li>
	 * </ul>
	 */
	public static final String BEFORE_CONVERSATION = "<!DOCTYPE html>\n" +
	                                                 "<html>\n" +
	                                                 "  <head>\n" +
	                                                 "    <meta charset=\"UTF-8\">\n" +
	                                                 "    <title>%s</title>\n" +
	                                                 "    <link rel=\"stylesheet\" href=\"./style.css\">\n" +
	                                                 "  </head>\n" +
	                                                 "  <body>\n" +
	                                                 "    <div class=\"page\">\n" +
	                                                 "      <div class=\"marvel-device\">\n" +
	                                                 "        <div class=\"screen\">\n" +
	                                                 "          <div class=\"screen-container\">\n" +
	                                                 "            <div class=\"chat\">\n" +
	                                                 "              <div class=\"chat-container\">\n" +
	                                                 "                <div class=\"user-bar\">\n" +
	                                                 "                  <div class=\"avatar\">\n" +
	                                                 "                    <img src=\"https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png\" alt=\"Avatar\">\n" +
	                                                 "                  </div>\n" +
	                                                 "                  <div class=\"name\">\n" +
	                                                 "                    <span>%s</span>\n" +
	                                                 "                  </div>\n" +
	                                                 "                </div>\n" +
	                                                 "                <div class=\"conversation\">\n" +
	                                                 "                  <div class=\"conversation-container\">\n";

	public static final String AFTER_CONVERSATION = "                  </div>\n" +
	                                                "                </div>\n" +
	                                                "              </div>\n" +
	                                                "            </div>\n" +
	                                                "          </div>\n" +
	                                                "        </div>\n" +
	                                                "      </div>\n" +
	                                                "    </div>\n" +
	                                                "  </body>\n" +
	                                                "</html>\n";

	/**
	 * Indented block for an incoming message. This string has two {@code %s} placeholders for
	 * - message
	 * - time
	 */
	public static final String MESSAGE_IN = "                    <div class=\"message received\">\n" +
	                                        "                      %s\n" +
	                                        "                      <span class=\"metadata\">\n" +
	                                        "                        <span class=\"time\">\n" +
	                                        "                          %s\n" +
	                                        "                        </span>\n" +
	                                        "                      </span>\n" +
	                                        "                    </div>\n";

	/**
	 * Indented block for an outgoing message. This string has two {@code %s} placeholders for
	 * <ul>
	 *     <li>message</li>
	 *     <li>time</li>
	 * </ul>
	 */
	public static final String MESSAGE_OUT = "                    <div class=\"message sent\">\n" +
	                                         "                      %s\n" +
	                                         "                      <span class=\"metadata\">\n" +
	                                         "                        <span class=\"time\">\n" +
	                                         "                          %s\n" +
	                                         "                        </span>\n" +
	                                         "                      </span>\n" +
	                                         "                    </div>\n";

	/**
	 * Indented block for a date change. Contains a {@code %s} placeholder for
	 * <ul>
	 *     <li>date</li>
	 * </ul>
	 */
	public static final String DATE_BLOCK = "                    <div class=\"date\">\n" +
	                                        "                      <span class=\"time\">\n" +
	                                        "                        %s\n" +
	                                        "                      </span>" +
	                                        "                    </div>\n";

	private StringBuilder content;
	private LocalDate lastDate;

	public HTMLFile() {
		this("", "");
	}

	public HTMLFile(String title, String name) {
		content = new StringBuilder(String.format(BEFORE_CONVERSATION, title, name));
		lastDate = LocalDate.MIN;
	}

	public void addMessageIn(Message message) {
		appendDateChange(message.getDate());
		String in = String.format(MESSAGE_IN, message.getMessage(), message.getTime());
		content.append(in);
	}

	public void addMessageOut(Message message) {
		appendDateChange(message.getDate());
		String out = String.format(MESSAGE_OUT, message.getMessage(), message.getTime());
		content.append(out);
	}

	private void appendDateChange(LocalDate localDate) {
		if (!lastDate.isEqual(localDate)) {
			lastDate = localDate;
			String date = String.format(DATE_BLOCK, localDate);
			content.append(date);
		}
	}

	public void finish() {
		content.append(AFTER_CONVERSATION);
	}

	@Override
	public String toString() {
		return content.toString();
	}
}
