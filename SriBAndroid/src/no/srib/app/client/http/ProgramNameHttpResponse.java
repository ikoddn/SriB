package no.srib.app.client.http;

import org.apache.http.HttpStatus;

import android.content.Context;
import no.srib.app.client.R;
import no.srib.app.client.adapter.BaseListAdapter;
import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.adapter.updater.JsonAdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.ProgramName;

public class ProgramNameHttpResponse implements HttpResponseListener {

	private AdapterUpdater<ProgramName, String> updater;
	private Context context;

	public ProgramNameHttpResponse(final Context context,
			final BaseListAdapter<ProgramName> adapter) {

		this.context = context;
		updater = new JsonAdapterUpdater<ProgramName>(ProgramName.class,
				adapter);
	}

	@Override
	public void onResponse(int statusCode, String response) {
		switch (statusCode) {
		case HttpStatus.SC_OK:
			String defaultValue = context.getResources().getString(
					R.string.spinner_podcast_default);
			updater.setDefaultValue(new ProgramName(0, defaultValue));
			updater.updateFrom(response);
			break;
		default:
			break;
		}
	}
}
