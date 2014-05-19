package no.srib.app.client;

import java.io.IOException;
import java.util.List;

import no.srib.app.client.adapter.ArticleListAdapter;
import no.srib.app.client.adapter.GridArrayAdapter;
import no.srib.app.client.adapter.SectionsPagerAdapter;
import no.srib.app.client.adapter.StableArrayAdapter;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.audioplayer.AudioPlayer;
import no.srib.app.client.audioplayer.AudioPlayerException;
import no.srib.app.client.fragment.ArticleListFragment;
import no.srib.app.client.fragment.ArticleListFragment.OnArticlesFragmentReadyListener;
import no.srib.app.client.fragment.ArticleSectionFragment;
import no.srib.app.client.fragment.LiveRadioFragment;
import no.srib.app.client.fragment.LiveRadioFragment.OnLiveRadioClickListener;
import no.srib.app.client.fragment.PodcastFragment;
import no.srib.app.client.fragment.PodcastFragment.OnPodcastFragmentReadyListener;
import no.srib.app.client.model.NewsArticle;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.model.ProgramName;
import no.srib.app.client.model.Schedule;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.AudioPlayerService;
import no.srib.app.client.service.StreamUpdaterService;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainActivity extends ActionBarActivity implements
		OnArticlesFragmentReadyListener, OnPodcastFragmentReadyListener {

	private final ObjectMapper MAPPER;
	private ArticleListAdapter articleListAdapter;

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

	private boolean autoPlayAfterConnect;

	private boolean audioPlayerServiceBound;
	private AudioPlayer audioPlayer;
	private ServiceConnection audioPlayerServiceConnection;

	private boolean streamUpdaterServiceBound;
	private StreamUpdaterService streamUpdater;
	private ServiceConnection streamUpdaterServiceConnection;

	private HttpAsyncTask programTask;
	private HttpAsyncTask podcastTask;

	private GridArrayAdapter gridViewAdapter = null;
	private StableArrayAdapter spinnerListAdapter = null;

	public MainActivity() {
		MAPPER = new ObjectMapper();
		articleListAdapter = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);
		viewPager.setCurrentItem(1);

		autoPlayAfterConnect = false;

		audioPlayerServiceBound = false;
		audioPlayer = null;
		audioPlayerServiceConnection = new AudioPlayerServiceConnection();

		streamUpdaterServiceBound = false;
		streamUpdater = null;
		streamUpdaterServiceConnection = new StreamUpdaterServiceConnection();

		bindServices();

		articleListAdapter = new ArticleListAdapter(
				LayoutInflater.from(MainActivity.this));

		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
				new ArticleHttpResponseListener());
		String url = getResources().getString(R.string.url_articles);
		httpAsyncTask.execute(url);

		// Podcast Part
		gridViewAdapter = new GridArrayAdapter(MainActivity.this);
		spinnerListAdapter = new StableArrayAdapter(MainActivity.this);

		programTask = new HttpAsyncTask(new GetProgramNames());
		podcastTask = new HttpAsyncTask(new GetAllPodcast());

		String programTaskUrl = getResources().getString(
				R.string.getAllProgramNames);
		String podcastTaskUrl = getResources()
				.getString(R.string.getAllPodcast);

		podcastTask.execute(podcastTaskUrl);
		programTask.execute(programTaskUrl);
		
		//ProgramName
		HttpAsyncTask programName = new HttpAsyncTask(new GetCurrentProgramName());
		
		String programNameURL = getResources().getString(R.string.currentProgram);
		
		programName.execute(programNameURL);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// unbindServices();
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

	public void bindServices() {
		// Establish a connection with the service. We use an explicit
		// class name because we want a specific service implementation that
		// we know will be running in our own process (and thus won't be
		// supporting component replacement by other applications).
		Context context = getApplicationContext();

		context.bindService(new Intent(MainActivity.this,
				AudioPlayerService.class), audioPlayerServiceConnection,
				Context.BIND_AUTO_CREATE);
		audioPlayerServiceBound = true;

		context.bindService(new Intent(MainActivity.this,
				StreamUpdaterService.class), streamUpdaterServiceConnection,
				Context.BIND_AUTO_CREATE);
		streamUpdaterServiceBound = true;
	}

	public void unbindServices() {
		Context context = getApplicationContext();

		if (audioPlayerServiceBound) {
			context.unbindService(audioPlayerServiceConnection);
			audioPlayerServiceBound = false;
		}

		if (streamUpdaterServiceBound) {
			context.unbindService(streamUpdaterServiceConnection);
			streamUpdaterServiceBound = false;
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

			audioPlayer.setStateListener(new AudioPlayerStateListener());

			LiveRadioFragment liveRadioFragment = (LiveRadioFragment) getFragment(SectionsPagerAdapter.LIVERADIO_FRAGMENT);

			if (liveRadioFragment != null) {
				liveRadioFragment
						.setOnLiveRadioClickListener(new LiveRadioClickListener());
			}
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

	private class StreamUpdaterServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			streamUpdater = ((StreamUpdaterService.StreamUpdaterBinder) service)
					.getService();
			String radioUrl = getResources().getString(R.string.currentUrl);
			streamUpdater.setStreamUpdateListener(new StreamUpdateListener());
			streamUpdater.updateFrom(radioUrl);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			streamUpdater = null;
		}
	}

	private class StreamUpdateListener implements OnStreamUpdateListener {

		@Override
		public void onStatus(Status status) {
			LiveRadioFragment fragment = (LiveRadioFragment) getFragment(SectionsPagerAdapter.LIVERADIO_FRAGMENT);

			if (fragment != null) {
				switch (status) {
				case NO_INTERNET:
					fragment.setStreamText("No internet connection");
					break;
				case SERVER_UNREACHABLE:
					fragment.setStreamText("Could not connect to server");
					break;
				case INVALID_RESPONSE:
					fragment.setStreamText("Invalid response from server");
					break;
				case CONNECTING:
					fragment.setStreamText("Connecting...");
					break;
				}
			}
		}

		@Override
		public void onStreamUpdate(StreamSchedule streamSchedule) {
			LiveRadioFragment fragment = (LiveRadioFragment) getFragment(SectionsPagerAdapter.LIVERADIO_FRAGMENT);

			try {
				String url = streamSchedule.getUrl();

				if (url == null) {
					throw new AudioPlayerException();
				}

				audioPlayer.setDataSource(url);

				if (autoPlayAfterConnect) {
					audioPlayer.start();
				}

				if (fragment != null) {
					fragment.setStreamText(streamSchedule.getName());
				}
			} catch (AudioPlayerException e) {
				if (fragment != null) {
					fragment.setStreamText("Playback error");
				}
			}
		}
	}

	private class AudioPlayerStateListener implements AudioPlayer.StateListener {

		@Override
		public void onStateChanged(AudioPlayer.State state) {
			LiveRadioFragment fragment = (LiveRadioFragment) getFragment(SectionsPagerAdapter.LIVERADIO_FRAGMENT);

			switch (state) {
			case PAUSED:
				fragment.setStatusText("paused");
				fragment.setPlayIcon();
				break;
			case PREPARING:
				fragment.setStatusText("preparing");
				fragment.setPauseIcon();
				break;
			case STARTED:
				fragment.setStatusText("started");
				fragment.setPauseIcon();
				break;
			case STOPPED:
				fragment.setStatusText("stopped");
				fragment.setPlayIcon();
				break;
			case UNINITIALIZED:
				fragment.setStatusText("uninitialized");
				break;
			case COMPLETED:
				fragment.setStatusText("completed");
				fragment.setPlayIcon();
				break;
			}
		}
	}

	private class LiveRadioClickListener implements OnLiveRadioClickListener {

		@Override
		public void onPlayClicked() {
			switch (audioPlayer.getState()) {
			case PAUSED:
			case PREPARING:
			case STOPPED:
				autoPlayAfterConnect = true;
				audioPlayer.start();
				break;
			case STARTED:
				autoPlayAfterConnect = false;
				audioPlayer.stop();
				break;
			case UNINITIALIZED:
			case COMPLETED:
				autoPlayAfterConnect = true;

				if (!streamUpdater.isUpdating()) {
					streamUpdater.update();
				}
				break;
			}
		}

		@Override
		public void onPauseClicked() {
			autoPlayAfterConnect = false;
			audioPlayer.pause();
		}
	}

	private class ListViewItemClickListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			PodcastFragment fragment = (PodcastFragment) getFragment(SectionsPagerAdapter.PODCAST_FRAGMENT);
			
			
			if (arg2 == 0) {

				HttpAsyncTask podcast = new HttpAsyncTask(new GetAllPodcast());
				String url = getResources().getString(R.string.getAllPodcast);
				podcast.execute(url);
				
			} else {
				Log.i("DEBUG", "DOWNLOADING NEW LIST");
				HttpAsyncTask podcast = new HttpAsyncTask(new GetAllPodcast());
				String url = getResources().getString(R.string.getAllPodcast);
				podcast.execute(url + "/" + arg0.getItemIdAtPosition(arg2));
			
			}
			GridView grid = fragment.getGridView();
			grid.smoothScrollToPosition(0);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	private class GridViewItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {

			String url = (String) view.getTag(R.id.podcast_url);
			String correctUrl = url.substring(3, url.length());
			String nasUrl = getResources().getString(R.string.podcast_nas_URL);
			String URL = nasUrl + correctUrl;
			try {
				audioPlayer.setDataSource(URL);
			} catch (AudioPlayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			viewPager.setCurrentItem(SectionsPagerAdapter.LIVERADIO_FRAGMENT);
			
			audioPlayer.start();
			
			
		}

	}

	public class GetProgramNames implements HttpResponseListener {

		@Override
		public void onResponse(String response) {

			List<ProgramName> list = null;

			if (response != null) {
				try {
					list = MAPPER.readValue(response,
							new TypeReference<List<ProgramName>>() {
							});
					
					list.add(0,new ProgramName(0, "Velg program"));
					spinnerListAdapter.setList(list);
					spinnerListAdapter.notifyDataSetChanged();
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
			}

		}

	}
	
	private class GetCurrentProgramName implements HttpResponseListener{

		@Override
		public void onResponse(String response) {
			Schedule schedule = null;
			
			if(response != null){
				try{
					schedule = MAPPER.readValue(response, Schedule.class);
					LiveRadioFragment fragment = (LiveRadioFragment) getFragment(SectionsPagerAdapter.LIVERADIO_FRAGMENT);
					fragment.setProgramNameText(schedule.getProgram());
				}catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
		
		
	}
	
	

	private class GetAllPodcast implements HttpResponseListener {

		@Override
		public void onResponse(String response) {
			List<Podcast> podcastList = null;

			if (response != null) {
				try {
					podcastList = MAPPER.readValue(response,
							new TypeReference<List<Podcast>>() {
							});
					gridViewAdapter.setList(podcastList);
					gridViewAdapter.notifyDataSetChanged();
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

			}

		}

	}

	private class ArticleHttpResponseListener implements HttpResponseListener {

		@Override
		public void onResponse(String response) {
			if (response != null) {
				try {
					List<NewsArticle> list = MAPPER.readValue(response,
							new TypeReference<List<NewsArticle>>() {
							});
					articleListAdapter.setList(list);
					articleListAdapter.notifyDataSetChanged();
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
			}
		}
	}

	@Override
	public void onArticlesFragmentReady() {
		ArticleSectionFragment fragment = (ArticleSectionFragment) getFragment(SectionsPagerAdapter.ARTICLESECTION_FRAGMENT);
		ArticleListFragment listFragment = (ArticleListFragment) fragment
				.getChildFragmentManager().getFragments().get(0);
		listFragment.setArticleListAdapter(articleListAdapter);
	}

	@Override
	public void onPodcastFragmentReady() {
		PodcastFragment fragment = (PodcastFragment) getFragment(SectionsPagerAdapter.PODCAST_FRAGMENT);
		fragment.setGridArrayAdapter(gridViewAdapter);
		fragment.setPodCastClickedListener(new GridViewItemClickListener());
		fragment.setSpinnerListAdapter(spinnerListAdapter);
		fragment.setSpinnerListSelectedListener(new ListViewItemClickListener());

	}

	@Override
	public void onBackPressed() {
		boolean close = true;
		int id = SectionsPagerAdapter.ARTICLESECTION_FRAGMENT;

		if (viewPager.getCurrentItem() == id) {
			ArticleSectionFragment fragment = (ArticleSectionFragment) getFragment(id);
			close = !fragment.backPressed();
		}

		if (close) {
			super.onBackPressed();
		}
	}

	private Fragment getFragment(int index) {
		String tag = getFragmentTag(viewPager.getId(), index);
		return getSupportFragmentManager().findFragmentByTag(tag);
	}

	private static String getFragmentTag(int viewPagerId, int index) {
		return "android:switcher:" + viewPagerId + ":" + index;
	}
}
