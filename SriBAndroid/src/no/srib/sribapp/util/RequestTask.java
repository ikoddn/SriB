package no.srib.sribapp.util;


import android.os.AsyncTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;


import no.srib.fragment.*;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class RequestTask extends AsyncTask<String, PlaceholderFragment, String> {

	private PlaceholderFragment a;

	public RequestTask(PlaceholderFragment a) {
		this.a = a;

	}

	@Override
	protected String doInBackground(String... uri) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;

		try {
			response = httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			

		} catch (IOException e) {
			
		}
		
		return responseString;
	}

	
	protected void onPostExecute(String result) {
		if(result !=null){
			a.populateTextView(result);
		}

		super.onPostExecute(result);
	}
	
	

}
