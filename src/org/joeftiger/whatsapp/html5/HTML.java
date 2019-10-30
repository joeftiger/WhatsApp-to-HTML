package org.joeftiger.whatsapp.html5;

public class HTML {

	public static final String PARAGRAPH = "p";
	public static final String FIGURE = "figure";
	public static final String FIG_CAPTION = "figcaption";

	public static String getStyle() {
		return "<style>\n" +
		       "table, th, td {\n" +
		       "  border: 1px solid black;\n" +
		       "  border-collapse: collapse;\n" +
		       "}\n" +
		       ".message {\n" +
		       "  background-color: lightgrey;\n" +
		       "}\n" +
		       "img {\n" +
		       "  max-height: 500px;\n" +
		       "  max-width: 100%;\n" +
		       "  height: auto;\n" +
		       "  width: auto;\n" +
		       "}\n" +
		       "</style>";
	}

	public static String getHeader() {
		return "<head>\n" +
		       "  <meta charset=\"utf-8\">\n" +
		       getStyle().indent(2) +
		       "\n</head>";
	}

	public static String toTable(String s) {
		return "<table style=\"border: 1px solid black;\">" + s + "</table>";
	}

	public static String toTableData(String s) {
		return "<td>" + s + "</td>";
	}

	public static String toTableData(String s, String class_) {
		return "<td class=\"" + class_ + "\">" + s + "</td>";
	}

	public static String toTableRow(String s) {
		return "<tr>" + s + "</tr>";
	}

	public static String toTableHeader(String s) {
		return "<th>" + s + "</th>";
	}

	public static String toHeader5(String s) {
		return "<h5>" + s + "</h5>";
	}

	public static String toDiv(String s) {
		return "<div>" + s + "</div>";
	}

	public static String toParagraph(String s) {
		return "<p>" + s.replaceAll("\\R", "<br>") + "</p>";
	}

	public static String toFigure(String s) {
		return "<figure>" + s + "</figure>";
	}

	public static String toImage(String s) {
		return "<img src=\"../" + s + "\" alt=\"" + s + "\">";
	}

	public static String toFigCaption(String s) {
		return "<figcaption>" + s.replaceAll("\\R", "<br>") + "</figcaption>";
	}

	public static String toVideo(String link) {
		return "<video controls loop>\n" +
		       "  <source src=\"../" + link + "\" type=\"video/mp4\">\n" +
		       "</video>";
	}

	private static String enclose(String content, String style) {
		return "<" + style + ">" + content + "</" + style + ">";
	}
}
