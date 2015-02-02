package no.srib.app.client.util;

import android.os.AsyncTask;
import android.os.Build;

public class AsyncUtil {

	public static void exec(AsyncTask task) {
		// run in multiple processes so not other async tasks in the app stops working
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			task.execute();
	}
}
