package no.srib.app.client.receiver;

import no.srib.app.client.event.listener.OnConnectivityChangedListener;
import no.srib.app.client.util.NetworkUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

	private OnConnectivityChangedListener connectionChangedListener;

	public ConnectivityChangeReceiver(
			final OnConnectivityChangedListener listener) {
		connectionChangedListener = listener;
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (NetworkUtil.networkAvailable(context)) {
			connectionChangedListener.onNetworkAvailable();
		} else {
			connectionChangedListener.onNetworkUnavailable();
		}
	}
}
