package no.srib.app.client.http;

import org.apache.http.HttpStatus;

import no.srib.app.client.adapter.BaseListAdapter;
import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.adapter.updater.JsonAdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.ProgramName;

public class AllProgramsHttpResponse implements HttpResponseListener {

	private AdapterUpdater<ProgramName, String> updater;

	public AllProgramsHttpResponse(final BaseListAdapter<ProgramName> adapter) {
		updater = new JsonAdapterUpdater<ProgramName>(ProgramName.class,
				adapter);
	}

	@Override
	public void onResponse(int statusCode, String response) {
		switch (statusCode) {
		case HttpStatus.SC_OK:
			updater.setDefaultValue(new ProgramName(0, "Velg program"));
			updater.updateFrom(response);
			break;
		default:
			break;
		}
	}
}
