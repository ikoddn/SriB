package no.srib.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.srib.R;
import no.srib.sribapp.util.GetUrlTask;
import no.srib.sribapp.util.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class PlaceholderFragment extends Fragment {
		
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
		
			RequestTask rq = new RequestTask(this);
			
		
			rq.execute("http://srib-app-dev.herokuapp.com/rest/podcast");
					
			GetUrlTask gu = new GetUrlTask(this);
			
			gu.execute("http://srib-app-dev.herokuapp.com/rest/radiourl");
			
			
			return rootView;
		}
		
		public void startMediaPlayer(final String result) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
			final MediaPlayer mp = new MediaPlayer();
			Uri url = null;
			
			JSONObject podO = null;
			
			try {
				
				podO = new JSONObject(result);
				url = Uri.parse(podO.getString("url"));
			
			} catch (JSONException e) {
				e.printStackTrace();

			}
			
			
			mp.setDataSource(getActivity().getApplicationContext(), url);
			mp.prepareAsync();
			mp.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					
				}
			});
			
			
			
		}
		
		
		public void populateTextView(String result){
			Activity ac = getActivity();
			TextView t2 =  (TextView) ac.findViewById(R.id.textView2);
			TextView t1 = (TextView) ac.findViewById(R.id.textView1);
			TextView t3 = (TextView) ac.findViewById(R.id.textView3);
			TextView t4 = (TextView) ac.findViewById(R.id.textView4);
			List<TextView> t = new ArrayList<TextView>();
			t.add(t1);
			t.add(t2);
			t.add(t3);
			t.add(t4);
			
			
			JSONArray reader = null;
			JSONObject podO = null;
			
			try {
				reader = new JSONArray(result);
				podO = reader.getJSONObject(0);
				t.get(0).setText(podO.getString("title"));
				t.get(1).setText(String.valueOf(podO.getInt("createdate")));
				int dur = podO.getInt("duration");
				dur = dur /= 60000;
				t.get(2).setText(String.valueOf(dur) + " min");
				t.get(3).setText(podO.getString("remark"));
			} catch (JSONException e) {
				e.printStackTrace();

			}
		}
		
		
		
	}

