package no.srib.fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
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
import no.srib.R;
import no.srib.model.Podcast;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastListFragment extends Fragment implements OnItemClickListener {

	private HashMap<Integer, String> mIdMap = new HashMap<Integer, String>();

	public PodcastListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.podcastlist_fragment, container,
				false);
		
		GetPodcastTask pod = new GetPodcastTask(this);
		pod.execute("http://10.0.2.2:8080/SriBServer/rest/podcast");

		return rootView;
	}

	public void populateListView(List<Podcast> resultList) {

		List<String> stringList = new ArrayList<String>();

		for (Podcast p : resultList) {
			stringList.add(p.getTitle());
		}

		final ListView listview = (ListView) getActivity().findViewById(
				R.id.listView);

		final StableArrayAdapter adapter = new StableArrayAdapter(getActivity()
				.getApplicationContext(), android.R.layout.simple_list_item_1,
				stringList);
		listview.setOnItemClickListener(this);
		listview.setAdapter(adapter);

	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(i, objects.get(i));
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			TextView tv = (TextView) view;

			tv.setTextColor(Color.BLACK);

			return view;

			// return super.getView(position, convertView, parent);
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return mIdMap.get(position);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.i("Debug", " " + arg2 + ": " + arg3);
		String uri = getFileName(arg2);
		String url = "http://podcast.srib.no:8080/podcast/" + uri;

		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));

		request.setDescription(mIdMap.get(arg2));
		request.setTitle("Podcast");

		// in order for this if to run, you must use the android 3.2 to compile
		// your app
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_PODCASTS, "name-of-the-file2.mp3");

		// get download service and enqueue file
		DownloadManager manager = (DownloadManager) getActivity()
				.getSystemService(Context.DOWNLOAD_SERVICE);

		manager.enqueue(request);

	}

	public String getFileName(int id) {

		String result = null;
		switch (id) {
		case 0:
			result = "SRRED13_A704B379AA29452AABD45256E2CDD288.MP3";
			break;
		case 1:
			result = "SRRED13_EDF6F7CCD5FE4F18A62C2C9DD9A8EBAC.MP3";
			break;
		case 2:
			result = "SRRED13_6C9F96544B494F64B99AFDB9B2D36E95.MP3";
			break;
		case 3:
			result = "SRRED18_67B08F3F3C2A4BD8A2F95DBE1FE93E48.MP3";
			break;

		case 4:
			result = "SRRED18_D53ECDB998C845DCAC100DBE8C057F28.MP3";

		default:
			break;
		}

		return result;
	}

	private class GetPodcastTask extends AsyncTask<String, Void, List<Podcast>> {

		private WeakReference<PodcastListFragment> podcast = null;
		
		private ProgressDialog dialog;

		public GetPodcastTask(PodcastListFragment pod){
			this.podcast = new WeakReference<PodcastListFragment>(pod);
		}
		

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Podcast is loading");
			dialog.setProgress(50);
			dialog.show();
			super.onPreExecute();
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
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					String title = jsonObject.getString("title");
					String filename = jsonObject.getString("filename");
					podcastList
							.add(new Podcast(title, filename, 0, 0, null, 0));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return podcastList;
		}

		@Override
		protected void onPostExecute(List<Podcast> result) {
				
				dialog.dismiss();
			
			if (result != null) {
				populateListView(result);

			}
		}

	}

}
