package no.srib.app.client;

import no.srib.app.client.receiver.ConnectivityChangeReceiver.OnConnectivityChangedListener;
import no.srib.app.client.service.StreamUpdaterService;

public class ConnectivityChangedListener implements
		OnConnectivityChangedListener {

	private StreamUpdaterService streamUpdater;

	public ConnectivityChangedListener(final StreamUpdaterService streamUpdater) {
		this.streamUpdater = streamUpdater;
	}

	@Override
	public void onNetworkAvailable() {
		streamUpdater.update();
	}

	@Override
	public void onNetworkUnavailable() {
		streamUpdater.stopUpdating();

		// TODO
		/*
		 * if (streamUpdateListener != null) {
		 * streamUpdateListener.onStatus(Status.NO_INTERNET); }
		 */
	}
}
