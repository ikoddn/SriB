package no.srib.sribapp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import no.srib.fragment.PodcastList;
import no.srib.model.Podcast;

import android.os.AsyncTask;

public class GetPodcastTask extends AsyncTask<String, Void, List<Podcast>>{

	PodcastList frag;
	
	public GetPodcastTask(PodcastList frag) {
		this.frag = frag;
	}

	@Override
	protected List<Podcast> doInBackground(String... params) {
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		List<Podcast> podcastList = new ArrayList<Podcast>();
		JSONObject jsonObject = null;
		
		try {
			response = httpclient.execute(new HttpGet(params[0]));
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
		
		try {
			JSONArray jsonArray = new JSONArray(responseString);
			for(int i = 0; i < jsonArray.length(); i++){
				jsonObject = jsonArray.getJSONObject(i);
				String title = jsonObject.getString("title");
				String filename = jsonObject.getString("filename");	
				podcastList.add(new Podcast(title, filename, 0, 0, null, 0));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return podcastList;
	}
	
	
	@Override
	protected void onPostExecute(List<Podcast> result) {
		if(result != null){
			frag.populateListView(result);
			
		}
	}

}
