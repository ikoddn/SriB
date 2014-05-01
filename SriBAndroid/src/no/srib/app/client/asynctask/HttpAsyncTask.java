package no.srib.app.client.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import no.srib.app.client.model.StreamSchedule;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {

	private HttpClient httpClient;

	public HttpAsyncTask() {
		httpClient = new DefaultHttpClient();
	}

	@Override
	protected String doInBackground(String... url) {
		String responseString = null;

		HttpGet httpGet = new HttpGet(url[0]);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				httpResponse.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				Log.e("SriB", "HTTP Status code: " + statusCode);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e("SriB", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("SriB", e.getMessage());
			e.printStackTrace();
		}

		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			Log.d("SriB", result);
			ObjectMapper mapper = new ObjectMapper();
			try {
				StreamSchedule streamSchedule = mapper.readValue(result, StreamSchedule.class);
				Log.d("SriB", "Name: " + streamSchedule.getName());
				Log.d("SriB", "URL: " + streamSchedule.getUrl());
				Log.d("SriB", "Time: " + streamSchedule.getTime().getTimeInMillis());
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				Log.e("SriB", e.getMessage());
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				Log.e("SriB", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("SriB", e.getMessage());
				e.printStackTrace();
			}
		} else {
			Log.d("SriB", "result == null");
		}
	}
}
