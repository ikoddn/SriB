package no.srib.app.client;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import no.srib.app.client.adapter.ArticleListAdapter;
import no.srib.app.client.adapter.PodcastGridAdapter;
import no.srib.app.client.adapter.ProgramSpinnerAdapter;
import no.srib.app.client.adapter.SectionsPagerAdapter;
import no.srib.app.client.adapter.updater.JsonAdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.audioplayer.AudioPlayer;
import no.srib.app.client.audioplayer.AudioPlayer.State;
import no.srib.app.client.audioplayer.AudioPlayerException;
import no.srib.app.client.fragment.ArticleListFragment;
import no.srib.app.client.fragment.ArticleListFragment.OnSearchListener;
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
import no.srib.app.client.model.ProgramName;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.AudioPlayerService;
import no.srib.app.client.service.BaseService;
import no.srib.app.client.service.ServiceHandler;
import no.srib.app.client.service.ServiceHandler.OnServiceReadyListener;
import no.srib.app.client.service.StreamUpdaterService;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener;
import no.srib.app.client.util.AsyncTaskCompleted;
import no.srib.app.client.util.AsyncTaskCompleted.AsyncTaskFinished;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
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

public class MainActivity extends FragmentActivity implements
		OnFragmentReadyListener {

	private ArticleListAdapter articleListAdapter;

	private HttpResponseListener currentProgramResponse;

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
	private AsyncTaskCompleted asyncTaskCompleted;

	// TODO Put this in SharedPrefrence.
	private String oldDataSource;

	public MainActivity() {
		articleListAdapter = null;
		viewPager = null;
		currentProgramResponse = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Resources res = getResources();

		asyncTaskCompleted = new AsyncTaskCompleted(new FragmentsReady(), 4);

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

		autoPlayAfterConnect = false;

		audioPlayerService = new ServiceHandler<AudioPlayerService>(
				AudioPlayerService.class, new AudioPlayerServiceReadyListener());

		streamUpdaterService = new ServiceHandler<StreamUpdaterService>(
				StreamUpdaterService.class,
				new StreamUpdaterServiceReadyListener());

		audioPlayerService.bind(MainActivity.this);
		streamUpdaterService.bind(MainActivity.this);

		articleListAdapter = new ArticleListAdapter(
				LayoutInflater.from(MainActivity.this));

		HttpResponseListener articleResponse = new ArticleHttpResponse(
				MainActivity.this, articleListAdapter);
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(articleResponse);
		String url = getResources().getString(R.string.url_articles);
		httpAsyncTask.execute(url);

		// Podcast Part
		podcastGridAdapter = new PodcastGridAdapter(MainActivity.this);
		programSpinnerAdapter = new ProgramSpinnerAdapter(MainActivity.this);

		JsonAdapterUpdater<ProgramName> programNameUpdater = new JsonAdapterUpdater<ProgramName>(
				ProgramName.class, programSpinnerAdapter);
		programNameUpdater.setDefaultValue(new ProgramName(0, res
				.getString(R.string.spinner_podcast_default)));

		HttpResponseListener programResponse = new ProgramNameHttpResponse(
				MainActivity.this, programSpinnerAdapter);
		HttpResponseListener podcastResponse = new PodcastHttpResponse(
				podcastGridAdapter);

		HttpAsyncTask programTask = new HttpAsyncTask(programResponse);
		HttpAsyncTask podcastTask = new HttpAsyncTask(podcastResponse);

		String programTaskUrl = res.getString(R.string.getAllProgramNames);
		String podcastTaskUrl = res.getString(R.string.getAllPodcast);

		podcastTask.execute(podcastTaskUrl);
		programTask.execute(programTaskUrl);
	}

	private class StreamUpdaterServiceReadyListener implements
			OnServiceReadyListener {

		@Override
		public void onServiceReady(BaseService baseService) {
			StreamUpdaterService service = (StreamUpdaterService) baseService;
			String radioUrl = getResources().getString(R.string.currentUrl);
			service.setStreamUpdateListener(new StreamUpdateListener());
			service.updateFrom(radioUrl);
		}
	}

	private class AudioPlayerServiceReadyListener implements
			OnServiceReadyListener {

		@Override
		public void onServiceReady(BaseService baseService) {
			AudioPlayerService audioPlayer = (AudioPlayerService) baseService;
			audioPlayer.setStateListener(new AudioPlayerStateListener());
		}
	}

	private class StreamUpdateListener implements OnStreamUpdateListener {

		@Override
		public void onStatus(Status status) {
			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			LiveRadioFragment fragment = (LiveRadioFragment) liveRadioSectionFragment
					.getBaseFragment();

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
			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			LiveRadioFragment fragment = null;

			if (liveRadioSectionFragment != null) {
				fragment = (LiveRadioFragment) liveRadioSectionFragment
						.getBaseFragment();
			}

			try {
				String url = streamSchedule.getUrl();

				if (url == null) {
					throw new AudioPlayerException();
				}

				AudioPlayerService audioPlayer = audioPlayerService
						.getService();
				audioPlayer.setDataSource(url);
				audioPlayer.setIsPodcast(false);
				fragment.setLiveRadioMode();
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

	public class AudioPlayerStateListener implements AudioPlayer.StateListener {

		@Override
		public void onStateChanged(AudioPlayer.State state) {
			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			LiveRadioFragment fragment = null;
			AudioPlayerService audioservice = audioPlayerService.getService();

			if (liveRadioSectionFragment != null) {
				fragment = (LiveRadioFragment) liveRadioSectionFragment
						.getBaseFragment();
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

				if (!streamUpdater.isUpdating()) {
					streamUpdater.update();
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
			StreamUpdaterService streamUpdater = streamUpdaterService
					.getService();

			audioPlayer.setIsPodcast(value);
			if (value) {
				Log.i("Debug", "Pod");

			} else {
				Log.i("Debug", "Live");
				oldDataSource = audioPlayer.getDataSource();

				if (currentProgramResponse != null) {
					HttpAsyncTask programTask = new HttpAsyncTask(
							currentProgramResponse);
					programTask.execute(getResources().getString(
							R.string.currentProgram));
				}

				streamUpdater.update();
				audioPlayer.start();

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
			LiveRadioFragment fragment = null;

			if (liveRadioSectionFragment != null) {
				fragment = (LiveRadioFragment) liveRadioSectionFragment
						.getBaseFragment();
			}

			if (fragment != null) {
				fragment.setProgramNameText(podcastName);
			}

			// TODO Info screen may be in focus, should show live radio screen
			viewPager
					.setCurrentItem(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			audioPlayer.setIsPodcast(true);
			fragment.setPodcastMode();
			audioPlayer.start();
		}
	}

	@Override
	public void onFragmentReady(final Fragment fragment) {
		if (fragment instanceof LiveRadioFragment) {
			LiveRadioFragment liveRadio = (LiveRadioFragment) fragment;

			liveRadio.setOnLiveRadioClickListener(new LiveRadioClickListener());
			liveRadio.setSeekBarOnChangeListener(new SeekBarListener());
			liveRadio.setSeekBarListener(new SeekBarImpl());

			TextView textView = liveRadio.getProgramNameTextView();
			currentProgramResponse = new CurrentScheduleHttpResponse(textView);

			HttpAsyncTask programName = new HttpAsyncTask(
					currentProgramResponse);
			String programNameURL = getResources().getString(
					R.string.currentProgram);

			programName.execute(programNameURL);
		} else if (fragment instanceof PodcastFragment) {
			PodcastFragment podcast = (PodcastFragment) fragment;

			podcast.setGridArrayAdapter(podcastGridAdapter);
			podcast.setPodCastClickedListener(new GridViewItemClickListener());
			podcast.setSpinnerListAdapter(programSpinnerAdapter);
			podcast.setSpinnerListSelectedListener(new ListViewItemClickListener());
		} else if (fragment instanceof ArticleListFragment) {
			ArticleListFragment articleList = (ArticleListFragment) fragment;

			articleList.setArticleListAdapter(articleListAdapter);
			articleList.setSearchListener(new ArticleSearch());
		} else if (fragment instanceof InfoFragment) {
			InfoFragment info = (InfoFragment) fragment;

			info.setInfoClickListener(new InfoClickListener());
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
			LiveRadioFragment liveFrag = (LiveRadioFragment) fragment
					.getChildFragmentManager().getFragments().get(0);
			AudioPlayerService audioservice = audioPlayerService.getService();

			int progress = audioservice.getProgress();
			int max = audioservice.getDuration();
			String time = fromMsToTime(progress);
			liveFrag.setTimeText(time);
			System.out.println(progress + "/" + max);
			liveFrag.setSeekBarProgress(progress);
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
		public void onSearch(String query) {
			HttpResponseListener articleResponse = new ArticleHttpResponse(
					MainActivity.this, articleListAdapter);
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(articleResponse);
			StringBuilder sb = new StringBuilder();
			sb.append(getResources().getString(R.string.url_articles));
			sb.append("?s=" + query);
			httpAsyncTask.execute(sb.toString());
		}
	}

	private class FragmentsReady implements AsyncTaskFinished {

		@Override
		public void onFinished() {
			LiveRadioSectionFragment fragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			fragment.startedUp();
		}
	}
}
