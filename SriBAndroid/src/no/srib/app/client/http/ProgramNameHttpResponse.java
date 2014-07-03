package no.srib.app.client.http;

import java.io.IOException;

import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.PodcastPrograms;
import no.srib.app.client.model.ProgramName;

import org.apache.http.HttpStatus;

import android.text.Html;

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
	public void onResponse(int statusCode, String response) {
		switch (statusCode) {
		case HttpStatus.SC_OK:
			try {
				String decodedHtml = Html.fromHtml(response).toString();

				PodcastPrograms programs = MAPPER.readValue(decodedHtml,
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
