package no.srib.app.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import no.srib.app.client.adapter.ArticleListAdapter;
import no.srib.app.client.adapter.PodcastGridAdapter;
import no.srib.app.client.adapter.ProgramSpinnerAdapter;
import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.adapter.updater.JsonAdapterUpdater;
import no.srib.app.client.adapter.updater.ProgramNameAdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.fragment.ArticleListFragment;
import no.srib.app.client.fragment.InfoFragment;
import no.srib.app.client.fragment.InfoFragment.OnInfoClickListener;
import no.srib.app.client.fragment.LiveRadioFragment;
import no.srib.app.client.fragment.LiveRadioFragment.OnLiveRadioClickListener;
import no.srib.app.client.fragment.LiveRadioFragment.SeekBarInterface;
import no.srib.app.client.fragment.LiveRadioSectionFragment;
import no.srib.app.client.fragment.PodcastFragment;
import no.srib.app.client.fragment.SectionFragment;
import no.srib.app.client.http.ArticleHttpResponse;
import no.srib.app.client.http.CurrentScheduleHttpResponse;
import no.srib.app.client.http.PodcastHttpResponse;
import no.srib.app.client.http.ProgramNameHttpResponse;
import no.srib.app.client.listener.OnFragmentReadyListener;
import no.srib.app.client.listener.OnSearchListener;
import no.srib.app.client.model.Article;
import no.srib.app.client.model.PodcastPrograms;
import no.srib.app.client.model.ProgramName;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.receiver.ConnectivityChangeReceiver;
import no.srib.app.client.service.BaseService;
import no.srib.app.client.service.ServiceHandler;
import no.srib.app.client.service.ServiceHandler.OnServiceReadyListener;
import no.srib.app.client.service.StreamUpdaterService;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener;
import no.srib.app.client.service.audioplayer.AudioPlayerException;
import no.srib.app.client.service.audioplayer.AudioPlayerService;
import no.srib.app.client.service.audioplayer.state.State;
import no.srib.app.client.service.audioplayer.state.StateListener;
import no.srib.app.client.viewpager.PageChangeListener;
import no.srib.app.client.viewpager.SectionsPagerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainActivity extends FragmentActivity implements
		OnFragmentReadyListener {

	private static final String PREFS_NAME = "MainActivityPrefs";
	private static final String KEY_STREAM = "Stream";

	private final ObjectMapper MAPPER;

	private boolean readyToUpdateStreamSchedule;
	private StreamSchedule streamSchedule;
	private ConnectivityChangeReceiver connectivityChangeReceiver;

	private ArticleListAdapter articleListAdapter;

	private HttpResponseListener currentProgramResponse;

	private AdapterUpdater<Article, String> articleAdapterUpdater;

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

	private ServiceHandler<AudioPlayerService> audioPlayerService;
	private ServiceHandler<StreamUpdaterService> streamUpdaterService;

	private PodcastGridAdapter podcastGridAdapter;
	private ProgramSpinnerAdapter programSpinnerAdapter;
	private Handler seekHandler = new Handler();
	private Runnable run;
	private int updateTimeTextIntervall = 1000;

	public MainActivity() {
		MAPPER = new ObjectMapper();

		articleListAdapter = null;
		viewPager = null;
		currentProgramResponse = null;
		audioPlayerService = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		readyToUpdateStreamSchedule = false;

		SharedPreferences sharedPrefs = getSharedPreferences(PREFS_NAME, 0);
		String json = sharedPrefs.getString(KEY_STREAM, null);

		if (json != null) {
			try {
				StreamSchedule storedSchedule = MAPPER.readValue(json,
						StreamSchedule.class);

				long currentTime = Calendar.getInstance().getTimeInMillis();

				// Check if the stored schedule is out-of-date
				if (storedSchedule.getTime() > currentTime) {
					streamSchedule = storedSchedule;
				} else {
					streamSchedule = null;
				}
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
		} else {
			streamSchedule = null;
		}

		Resources res = getResources();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);
		viewPager
				.setCurrentItem(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

		viewPager.setOnPageChangeListener(new PageChangeListener(
				MainActivity.this, viewPager));

		autoPlayAfterConnect = false;

		audioPlayerService = new ServiceHandler<AudioPlayerService>(
				AudioPlayerService.class, new AudioPlayerServiceReadyListener());

		streamUpdaterService = new ServiceHandler<StreamUpdaterService>(
				StreamUpdaterService.class,
				new StreamUpdaterServiceReadyListener());

		audioPlayerService.bind(MainActivity.this);
		streamUpdaterService.bind(MainActivity.this);

		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/clairehandbold.ttf");

		articleListAdapter = new ArticleListAdapter(inflater);

		articleAdapterUpdater = new JsonAdapterUpdater<Article>(Article.class,
				articleListAdapter);
		HttpResponseListener articleResponse = new ArticleHttpResponse(
				MainActivity.this, articleAdapterUpdater, false);
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(articleResponse);
		String url = getResources().getString(R.string.url_articles);
		httpAsyncTask.execute(url);

		// Podcast Part
		podcastGridAdapter = new PodcastGridAdapter(inflater, font);
		programSpinnerAdapter = new ProgramSpinnerAdapter(inflater, font);

		JsonAdapterUpdater<ProgramName> programNameUpdater = new JsonAdapterUpdater<ProgramName>(
				ProgramName.class, programSpinnerAdapter);
		programNameUpdater.setDefaultValue(new ProgramName(0, res
				.getString(R.string.spinner_podcast_default)));

		AdapterUpdater<ProgramName, PodcastPrograms> programNameAdapterUpdater = new ProgramNameAdapterUpdater(
				programSpinnerAdapter);
		programNameAdapterUpdater.setDefaultValue(new ProgramName(0,
				getResources().getString(R.string.spinner_podcast_default)));

		HttpResponseListener programResponse = new ProgramNameHttpResponse(
				programNameAdapterUpdater);
		HttpResponseListener podcastResponse = new PodcastHttpResponse(
				podcastGridAdapter);

		HttpAsyncTask programTask = new HttpAsyncTask(programResponse);
		HttpAsyncTask podcastTask = new HttpAsyncTask(podcastResponse);

		String programTaskUrl = res.getString(R.string.url_podcastprograms);
		String podcastTaskUrl = res.getString(R.string.getAllPodcast);

		podcastTask.execute(podcastTaskUrl);
		programTask.execute(programTaskUrl);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(connectivityChangeReceiver);
	}

	private class StreamUpdaterServiceReadyListener implements
			OnServiceReadyListener {

		@Override
		public void onServiceReady(BaseService baseService) {
			StreamUpdaterService service = (StreamUpdaterService) baseService;
			service.setStreamUpdateListener(new StreamUpdateListener());
			String url = getResources().getString(R.string.url_audiostream);
			service.setUpdateURL(url);

			connectivityChangeReceiver = new ConnectivityChangeReceiver();
			connectivityChangeReceiver
					.setConnectionChangedListener(new ConnectivityChangedListener(
							service));

			IntentFilter filter = new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION);
			registerReceiver(connectivityChangeReceiver, filter);

			if (streamSchedule == null) {
				service.update();
			}
		}
	}

	private class AudioPlayerServiceReadyListener implements
			OnServiceReadyListener {

		@Override
		public void onServiceReady(BaseService baseService) {
			AudioPlayerService audioPlayer = (AudioPlayerService) baseService;
			audioPlayer.setStateListener(new AudioPlayerStateListener());

			updateStreamIfReady();
		}
	}

	private class StreamUpdateListener implements OnStreamUpdateListener {

		@Override
		public void onStatus(Status status) {
			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

			LiveRadioFragment liveRadio = liveRadioSectionFragment
					.getLiveRadioFragment();

			if (liveRadio != null) {
				switch (status) {
				case NO_INTERNET:
					liveRadio.setStreamText("No internet connection");
					break;
				case SERVER_UNREACHABLE:
					liveRadio.setStreamText("Could not connect to server");
					break;
				case INVALID_RESPONSE:
					liveRadio.setStreamText("Invalid response from server");
					break;
				case CONNECTING:
					liveRadio.setStreamText("Connecting...");
					break;
				}
			}
		}

		@Override
		public void onStreamUpdate(StreamSchedule newStreamSchedule) {
			streamSchedule = newStreamSchedule;

			try {
				String json = MAPPER.writeValueAsString(newStreamSchedule);

				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(KEY_STREAM, json);
				editor.commit();
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			updateStreamIfReady();
		}
	}

	private void updateStreamIfReady() {
		if (!readyToUpdateStreamSchedule) {
			readyToUpdateStreamSchedule = true;
		} else if (streamSchedule != null) {
			updateStream(streamSchedule);
		}
	}

	private void updateStream(StreamSchedule streamSchedule) {
		LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

		LiveRadioFragment liveRadio = liveRadioSectionFragment
				.getLiveRadioFragment();

		try {
			liveRadioSectionFragment.replaceLoadingFragment();

			String url = streamSchedule.getUrl();

			if (url == null) {
				throw new AudioPlayerException();
			}

			AudioPlayerService audioPlayer = audioPlayerService.getService();
			audioPlayer.setDataSource(url);
			audioPlayer.setIsPodcast(false);
			liveRadio.setLiveRadioMode();

			if (autoPlayAfterConnect) {
				audioPlayer.start();
			}

			liveRadio.setStreamText(streamSchedule.getName());
		} catch (AudioPlayerException e) {
			e.printStackTrace();
			liveRadio.setStreamText("Playback error");
		}
	}

	public class AudioPlayerStateListener implements StateListener {

		@Override
		public void onStateChanged(State state) {
			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			LiveRadioFragment fragment = null;
			AudioPlayerService audioservice = audioPlayerService.getService();

			if (liveRadioSectionFragment != null) {
				fragment = liveRadioSectionFragment.getLiveRadioFragment();
			}

			switch (state) {
			case PAUSED:
				fragment.setStatusText("paused");
				fragment.setPlayIcon();
				seekHandler.removeCallbacks(run);
				break;
			case PREPARING:
				fragment.setStatusText("preparing");
				fragment.setPauseIcon();
				break;
			case STARTED:
				fragment.setStatusText("started");
				fragment.setPauseIcon();
				seekHandler.removeCallbacks(run);
				int duration = audioservice.getDuration();
				fragment.setMaxOnSeekBar(duration);
				SeekBarInterface seekBar = new SeekBarImpl();
				seekBar.updateSeekBar();
				break;
			case STOPPED:
				fragment.setStatusText("stopped");
				fragment.setPlayIcon();
				seekHandler.removeCallbacks(run);

				break;
			case UNINITIALIZED:
				fragment.setStatusText("uninitialized");
				break;
			case COMPLETED:
				fragment.setStatusText("completed");
				fragment.setPlayIcon();
				seekHandler.removeCallbacks(run);
				break;
			}
		}
	}

	private class LiveRadioClickListener implements OnLiveRadioClickListener {

		@Override
		public void onPlayPauseClicked() {
			AudioPlayerService audioPlayer = audioPlayerService.getService();

			switch (audioPlayer.getState()) {
			case PAUSED:
			case PREPARING:
			case STOPPED:
				autoPlayAfterConnect = true;
				audioPlayer.start();
				break;
			case STARTED:
				autoPlayAfterConnect = false;
				audioPlayer.pause();
				break;
			case UNINITIALIZED:
			case COMPLETED:
				autoPlayAfterConnect = true;

				StreamUpdaterService streamUpdater = streamUpdaterService
						.getService();

				if (!streamUpdater.hasUpdateScheduled()) {
					streamUpdater.update();
				} else {
					updateStreamIfReady();
				}
				break;
			}
		}

		@Override
		public void onStopClicked() {
			autoPlayAfterConnect = false;
			AudioPlayerService audioPlayer = audioPlayerService.getService();
			audioPlayer.stop();
		}

		@Override
		public void onInstagramClicked() {
			openURL(R.string.url_instagram);
		}

		@Override
		public void onSoundCloudClicked() {
			openURL(R.string.url_soundcloud);
		}

		@Override
		public void onTwitterClicked() {
			openURL(R.string.url_twitter);
		}

		@Override
		public void onSwitchPodcastSelected(boolean value) {
			AudioPlayerService audioPlayer = audioPlayerService.getService();

			audioPlayer.setIsPodcast(value);
			if (value) {
				Log.i("Debug", "Pod");

			} else {
				Log.i("Debug", "Live");

				if (currentProgramResponse != null) {
					HttpAsyncTask programTask = new HttpAsyncTask(
							currentProgramResponse);
					programTask.execute(getResources().getString(
							R.string.currentProgram));
				}

				updateStreamIfReady();
			}
		}

		@Override
		public void onInfoClicked() {
			SectionFragment fragment = (SectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			fragment.pushFragment(new InfoFragment());
		}
	}

	private class InfoClickListener implements OnInfoClickListener {

		@Override
		public void onFacebookClicked() {
			openURL(R.string.url_facebook);
		}

		@Override
		public void onSpotifyClicked() {
			try {
				// Try to open Spotify app
				openURL(R.string.scheme_uri_spotify);
			} catch (ActivityNotFoundException e) {
				// Spotify app not installed, fall back to profile web site
				openURL(R.string.url_spotify);
			}
		}

		@Override
		public void onSribwwwClicked() {
			openURL(R.string.url_sribwww);
		}
	}

	private class ListViewItemClickListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			PodcastFragment fragment = (PodcastFragment) getFragment(SectionsPagerAdapter.PODCAST_FRAGMENT);

			HttpResponseListener podcastResponse = new PodcastHttpResponse(
					podcastGridAdapter);
			HttpAsyncTask podcastTask = new HttpAsyncTask(podcastResponse);
			String url = getResources().getString(R.string.getAllPodcast);

			if (position != 0) {
				url += "/" + parent.getItemIdAtPosition(position);
			}

			podcastTask.execute(url);

			GridView grid = fragment.getGridView();
			grid.smoothScrollToPosition(0);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private class GridViewItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {

			AudioPlayerService audioPlayer = audioPlayerService.getService();
			String url = (String) view.getTag(R.id.podcast_url);
			String podcastName = (String) view.getTag(R.id.podcast_name);
			String correctUrl = url.substring(3, url.length());
			String nasUrl = getResources().getString(R.string.podcast_nas_URL);
			String URL = nasUrl + correctUrl;
			System.out.println(URL);
			try {
				audioPlayer.setDataSource(URL);
			} catch (AudioPlayerException e) {

				e.printStackTrace();
			}

			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

			if (liveRadioSectionFragment != null) {
				liveRadioSectionFragment.replaceLoadingFragment();

				LiveRadioFragment fragment = liveRadioSectionFragment
						.getLiveRadioFragment();
				fragment.setProgramNameText(podcastName);
				fragment.setPodcastMode();
			}

			// TODO Info screen may be in focus, should show live radio screen
			viewPager
					.setCurrentItem(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			audioPlayer.setIsPodcast(true);
			audioPlayer.start();
		}
	}

	@Override
	public void onFragmentReady(final Fragment fragment) {
		if (fragment instanceof LiveRadioFragment) {
			LiveRadioFragment liveRadio = (LiveRadioFragment) fragment;

			liveRadio.setOnLiveRadioClickListener(new LiveRadioClickListener());
			liveRadio.setSeekBarOnChangeListener(new SeekBarListener());
			new SeekBarImpl();

			TextView textView = liveRadio.getProgramNameTextView();
			currentProgramResponse = new CurrentScheduleHttpResponse(textView);

			HttpAsyncTask programName = new HttpAsyncTask(
					currentProgramResponse);
			String programNameURL = getResources().getString(
					R.string.currentProgram);

			programName.execute(programNameURL);

			AudioPlayerService audioPlayer = audioPlayerService.getService();

			switch (audioPlayer.getState()) {
			case PREPARING:
			case STARTED:
				liveRadio.setPauseIcon();
				break;
			case COMPLETED:
			case PAUSED:
			case STOPPED:
			case UNINITIALIZED:
				break;
			}
		} else if (fragment instanceof PodcastFragment) {
			PodcastFragment podcast = (PodcastFragment) fragment;

			podcast.setGridArrayAdapter(podcastGridAdapter);
			podcast.setPodcastClickedListener(new GridViewItemClickListener());
			podcast.setSpinnerListAdapter(programSpinnerAdapter);
			podcast.setSpinnerListSelectedListener(new ListViewItemClickListener());
		} else if (fragment instanceof ArticleListFragment) {
			ArticleListFragment articleList = (ArticleListFragment) fragment;

			articleList.setArticleListAdapter(articleListAdapter);
			articleList.setSearchListener(new ArticleSearch());
		} else if (fragment instanceof InfoFragment) {
			InfoFragment info = (InfoFragment) fragment;

			info.setInfoClickListener(new InfoClickListener());
		} else if (fragment instanceof LiveRadioSectionFragment) {
			updateStreamIfReady();
		}
	}

	private void openURL(int resId) {
		String url = getResources().getString(resId);
		final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri
				.parse(url));
		startActivity(intent);
	}

	private class SeekBarListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			AudioPlayerService audioservice = audioPlayerService.getService();
			State currentState = audioservice.getState();
			if (fromUser && currentState == State.STARTED
					|| currentState == State.PAUSED) {
				System.out.println(progress + " " + seekBar.getMax());

				audioservice.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			seekHandler.removeCallbacks(run);
			updateTimeTextIntervall = 10;
			seekHandler.postDelayed(run, updateTimeTextIntervall);

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			seekHandler.removeCallbacks(run);
			updateTimeTextIntervall = 1000;
			seekHandler.postDelayed(run, updateTimeTextIntervall);

		}

	}

	@Override
	public void onBackPressed() {
		boolean close = true;

		int id = viewPager.getCurrentItem();

		switch (id) {
		case SectionsPagerAdapter.ARTICLE_SECTION_FRAGMENT:
		case SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT:
			SectionFragment fragment = (SectionFragment) getFragment(id);
			close = !fragment.popFragment();
			break;
		default:
			break;
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

	private class SeekBarImpl implements SeekBarInterface {

		public SeekBarImpl() {
			run = new Runnable() {

				@Override
				public void run() {
					SeekBarInterface sek = new SeekBarImpl();
					sek.updateSeekBar();
				}
			};
		}

		@Override
		public void updateSeekBar() {
			LiveRadioSectionFragment fragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

			if (fragment != null) {
				LiveRadioFragment liveFrag = (LiveRadioFragment) fragment
						.getChildFragmentManager().getFragments().get(0);
				AudioPlayerService audioservice = audioPlayerService
						.getService();

				int progress = audioservice.getProgress();
				String time = fromMsToTime(progress);
				liveFrag.setTimeText(time);
				// int max = audioservice.getDuration();
				// System.out.println(progress + "/" + max);
				liveFrag.setSeekBarProgress(progress);
			}

			seekHandler.postDelayed(run, updateTimeTextIntervall);
		}

		private String fromMsToTime(int ms) {
			String time = "";
			long hours = TimeUnit.MILLISECONDS.toHours(ms);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
					- TimeUnit.HOURS.toMinutes(hours);
			long seconds = TimeUnit.MILLISECONDS.toSeconds(ms)
					- TimeUnit.MINUTES.toSeconds(minutes);

			Locale locale = Locale.getDefault();
			if (hours == 0) {
				time = String.format(locale, "%02d:%02d", minutes, seconds);
			} else {
				time = String.format(locale, "%02d:%02d:%02d", hours, minutes,
						seconds);
			}

			return time;
		}

	}

	private class ArticleSearch implements OnSearchListener {

		@Override
		public void onSearch(final String query) {
			try {
				String urlEncodedQuery = URLEncoder.encode(query, "UTF-8");

				HttpResponseListener articleResponse = new ArticleHttpResponse(
						MainActivity.this, articleAdapterUpdater, true);
				HttpAsyncTask httpAsyncTask = new HttpAsyncTask(articleResponse);

				StringBuilder sb = new StringBuilder();
				sb.append(getResources().getString(R.string.url_articles));
				sb.append("?q=" + urlEncodedQuery);

				httpAsyncTask.execute(sb.toString());
			} catch (UnsupportedEncodingException e) {
			}
		}

		@Override
		public void restorePreSearchData() {
			articleAdapterUpdater.restoreStoredData();
		}
	}
}
