package no.srib.app.client;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import no.srib.app.client.adapter.ArticleListAdapter;
import no.srib.app.client.adapter.PodcastGridAdapter;
import no.srib.app.client.adapter.ProgramSpinnerAdapter;
import no.srib.app.client.adapter.SectionsPagerAdapter;
import no.srib.app.client.asynctask.ArticleAsyncTask;
import no.srib.app.client.asynctask.PodcastAsyncTask;
import no.srib.app.client.asynctask.PodcastProgramsAsyncTask;
import no.srib.app.client.asynctask.ScheduleAsyncTask;
import no.srib.app.client.db.DataSource;
import no.srib.app.client.event.ManualExitEvent;
import no.srib.app.client.event.handler.ArticleSearchHandler;
import no.srib.app.client.event.handler.AudioPlayerHandler;
import no.srib.app.client.event.handler.ConnectivityChangedHandler;
import no.srib.app.client.event.handler.InfoClickHandler;
import no.srib.app.client.event.handler.PageChangeHandler;
import no.srib.app.client.event.handler.PhoneStateHandler;
import no.srib.app.client.fragment.ArticleListFragment;
import no.srib.app.client.fragment.InfoFragment;
import no.srib.app.client.fragment.LiveRadioFragment;
import no.srib.app.client.fragment.LiveRadioFragment.OnLiveRadioClickListener;
import no.srib.app.client.fragment.LiveRadioSectionFragment;
import no.srib.app.client.activities.PodcastActivity;
import no.srib.app.client.fragment.PodcastListFragment;
import no.srib.app.client.fragment.SectionFragment;
import no.srib.app.client.imageloader.UrlImageLoaderSimple;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.model.Schedule;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.notification.NotificationService;
import no.srib.app.client.receiver.ConnectivityChangeReceiver;
import no.srib.app.client.service.ServiceBinder;
import no.srib.app.client.service.StreamUpdaterService;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener;
import no.srib.app.client.service.audioplayer.AudioPlayerException;
import no.srib.app.client.service.audioplayer.AudioPlayerService;
import no.srib.app.client.service.audioplayer.DataSourceType;
import no.srib.app.client.service.audioplayer.state.State;
import no.srib.app.client.service.audioplayer.state.StateListener;
import no.srib.app.client.util.AudioMetaUtil;
import no.srib.app.client.util.BusProvider;
import no.srib.app.client.util.Logger;
import no.srib.app.client.util.NetworkUtil;
import no.srib.app.client.view.CircleIshPageIndicator;

//import no.srib.app.client.util.PodcastDownloader;
public class MainActivity extends FragmentActivity {
	private static MainActivity inst;

    private ProgressBar bar;
    private int status;
    private Handler handler;
    private static final int SEEKBAR_UPDATE_INTERVAL = 1000;
    private SharedPreferences sharedPref;
    private CircleIshPageIndicator viewPageIndicator;

    enum Component {
        AUDIOPLAYER,
        STREAMUPDATER,
        LIVERADIOSECTION
    }

    private Map<Component, Boolean> readyComponents;

    private ConnectivityChangeReceiver connectivityChangeReceiver;

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

    private AudioPlayerService audioPlayer;
    private StreamUpdaterService streamUpdater;

	private ArticleListAdapter articleListAdapter;
	private PodcastGridAdapter podcastGridAdapter;
	private ProgramSpinnerAdapter programSpinnerAdapter;
	public static Schedule schedule; // TODO: share this in a better way

    private Handler seekbarHandler;
    private Runnable seekbarUpdater;

//	private NotificationService notification;
//	private static MainActivity mainActivity;

