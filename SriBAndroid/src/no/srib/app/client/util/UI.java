package no.srib.app.client.util;

import android.os.Handler;
import android.os.Looper;

/**
 * @author Jostein
 */
public class UI {
	static private Handler mainLoopHandler = new Handler(Looper.getMainLooper());
	static public void runOnUI(Runnable command) {
		mainLoopHandler.post(command);
	}
}
