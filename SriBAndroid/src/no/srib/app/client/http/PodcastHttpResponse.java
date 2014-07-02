package no.srib.app.client.http;

import org.apache.http.HttpStatus;

import android.text.Html;
import no.srib.app.client.adapter.BaseListAdapter;
import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.adapter.updater.JsonAdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.Podcast;

public class PodcastHttpResponse implements HttpResponseListener {

	private AdapterUpdater<Podcast, String> updater;

	public PodcastHttpResponse(final BaseListAdapter<Podcast> adapter) {
		updater = new JsonAdapterUpdater<Podcast>(Podcast.class, adapter);
	}

	@Override
	public void onResponse(final int statusCode, final String response) {
		switch (statusCode) {
		case HttpStatus.SC_OK:
			String decodedHtml = Html.fromHtml(response).toString();
			updater.updateFrom(decodedHtml, true);
			break;
		default:
			break;
		}
	}
}