    public MainActivity() {
        readyComponents = new EnumMap<Component, Boolean>(Component.class);
        Component[] components = Component.values();
        for (Component component : components) {
            readyComponents.put(component, false);
        }

        articleListAdapter = null;
        viewPager = null;
        audioPlayer = null;
        streamUpdater = null;

        schedule = new Schedule();

        seekbarHandler = new Handler();
        seekbarUpdater = new SeekbarUpdater();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		inst = this;
    	BusProvider.INSTANCE.get().register(this);

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

		viewPager.setOnPageChangeListener(new PageChangeHandler(
				MainActivity.this, viewPager));

		viewPageIndicator = (CircleIshPageIndicator)findViewById(R.id.indicator);
		viewPageIndicator.setViewPager(viewPager);
		viewPageIndicator.setCurrentItem(1);
		DataSource.inst(this);

		new ServiceBinder<AudioPlayerService>(AudioPlayerService.class)
				.bind(this);

		new ServiceBinder<StreamUpdaterService>(StreamUpdaterService.class)
				.bind(this);

		articleListAdapter = new ArticleListAdapter(this);
		podcastGridAdapter = new PodcastGridAdapter(this);

		programSpinnerAdapter = new ProgramSpinnerAdapter(this);
		UrlImageLoaderSimple.init(this);
		sharedPref = getSharedPreferences(
				getString(R.string.podcast_preference_file), Context.MODE_PRIVATE);
	}



    @Override
	protected void onPause() {
		super.onPause();

//		if(notification != null)
//			notification.show();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// start the notification when audioPlayer is ready
//		if(notification != null)
//			notification.hide();

		updateContent();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// TODO: How do we shut down the notification here??????
		exitCleanup();
	}

