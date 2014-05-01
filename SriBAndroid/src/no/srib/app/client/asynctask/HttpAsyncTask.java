package no.srib.app.client.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {

	private HttpClient httpClient;
	private HttpResponseListener responseListener;

	public HttpAsyncTask(HttpResponseListener responseListener) {
		httpClient = new DefaultHttpClient();
		this.responseListener = responseListener;
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
		responseListener.onResponse(result);
	}

	public interface HttpResponseListener {
		void onResponse(String response);
	}
}
