package no.srib.app.client.http;

import java.io.IOException;

import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.PodcastPrograms;
import no.srib.app.client.model.ProgramName;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProgramNameHttpResponse implements HttpResponseListener {

	private final ObjectMapper MAPPER;
	private AdapterUpdater<ProgramName, PodcastPrograms> updater;

	public ProgramNameHttpResponse(
			final AdapterUpdater<ProgramName, PodcastPrograms> updater) {

		MAPPER = new ObjectMapper();
		this.updater = updater;
	}

	@Override
	public void onResponse(final int statusCode, final String response) {
		switch (statusCode) {
		case HttpStatus.SC_OK:
			try {
				PodcastPrograms programs = MAPPER.readValue(response,
						PodcastPrograms.class);

				updater.updateFrom(programs, true);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}
}