	public void updateContent() {
		new ArticleAsyncTask(this, articleListAdapter).execute();
		new PodcastAsyncTask(this, podcastGridAdapter).execute();
		new PodcastProgramsAsyncTask(this, programSpinnerAdapter).execute();
		new ScheduleAsyncTask(this, schedule).execute();
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
					liveRadioSectionFragment.replaceLoadingFragment();
					liveRadio.setStreamText("No internet connection");
					liveRadio.setProgramNameText(getResources().getString(
							R.string.textview_liveradio_error_nointernet));
					break;
				case SERVER_UNREACHABLE:
					liveRadioSectionFragment.replaceLoadingFragment();
					liveRadio.setStreamText("Could not connect to server");
					liveRadio
							.setProgramNameText(getResources()
									.getString(
											R.string.textview_liveradio_error_serverunreachable));
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
			audioPlayer.setCurrentStream(streamSchedule);

			switch (audioPlayer.getDataSourceType()) {
			case PODCAST:
				break;
			case LIVE_RADIO:
			case NONE:
			default:
				try {
					audioPlayer.setCurrentStreamAsSource();
				} catch (AudioPlayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			liveRadioSectionFragment.replaceLoadingFragment();
		}
	}

	private void prepareLiveRadioIfReady() {
		if (readyComponents.get(Component.AUDIOPLAYER)
				&& readyComponents.get(Component.STREAMUPDATER)
				&& readyComponents.get(Component.LIVERADIOSECTION)) {

			if (NetworkUtil.networkAvailable(this)) {
				streamUpdater.onNetworkAvailable();
			} else {
				streamUpdater.onNetworkUnavailable();
			}
		}
	}

	private void setProgramNameText() {
		String programName = null;

		LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

		if (liveRadioSectionFragment != null) {
			LiveRadioFragment fragment = liveRadioSectionFragment
					.getLiveRadioFragment();

			programName = AudioMetaUtil.getProgramName(audioPlayer);

			if (programName != null) {
				fragment.setProgramNameText(programName);
			}
		}
	}

	private void clearPodcastPosition() {
		if(audioPlayer.getDataSourceType() == DataSourceType.PODCAST) {
			sharedPref
					.edit()
					.remove("podcast:" + audioPlayer.getCurrentPodcast().getRefnr() + ":pos")
					.apply();
		}
	}

	private void savePodcastPosition() {
		if(audioPlayer.getDataSourceType() == DataSourceType.PODCAST
				&& audioPlayer.getProgress() > 0) {
			sharedPref
					.edit()
					.putInt("podcast:" + audioPlayer.getCurrentPodcast().getRefnr() + ":pos"
							, audioPlayer.getProgress())
					.apply();
		}
	}

	private void seekSavedPodcastPosition() {
		if(audioPlayer.getDataSourceType() == DataSourceType.PODCAST) {
			int t = sharedPref.getInt("podcast:" + audioPlayer.getCurrentPodcast().getRefnr() + ":pos", 0);
			if(Math.abs(t - audioPlayer.getProgress()) > 5000)
				audioPlayer.seekTo(sharedPref.getInt("podcast:" + audioPlayer.getCurrentPodcast().getRefnr() + ":pos", 0));
		}
	}

	public class AudioPlayerStateListener implements StateListener {

		@Override
		public void onStateChanged(State state) {
			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

			if (liveRadioSectionFragment != null) {
				LiveRadioFragment fragment = liveRadioSectionFragment
						.getLiveRadioFragment();

				seekbarHandler.removeCallbacks(seekbarUpdater);

				switch (state) {
				case PAUSED:
					// save position on pause
					savePodcastPosition();
					fragment.setStatusText("paused");
					fragment.setPlayIcon();
					break;
				case PREPARING:
					fragment.setStatusText("preparing");
					fragment.setTimeText("");
					fragment.setPauseIcon();

					String buffering = getResources().getString(
							R.string.textview_liveradio_buffering);
					fragment.setProgramNameText(buffering);
					break;
				case STARTED:
					fragment.setStatusText("started");
					fragment.setPauseIcon();

					switch (audioPlayer.getDataSourceType()) {
					case LIVE_RADIO:
						String liveText = getResources().getString(
								R.string.textView_liveradio_time_live);
						fragment.setTimeText(liveText);
						break;
					case PODCAST:
						seekSavedPodcastPosition();
						fragment.setMaxOnSeekBar(audioPlayer.getDuration());
						seekbarHandler.postDelayed(seekbarUpdater, 0);
						break;
					case NONE:
					default:
						break;
					}

					setProgramNameText();

					break;
				case STOPPED:
					// save position on stopped
					savePodcastPosition();
					fragment.setStatusText("stopped");
					fragment.setPlayIcon();
					fragment.setTimeText("");
					break;
				case UNINITIALIZED:
					fragment.setStatusText("uninitialized");
					fragment.setTimeText("");
					break;
				case COMPLETED:
					clearPodcastPosition();
					fragment.setStatusText("completed");
					fragment.setPlayIcon();
					fragment.setTimeText("");

					if (!NetworkUtil.networkAvailable(MainActivity.this)) {
						String error = getResources().getString(
								R.string.textview_liveradio_error_nointernet);
						fragment.setProgramNameText(error);
					}
					break;
				}
			}
		}
	}

	private class LiveRadioClickListener implements OnLiveRadioClickListener {

		@Override
		public void onPlayPauseClicked() {
			switch (audioPlayer.getState()) {
			case PAUSED:
			case PREPARING:
			case STOPPED:
				audioPlayer.start();
				break;
			case STARTED:
				audioPlayer.pause();
				break;
			case UNINITIALIZED:
			case COMPLETED:
				try {
					switch (audioPlayer.getDataSourceType()) {
					case PODCAST:
						audioPlayer.setCurrentPodcastAsSource();
						break;
					case LIVE_RADIO:
					case NONE:
					default:
						audioPlayer.setCurrentStreamAsSource();
						break;
					}

					audioPlayer.start();
				} catch (AudioPlayerException e) {
					e.printStackTrace();
				}

				break;
			}
		}

		@Override
		public void onStopClicked() {
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
		public void onRadioPodcastSwitchToggled(final boolean podcast) {
			if (!podcast) {

				seekbarHandler.removeCallbacks(seekbarUpdater);

				boolean autoPlay = audioPlayer.getState() == State.STARTED;

				if(autoPlay)
					savePodcastPosition();

				try {
					audioPlayer.setCurrentStreamAsSource();

					if (autoPlay) {
						audioPlayer.start();
					}
				} catch (AudioPlayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onInfoClicked() {
			SectionFragment fragment = (SectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			fragment.pushFragment(new InfoFragment());
		}
	}

	private class ListViewItemClickListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			Integer programId = null;

			if (position != 0) {
				programId = (int) parent.getItemIdAtPosition(position);
			}

			// position 2 == downloaded podcasts
			if (position == 1) {
				podcastGridAdapter.setList(DataSource.podcast().getAllLocalPodcasts());
				podcastGridAdapter.notifyDataSetChanged();
			}
			else {
				new PodcastAsyncTask(MainActivity.this, podcastGridAdapter)
						.execute(programId);
			}

			PodcastListFragment fragment = (PodcastListFragment) getFragment(SectionsPagerAdapter.PODCAST_FRAGMENT);
			GridView grid = fragment.getGridView();
			grid.smoothScrollToPosition(0);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	public static Podcast clickedPodcast;
	private class GridViewItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {

			clickedPodcast = (Podcast) view.getTag();

			Intent intent = new Intent(MainActivity.this, PodcastActivity.class);
			MainActivity.this.startActivity(intent);

//			if(true) return;
			// save position
//			savePodcastPosition();
//			Podcast podcast = (Podcast) view.getTag();
//			podcast.download();
//
//			if(true) return;



		}
	}

	static public void setPodcastAudioSource(Podcast podcast) {
		// save position
		inst.savePodcastPosition();

		inst.audioPlayer.setCurrentPodcast(podcast);

		try {
			inst.audioPlayer.setCurrentPodcastAsSource();
		} catch (AudioPlayerException e) {
			e.printStackTrace();
		}

		inst.audioPlayer.start();
	}

	@Subscribe
	@SuppressWarnings("UnusedDeclaration")
	public void onAudioPlayerServiceReady(final AudioPlayerService service) {
		audioPlayer = service;

		NotificationService.init(this, service);

		audioPlayer.setStateListener(new AudioPlayerStateListener());
		audioPlayer.setListener(new AudioPlayerHandler(this));

		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		manager.listen(new PhoneStateHandler(audioPlayer),
				PhoneStateListener.LISTEN_CALL_STATE);

		readyComponents.put(Component.AUDIOPLAYER, true);
		prepareLiveRadioIfReady();
	}

	@Subscribe
	@SuppressWarnings("UnusedDeclaration")
	public void onStreamUpdaterServiceReady(final StreamUpdaterService service) {
		streamUpdater = service;

		streamUpdater.setStreamUpdateListener(new StreamUpdateListener());

		connectivityChangeReceiver = new ConnectivityChangeReceiver(
				new ConnectivityChangedHandler(this, streamUpdater));

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectivityChangeReceiver, filter);

		readyComponents.put(Component.STREAMUPDATER, true);
		prepareLiveRadioIfReady();
	}

	@Subscribe
	public void onArticleListFragmentReady(final ArticleListFragment fragment) {
		fragment.setArticleListAdapter(articleListAdapter);
		fragment.setSearchListener(new ArticleSearchHandler(this,
				articleListAdapter));
	}

	@Subscribe
	public void onInfoFragmentReady(final InfoFragment fragment) {
		fragment.setInfoClickListener(new InfoClickHandler(this));
	}

	@Subscribe
	public void onLiveRadioFragmentReady(final LiveRadioFragment fragment) {
		fragment.setOnLiveRadioClickListener(new LiveRadioClickListener());
		fragment.setSeekBarOnChangeListener(new SeekBarChangeListener(fragment
				.getTimeTextView()));

		if (audioPlayer != null) {
			setProgramNameText();

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

			switch (audioPlayer.getDataSourceType()) {
			case LIVE_RADIO:
				fragment.setLiveRadioMode();

				switch (audioPlayer.getState()) {
				case STARTED:
				case PAUSED:
					String liveText = getResources().getString(
							R.string.textView_liveradio_time_live);
					fragment.setTimeText(liveText);
					break;
				case STOPPED:
				case COMPLETED:
				case PREPARING:
				case UNINITIALIZED:
				default:
					break;
				}

				break;
			case PODCAST:
				fragment.setPodcastMode();
				fragment.setMaxOnSeekBar(audioPlayer.getDuration());

				if (audioPlayer.getState() == State.STARTED) {
					seekbarHandler.removeCallbacks(seekbarUpdater);
					seekbarHandler.postDelayed(seekbarUpdater, 0);
				}
				break;
			case NONE:
			default:
				break;
			}
		}
	}

	@Subscribe
	public void onLiveRadioSectionFragmentReady(
			final LiveRadioSectionFragment fragment) {

		readyComponents.put(Component.LIVERADIOSECTION, true);
		prepareLiveRadioIfReady();
	}

	@Subscribe
	public void onPodcastFragmentReady(final PodcastListFragment fragment) {
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

	private class SeekBarChangeListener implements OnSeekBarChangeListener {

		private int progress;
		private TextView timeTextView;

		public SeekBarChangeListener(final TextView timeTextView) {
			progress = 0;
			this.timeTextView = timeTextView;
		}

		@Override
		public void onProgressChanged(final SeekBar seekBar,
				final int progress, final boolean fromUser) {

			this.progress = progress;

			String timeString = fromMsToTime(progress);
			timeTextView.setText(timeString);
		}

		@Override
		public void onStartTrackingTouch(final SeekBar seekBar) {
			seekbarHandler.removeCallbacks(seekbarUpdater);
		}

		@Override
		public void onStopTrackingTouch(final SeekBar seekBar) {
			seekbarHandler.postDelayed(seekbarUpdater, SEEKBAR_UPDATE_INTERVAL);

			audioPlayer.seekTo(progress);
		}

		private String fromMsToTime(final int ms) {
			long hours = TimeUnit.MILLISECONDS.toHours(ms);
			String format = hours > 0 ? "HH:mm:ss" : "mm:ss";

			return DurationFormatUtils.formatDuration(ms, format);
		}
	}

	private class SeekbarUpdater implements Runnable {

		@Override
		public void run() {
			if (audioPlayer.getState() == State.STARTED) {
				LiveRadioSectionFragment sectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

				if (sectionFragment != null) {
					LiveRadioFragment fragment = sectionFragment
							.getLiveRadioFragment();

					fragment.setSeekBarProgress(audioPlayer.getProgress());
					seekbarHandler.postDelayed(seekbarUpdater,
							SEEKBAR_UPDATE_INTERVAL);
				}
			}
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

	public Fragment getFragment(int index) {
		String tag = getFragmentTag(viewPager.getId(), index);
		return getSupportFragmentManager().findFragmentByTag(tag);
	}

	public ViewPager getViewPager() {
		return viewPager;
	}

	private static String getFragmentTag(int viewPagerId, int index) {
		return "android:switcher:" + viewPagerId + ":" + index;
	}

	/**
	 * Exit the app, used mainly from notification area
	 *
	 * @param e
	 */
	@Subscribe
	@SuppressWarnings("UnusedDeclaration")
	public void exitApp(@Nullable ManualExitEvent e) {
		savePodcastPosition();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * Shut down all services, remove notification and unregister connectivity
	 * broadcast receiver
	 */
	private void exitCleanup() {
		if(seekbarHandler != null)
			seekbarHandler.removeCallbacks(seekbarUpdater);

		try {
			BusProvider.INSTANCE.get().unregister(this);
			unregisterReceiver(connectivityChangeReceiver); // TODO is this correct
															// if back button has
															// been pressed?
		}
		catch (IllegalArgumentException e) {
			Logger.i("MainActivity already unregistered");
		}
	}
    }
