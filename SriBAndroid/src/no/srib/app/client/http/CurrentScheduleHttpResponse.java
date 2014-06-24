package no.srib.app.client.http;

import java.io.IOException;

import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.Schedule;

import org.apache.http.HttpStatus;

import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CurrentScheduleHttpResponse implements HttpResponseListener {

	private final ObjectMapper MAPPER;
	private TextView textView;

	public CurrentScheduleHttpResponse(final TextView textView) {
		MAPPER = new ObjectMapper();
		this.textView = textView;
	}

	@Override
	public void onResponse(int statusCode, String response) {
		switch (statusCode) {
		case HttpStatus.SC_OK:
			try {
				Schedule schedule = MAPPER.readValue(response, Schedule.class);
				// TODO Spanned text = Html.fromHtml(schedule.getProgram());
				textView.setText(schedule.getProgram());
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
