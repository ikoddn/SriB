package no.srib.app.client.receiver;

import no.srib.app.client.event.listener.OnConnectivityChangedListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

	private OnConnectivityChangedListener connectionChangedListener;

	public ConnectivityChangeReceiver(
			final OnConnectivityChangedListener listener) {
		connectionChangedListener = listener;
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (networkAvailable(context)) {
			connectionChangedListener.onNetworkAvailable();
		} else {
			connectionChangedListener.onNetworkUnavailable();
		}
	}

	public static boolean networkAvailable(final Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();

		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
