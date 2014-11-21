package org.openbakery.racecontrol.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LfsNames {

	private static String[] colorCodes = { "#000000", "#ff0000", "#00aa00", "#cccc00", "#0000ff", "#993366", "#6666FF", "#777777",
			"#006600" };

	public static String getHtmlColoredName(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		Pattern pattern = Pattern.compile("\\^(\\d)(.*?)");
		StringBuffer buffer = new StringBuffer();
		Matcher matcher = pattern.matcher(name);
		boolean patternFound = false;
		while (matcher.find()) {
			matcher.appendReplacement(buffer, matcher.group(2));

			int colorCode = 0;
			try {
				colorCode = Integer.parseInt(matcher.group(1));
			} catch (NumberFormatException ex) {
				// colorCode is already 0
			}
			if (colorCode < colorCodes.length) {
				if (patternFound) {
					buffer.append("</span>");
				}
				buffer.append("<span style=\"color:" + colorCodes[colorCode] + "\">");
			}

			patternFound = true;
		}
		matcher.appendTail(buffer);
		if (patternFound) {
			buffer.append("</span>");
		}
		return buffer.toString();
	}
}
