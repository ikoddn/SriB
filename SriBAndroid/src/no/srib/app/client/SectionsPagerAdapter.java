package no.srib.app.client;

import no.srib.app.client.fragment.ArticlesFragment;
import no.srib.app.client.fragment.LiveRadioFragment;
import no.srib.app.client.fragment.PodcastFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public final static int LIVERADIO_FRAGMENT = 0;
	public final static int PODCAST_FRAGMENT = 1;
	public final static int ARTICLES_FRAGMENT = 2;

	public final static int NUMBER_OF_FRAGMENTS = 3;
	
	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		switch (position) {
		case LIVERADIO_FRAGMENT:
			return new LiveRadioFragment();
		case PODCAST_FRAGMENT:
			return new PodcastFragment();
		case ARTICLES_FRAGMENT:
			return new ArticlesFragment();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return NUMBER_OF_FRAGMENTS;
	}
}
