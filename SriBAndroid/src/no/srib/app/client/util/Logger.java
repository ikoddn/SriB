package no.srib.app.client.util;

import android.util.Log;

/**
 * Class to simplify logger methods
 */
public class Logger {
	private static final String TAG = "SriB";

	static public void i(String message) {
		Log.i(TAG, message);
	}

	static public void d(String message) {
		Log.d(TAG, message);
	}

	public static void e(String message) {
		Log.e(TAG, message);
	}
}
