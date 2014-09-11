package no.srib.app.client.util;

/**
 * @author Jostein Eriksen
 */
public class StringUtil {
	static public String limit(String str, int charCount) {
		return str.length() > charCount? str.substring(0, charCount-4) + "...": str;
	}
}
