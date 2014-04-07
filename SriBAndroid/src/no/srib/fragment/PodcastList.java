package no.srib.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.srib.R;
import no.srib.model.Podcast;
import no.srib.sribapp.util.GetPodcastTask;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PodcastList extends Fragment implements OnItemClickListener {
	
	private HashMap<Integer, String> mIdMap =  new HashMap<Integer, String>();
	
	public PodcastList() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.podcastlist_main, container,
				false);
		Button button = (Button) rootView.findViewById(R.id.tilbakeknapp);
		button.setOnClickListener((OnClickListener) getActivity());

		GetPodcastTask pod = new GetPodcastTask(this);
		pod.execute("http://129.177.114.51:8080/SriBServer/rest/podcast");

		return rootView;
	}

	public void populateListView(List<Podcast> resultList) {
		
		List<String> stringList = new ArrayList<String>();
		
		for(Podcast p : resultList){
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
				 mIdMap.put(i,objects.get(i));
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
		Log.i("Debug"," " + arg2 + ": " + arg3);
		String uri = getFileName(arg2);
		String url = "http://podcast.srib.no:8080/podcast/" + uri;
		
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		
		request.setDescription(mIdMap.get(arg2));
		request.setTitle("Podcast");
		
		// in order for this if to run, you must use the android 3.2 to compile your app
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    request.allowScanningByMediaScanner();
		    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PODCASTS, "name-of-the-file2.mp3");

		// get download service and enqueue file
		DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
		
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

}
