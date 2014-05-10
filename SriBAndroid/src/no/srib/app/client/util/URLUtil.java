package no.srib.app.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLUtil {

	private final static String UTF8 = "UTF-8";

	public static String urlEncodeFilename(final String url) {
		int index = url.lastIndexOf('/') + 1;

		if (index != 0) {
			String path = url.substring(0, index);
			String filename = url.substring(index);

			try {
				filename = URLEncoder.encode(filename, UTF8);
			} catch (UnsupportedEncodingException e) {
			}

			return path + filename;
		}

		return url;
	}
}
