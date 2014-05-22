package no.srib.app.client.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

	private OnConnectionChangedListener connectionChangedListener;
	
	public ConnectivityChangeReceiver() {
		connectionChangedListener = null;
	}

	public void setConnectionChangedListener(
			OnConnectionChangedListener connectionChangedListener) {
		this.connectionChangedListener = connectionChangedListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (networkAvailable(context)) {
			connectionChangedListener.onNetworkAvailable();
		} else {
			connectionChangedListener.onNetworkUnavailable();
		}
	}

	public boolean networkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();

		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public interface OnConnectionChangedListener {
		void onNetworkAvailable();

		void onNetworkUnavailable();
	}
}
