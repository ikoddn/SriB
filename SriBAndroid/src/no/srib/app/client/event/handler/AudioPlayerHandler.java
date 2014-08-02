package no.srib.app.client.event.handler;

import no.srib.app.client.MainActivity;
import no.srib.app.client.adapter.SectionsPagerAdapter;
import no.srib.app.client.event.listener.AudioPlayerListener;
import no.srib.app.client.fragment.LiveRadioFragment;
import no.srib.app.client.fragment.LiveRadioSectionFragment;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.model.StreamSchedule;

public class AudioPlayerHandler implements AudioPlayerListener {

	private MainActivity activity;

	public AudioPlayerHandler(final MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onSwitchToPodcast(final Podcast podcast) {
		LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) activity
				.getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

		liveRadioSectionFragment.replaceLoadingFragment();

		LiveRadioFragment fragment = liveRadioSectionFragment
				.getLiveRadioFragment();
		fragment.setPodcastMode();

		activity.getViewPager().setCurrentItem(
				SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);
	}

	@Override
	public void onSwitchToStreamSchedule(final StreamSchedule streamSchedule) {
		LiveRadioSectionFragment liveRadioSectionFragment = (LiveRadioSectionFragment) activity
				.getFragment(SectionsPagerAdapter.LIVERADIO_SECTION_FRAGMENT);

		liveRadioSectionFragment.replaceLoadingFragment();

		LiveRadioFragment liveRadio = liveRadioSectionFragment
				.getLiveRadioFragment();

		liveRadio.setLiveRadioMode();
		if (liveRadio.isReady()) {
			liveRadio.setStreamText(streamSchedule.getName());
		}
	}
}
