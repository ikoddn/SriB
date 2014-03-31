package no.srib;




import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import no.srib.sribapp.util.RequestTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	public void exceptionHandeling(Activity t){
		Toast to = new Toast(t.getApplicationContext());
		to.setText("Hei hei");
		to.show();
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
		
			RequestTask rq = new RequestTask(this);
			
		
			rq.execute("http://129.177.114.189:8080/SriBServer/rest/podcast");
					
			
			
			return rootView;
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

}
