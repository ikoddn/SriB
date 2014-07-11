package no.srib.app.client;

import no.srib.app.client.receiver.ConnectivityChangeReceiver.OnConnectivityChangedListener;
import no.srib.app.client.service.StreamUpdaterService;

public class ConnectivityChangedListener implements
		OnConnectivityChangedListener {

	private boolean previouslyUnavailable;
	private StreamUpdaterService streamUpdater;

	public ConnectivityChangedListener(final StreamUpdaterService streamUpdater) {
		previouslyUnavailable = false;
		this.streamUpdater = streamUpdater;
	}

	@Override
	public void onNetworkAvailable() {
		if (previouslyUnavailable) {
			streamUpdater.update();
			previouslyUnavailable = false;
		}
	}

	@Override
	public void onNetworkUnavailable() {
		streamUpdater.stopUpdating();
		previouslyUnavailable = true;

		// TODO
		/*
		 * if (streamUpdateListener != null) {
		 * streamUpdateListener.onStatus(Status.NO_INTERNET); }
		 */
	}
}
