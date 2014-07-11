package no.srib.app.client.http;

import no.srib.app.client.adapter.ListBasedAdapter;
import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.adapter.updater.JsonAdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.Podcast;

import org.apache.http.HttpStatus;

public class PodcastHttpResponse implements HttpResponseListener {

	private AdapterUpdater<Podcast, String> updater;

	public PodcastHttpResponse(final ListBasedAdapter<Podcast> adapter) {
		updater = new JsonAdapterUpdater<Podcast>(Podcast.class, adapter);
	}

	@Override
	public void onResponse(final int statusCode, final String response) {
		switch (statusCode) {
		case HttpStatus.SC_OK:
			updater.updateFrom(response, true);
			break;
		default:
			break;
		}
	}
}
