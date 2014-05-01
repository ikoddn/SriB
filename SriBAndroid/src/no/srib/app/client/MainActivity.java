package no.srib.app.client;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.srib.R;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.audioplayer.AudioPlayer;
import no.srib.app.client.audioplayer.AudioPlayerException;
import no.srib.app.client.audioplayer.AudioPlayerService;
import no.srib.app.client.fragment.LiveRadioFragment;
import no.srib.app.client.model.StreamSchedule;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter sectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager viewPager;

	private boolean serviceBound;
	private AudioPlayer audioPlayer;
	private ServiceConnection serviceConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		HttpAsyncTask streamScheduleTask = new HttpAsyncTask(
				new StreamScheduleResponseListener());
		streamScheduleTask
				.execute("http://80.203.58.154:8080/SriBServer/rest/radiourl");

		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);

		serviceBound = false;
		audioPlayer = null;
		serviceConnection = new AudioPlayerServiceConnection();

		doBindService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		doUnbindService();
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

	public void doBindService() {
		// Establish a connection with the service. We use an explicit
		// class name because we want a specific service implementation that
		// we know will be running in our own process (and thus won't be
		// supporting component replacement by other applications).
		bindService(new Intent(MainActivity.this, AudioPlayerService.class),
				serviceConnection, Context.BIND_AUTO_CREATE);
		serviceBound = true;
	}

	public void doUnbindService() {
		if (serviceBound) {
			unbindService(serviceConnection);
			serviceBound = false;
		}
	}

	private class AudioPlayerServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// This is called when the connection with the service has been
			// established, giving us the service object we can use to
			// interact with the service. Because we have bound to a explicit
			// service that we know is running in our own process, we can
			// cast its IBinder to a concrete class and directly access it.
			audioPlayer = ((AudioPlayerService.AudioPlayerBinder) service)
					.getService();

			String tag = getFragmentTag(viewPager.getId(),
					SectionsPagerAdapter.LIVERADIO_FRAGMENT);
			LiveRadioFragment liveRadioFragment = (LiveRadioFragment) getSupportFragmentManager()
					.findFragmentByTag(tag);
			liveRadioFragment.setAudioPlayer(audioPlayer);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			// Because it is running in our same process, we should never
			// see this happen.
			audioPlayer = null;
		}
	}

	private class StreamScheduleResponseListener implements
			HttpResponseListener {

		@Override
		public void onResponse(String response) {
			if (response != null) {
				Log.d("SriB", response);
				ObjectMapper mapper = new ObjectMapper();

				try {
					StreamSchedule streamSchedule = mapper.readValue(response,
							StreamSchedule.class);
					audioPlayer.setDataSource(streamSchedule.getUrl());
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
				} catch (AudioPlayerException e) {
					// TODO Auto-generated catch block
					Log.e("SriB", e.getMessage());
					e.printStackTrace();
				}
			} else {
				Log.d("SriB", "result == null");
			}
		}
	}

	private static String getFragmentTag(int viewPagerId, int index) {
		return "android:switcher:" + viewPagerId + ":" + index;
	}
}
