package no.srib.app.client.event.handler;

import no.srib.app.client.MainActivity;
import no.srib.app.client.event.listener.OnConnectivityChangedListener;
import no.srib.app.client.service.StreamUpdaterService;

public class ConnectivityChangedHandler implements
		OnConnectivityChangedListener {

	private boolean previouslyUnavailable;
	private MainActivity activity;
	private StreamUpdaterService streamUpdater;

	public ConnectivityChangedHandler(final MainActivity activity,
			final StreamUpdaterService streamUpdater) {
		previouslyUnavailable = false;
		this.activity = activity;
		this.streamUpdater = streamUpdater;
	}

	@Override
	public void onNetworkAvailable() {
		if (previouslyUnavailable) {
			activity.updateContent();
			streamUpdater.onNetworkAvailable();
			previouslyUnavailable = false;
		}
	}

	@Override
	public void onNetworkUnavailable() {
		streamUpdater.onNetworkUnavailable();
		previouslyUnavailable = true;
	}
}
