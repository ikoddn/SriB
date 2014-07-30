package no.srib.app.client.event.handler;

import no.srib.app.client.event.listener.OnConnectivityChangedListener;
import no.srib.app.client.service.StreamUpdaterService;

public class ConnectivityChangedHandler implements
		OnConnectivityChangedListener {

	private boolean previouslyUnavailable;
	private StreamUpdaterService streamUpdater;

	public ConnectivityChangedHandler(final StreamUpdaterService streamUpdater) {
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
