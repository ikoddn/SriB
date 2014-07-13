package no.srib.app.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import no.srib.app.client.adapter.ArticleListAdapter;
import no.srib.app.client.adapter.PodcastGridAdapter;
import no.srib.app.client.adapter.ProgramSpinnerAdapter;
import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.adapter.updater.JsonAdapterUpdater;
import no.srib.app.client.adapter.updater.ProgramNameAdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.dao.StreamScheduleDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.dao.sharedpreferences.StreamScheduleDAOImpl;
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
import no.srib.app.client.listener.OnSearchListener;
import no.srib.app.client.model.Article;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.model.PodcastPrograms;
import no.srib.app.client.model.ProgramName;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.receiver.ConnectivityChangeReceiver;
import no.srib.app.client.service.ServiceHandler;
import no.srib.app.client.service.StreamUpdaterService;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener;
import no.srib.app.client.service.audioplayer.AudioPlayerException;
import no.srib.app.client.service.audioplayer.AudioPlayerService;
import no.srib.app.client.service.audioplayer.AudioPlayerService.DataSourceType;
import no.srib.app.client.service.audioplayer.state.State;
import no.srib.app.client.service.audioplayer.state.StateListener;
import no.srib.app.client.util.BusProvider;
import no.srib.app.client.viewpager.PageChangeListener;
import no.srib.app.client.viewpager.SectionsPagerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

public class MainActivity extends FragmentActivity {

	enum Component {
		AUDIOPLAYER,
		STREAMUPDATER,
		LIVERADIOSECTION
	}

	private Map<Component, Boolean> readyComponents;

	private StreamScheduleDAO streamScheduleDAO;

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
		readyComponents = new EnumMap<Component, Boolean>(Component.class);
		Component[] components = Component.values();
		for (Component component : components) {
			readyComponents.put(component, false);
		}

		streamScheduleDAO = new StreamScheduleDAOImpl(this);

