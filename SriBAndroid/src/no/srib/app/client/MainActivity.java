package no.srib.app.client;

import java.io.IOException;
import java.util.List;

import no.srib.app.client.adapter.ArticleListAdapter;
import no.srib.app.client.adapter.GridArrayAdapter;
import no.srib.app.client.adapter.SectionsPagerAdapter;
import no.srib.app.client.adapter.SpinnerAdapter;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.audioplayer.AudioPlayer;
import no.srib.app.client.audioplayer.AudioPlayer.State;
import no.srib.app.client.audioplayer.AudioPlayerException;
import no.srib.app.client.fragment.ArticleListFragment;
import no.srib.app.client.fragment.ArticleListFragment.OnArticlesFragmentReadyListener;
import no.srib.app.client.fragment.ArticleSectionFragment;
import no.srib.app.client.fragment.LiveRadioFragment;
import no.srib.app.client.fragment.LiveRadioFragment.OnLiveRadioClickListener;
import no.srib.app.client.fragment.LiveRadioFragment.OnLiveRadioFragmentReadyListener;
import no.srib.app.client.fragment.LiveRadioFragment.SeekBarInterface;
import no.srib.app.client.fragment.LiveRadioSectionFragment;
import no.srib.app.client.fragment.PodcastFragment;
import no.srib.app.client.fragment.PodcastFragment.OnPodcastFragmentReadyListener;
import no.srib.app.client.fragment.SectionFragment;
import no.srib.app.client.model.NewsArticle;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.model.ProgramName;
import no.srib.app.client.model.Schedule;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.AudioPlayerService;
import no.srib.app.client.service.BaseService;
import no.srib.app.client.service.ServiceHandler;
import no.srib.app.client.service.ServiceHandler.OnServiceReadyListener;
import no.srib.app.client.service.StreamUpdaterService;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainActivity extends FragmentActivity implements
		OnArticlesFragmentReadyListener, OnPodcastFragmentReadyListener,
		OnLiveRadioFragmentReadyListener {

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

	private ServiceHandler<AudioPlayerService> audioPlayerService;
	private ServiceHandler<StreamUpdaterService> streamUpdaterService;

	private GridArrayAdapter gridViewAdapter = null;
	private SpinnerAdapter spinnerListAdapter = null;

	Handler seekHandler = new Handler();
	Runnable run;

	public MainActivity() {
		MAPPER = new ObjectMapper();
		articleListAdapter = null;
		viewPager = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
				new ArticleHttpResponseListener());
		String url = getResources().getString(R.string.url_articles);
		httpAsyncTask.execute(url);

		// Podcast Part
		gridViewAdapter = new GridArrayAdapter(MainActivity.this);
		spinnerListAdapter = new SpinnerAdapter(MainActivity.this);

		HttpAsyncTask programTask = new HttpAsyncTask(new GetProgramNames());
		HttpAsyncTask podcastTask = new HttpAsyncTask(new GetAllPodcast());

		String programTaskUrl = getResources().getString(
				R.string.getAllProgramNames);
		String podcastTaskUrl = getResources()
				.getString(R.string.getAllPodcast);

		podcastTask.execute(podcastTaskUrl);
		programTask.execute(programTaskUrl);

		// ProgramName
		HttpAsyncTask programName = new HttpAsyncTask(
				new GetCurrentProgramName());

		String programNameURL = getResources().getString(
				R.string.currentProgram);

		programName.execute(programNameURL);

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

			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			LiveRadioFragment liveRadioFragment = null;

			if (liveRadioSectionFragment != null) {
				liveRadioFragment = (LiveRadioFragment) liveRadioSectionFragment
						.getBaseFragment();
			}

			if (liveRadioFragment != null) {
				liveRadioFragment
						.setOnLiveRadioClickListener(new LiveRadioClickListener());
			}

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

				AudioPlayerService audioPlayer = (AudioPlayerService) audioPlayerService
						.getService();
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
			LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
			LiveRadioFragment fragment = null;
			AudioPlayerService audioservice = (AudioPlayerService) audioPlayerService
					.getService();

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
			AudioPlayerService audioPlayer = (AudioPlayerService) audioPlayerService
					.getService();

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

				StreamUpdaterService streamUpdater = (StreamUpdaterService) streamUpdaterService
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
			AudioPlayerService audioPlayer = (AudioPlayerService) audioPlayerService
					.getService();
			audioPlayer.stop();
		}

		@Override
		public void onTwitterClicked() {
			String url = getResources().getString(R.string.url_twitter);
			final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri
					.parse(url));
			startActivity(intent);
		}

		@Override
		public void onInstagramClicked() {
			String url = getResources().getString(R.string.url_instagram);
			final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri
					.parse(url));
			startActivity(intent);
		}
	}

	private class ListViewItemClickListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			PodcastFragment fragment = (PodcastFragment) getFragment(SectionsPagerAdapter.PODCAST_FRAGMENT);

			if (position == 0) {

				HttpAsyncTask podcast = new HttpAsyncTask(new GetAllPodcast());
				String url = getResources().getString(R.string.getAllPodcast);
				podcast.execute(url);

			} else {
				HttpAsyncTask podcast = new HttpAsyncTask(new GetAllPodcast());
				String url = getResources().getString(R.string.getAllPodcast);
				podcast.execute(url + "/"
						+ parent.getItemIdAtPosition(position));

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
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {

			AudioPlayerService audioPlayer = (AudioPlayerService) audioPlayerService
					.getService();
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

					list.add(0, new ProgramName(0, "Velg program"));
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

	private class GetCurrentProgramName implements HttpResponseListener {

		@Override
		public void onResponse(String response) {
			Schedule schedule = null;

			if (response != null) {
				try {
					schedule = MAPPER.readValue(response, Schedule.class);
					LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
					LiveRadioFragment fragment = null;

					if (liveRadioSectionFragment != null) {
						fragment = (LiveRadioFragment) liveRadioSectionFragment
								.getBaseFragment();
					}

					if (fragment != null) {
						fragment.setProgramNameText(schedule.getProgram());
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
		ArticleSectionFragment fragment = (ArticleSectionFragment) getFragment(SectionsPagerAdapter.ARTICLE_SECTION_FRAGMENT);
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
	public void onLiveRadioFragmentReady() {
		LiveRadioSectionFragment fragment = (LiveRadioSectionFragment) getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
		LiveRadioFragment liveFrag = (LiveRadioFragment) fragment
				.getChildFragmentManager().getFragments().get(0);
		liveFrag.setSeekBarOnChangeListener(new SeekBarListener());
		liveFrag.setSeekBarListener(new SeekBarImpl());

	}

	private class SeekBarListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			AudioPlayerService audioservice = (AudioPlayerService) audioPlayerService
					.getService();
			State currentState = audioservice.getState();
			if (fromUser && currentState == State.STARTED
					|| currentState == State.PAUSED) {
				System.out.println(progress + " " + seekBar.getMax());

				audioservice.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

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
			AudioPlayerService audioservice = (AudioPlayerService) audioPlayerService
					.getService();

			int progress = audioservice.getProgress();
			int max = audioservice.getDuration();
			System.out.println(progress + "/" + max);
			liveFrag.setSeekBarProgress(progress);
			seekHandler.postDelayed(run, 1000);

		}

	}

}