		articleListAdapter = null;
		viewPager = null;
		currentProgramResponse = null;
		audioPlayerService = null;
		streamUpdaterService = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BusProvider.INSTANCE.get().register(this);

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
				AudioPlayerService.class);

		streamUpdaterService = new ServiceHandler<StreamUpdaterService>(
				StreamUpdaterService.class);

		audioPlayerService.bind(MainActivity.this);
		streamUpdaterService.bind(MainActivity.this);

		articleListAdapter = new ArticleListAdapter(this);

		articleAdapterUpdater = new JsonAdapterUpdater<Article>(Article.class,
				articleListAdapter);
		HttpResponseListener articleResponse = new ArticleHttpResponse(
				MainActivity.this, articleAdapterUpdater, false);
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(articleResponse);
		String url = getResources().getString(R.string.url_articles);
		httpAsyncTask.execute(url);

		// Podcast Part
		podcastGridAdapter = new PodcastGridAdapter(this);
		programSpinnerAdapter = new ProgramSpinnerAdapter(this);

		AdapterUpdater<ProgramName, PodcastPrograms> programNameAdapterUpdater = new ProgramNameAdapterUpdater(
				programSpinnerAdapter);

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

		BusProvider.INSTANCE.get().unregister(this);
		unregisterReceiver(connectivityChangeReceiver);
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
		public void onStreamUpdate(StreamSchedule streamSchedule) {
			try {
				streamScheduleDAO.set(streamSchedule);
			} catch (DAOException e) {
			}

			updateStream(streamSchedule);

			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			liveRadioSectionFragment.replaceLoadingFragment();
		}
	}

	private void prepareLiveRadioIfReady() {
		if (readyComponents.get(Component.AUDIOPLAYER)
				&& readyComponents.get(Component.STREAMUPDATER)
				&& readyComponents.get(Component.LIVERADIOSECTION)) {

			StreamSchedule streamSchedule;

			try {
				StreamSchedule storedSchedule = streamScheduleDAO.get();

				long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;

				// Check if the stored schedule is out-of-date
				if (storedSchedule != null
						&& storedSchedule.getTime() > currentTime) {
					streamSchedule = storedSchedule;
				} else {
					streamSchedule = null;
				}
			} catch (DAOException e) {
				streamSchedule = null;
			}

			if (streamSchedule == null) {
				StreamUpdaterService streamUpdater = streamUpdaterService
						.getService();

				streamUpdater.update();
			} else {
				AudioPlayerService audioPlayer = audioPlayerService
						.getService();

				switch (audioPlayer.getState()) {
				case PAUSED:
				case PREPARING:
				case STARTED:
				case STOPPED:
					DataSourceType dataSourceType = audioPlayer
							.getDataSourceType();

					switch (dataSourceType) {
					case NONE:
					case PODCAST:
						updateStream(streamSchedule);
						break;
					case LIVE_RADIO:
					default:
						break;

					}
					break;
				case UNINITIALIZED:
				case COMPLETED:
					updateStream(streamSchedule);
					break;
				default:
					break;

				}

				LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
				liveRadioSectionFragment.replaceLoadingFragment();
			}
		}
	}

	private void updateStream(StreamSchedule streamSchedule) {
		LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

		LiveRadioFragment liveRadio = liveRadioSectionFragment
				.getLiveRadioFragment();

		try {
			String url = streamSchedule.getUrl();

			if (url == null) {
				throw new AudioPlayerException();
			}

			AudioPlayerService audioPlayer = audioPlayerService.getService();

			audioPlayer.setDataSource(url, DataSourceType.LIVE_RADIO);
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
				prepareLiveRadioIfReady();
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

				prepareLiveRadioIfReady();
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
			Podcast podcast = (Podcast) view.getTag();

			String filepath = podcast.getFilename();
			int lastSlashIndex = filepath.lastIndexOf("\\");
			String filename = filepath.substring(lastSlashIndex + 1);

			String nasUrl = getResources().getString(R.string.url_podcast_nas);
			String url = nasUrl + filename;

			try {
				audioPlayer.setDataSource(url, DataSourceType.PODCAST);
			} catch (AudioPlayerException e) {
				e.printStackTrace();
			}

			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

			if (liveRadioSectionFragment != null) {
				liveRadioSectionFragment.replaceLoadingFragment();

				LiveRadioFragment fragment = liveRadioSectionFragment
						.getLiveRadioFragment();
				fragment.setProgramNameText(podcast.getProgram());
				fragment.setPodcastMode();
			}

			viewPager
					.setCurrentItem(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			audioPlayer.start();
		}
	}

	@Subscribe
	public void onAudioPlayerServiceReady(final AudioPlayerService service) {
		service.setStateListener(new AudioPlayerStateListener());

		readyComponents.put(Component.AUDIOPLAYER, true);
		prepareLiveRadioIfReady();
	}

	@Subscribe
	public void onStreamUpdaterServiceReady(final StreamUpdaterService service) {
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

		readyComponents.put(Component.STREAMUPDATER, true);
		prepareLiveRadioIfReady();
	}

	@Subscribe
	public void onArticleListFragmentReady(final ArticleListFragment fragment) {
		fragment.setArticleListAdapter(articleListAdapter);
		fragment.setSearchListener(new ArticleSearch());
	}

	@Subscribe
	public void onInfoFragmentReady(final InfoFragment fragment) {
		fragment.setInfoClickListener(new InfoClickListener());
	}

	@Subscribe
	public void onLiveRadioFragmentReady(final LiveRadioFragment fragment) {
		fragment.setOnLiveRadioClickListener(new LiveRadioClickListener());
		fragment.setSeekBarOnChangeListener(new SeekBarListener());
		new SeekBarImpl();

		TextView textView = fragment.getProgramNameTextView();
		currentProgramResponse = new CurrentScheduleHttpResponse(textView);

		HttpAsyncTask programName = new HttpAsyncTask(currentProgramResponse);
		String programNameURL = getResources().getString(
				R.string.currentProgram);

		programName.execute(programNameURL);

		AudioPlayerService audioPlayer = audioPlayerService.getService();

		switch (audioPlayer.getState()) {
		case PREPARING:
		case STARTED:
			fragment.setPauseIcon();
			break;
		case COMPLETED:
		case PAUSED:
		case STOPPED:
		case UNINITIALIZED:
			break;
		}
	}

	@Subscribe
	public void onLiveRadioSectionFragmentReady(
			final LiveRadioSectionFragment fragment) {

		readyComponents.put(Component.LIVERADIOSECTION, true);
		prepareLiveRadioIfReady();
	}

	@Subscribe
	public void onPodcastFragmentReady(final PodcastFragment fragment) {
		fragment.setGridArrayAdapter(podcastGridAdapter);
		fragment.setSpinnerListAdapter(programSpinnerAdapter);
		fragment.setPodcastClickedListener(new GridViewItemClickListener());
		fragment.setSpinnerListSelectedListener(new ListViewItemClickListener());
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
