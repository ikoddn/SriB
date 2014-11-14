package no.srib.app.client.adapter;

import no.srib.app.client.R;
import no.srib.app.client.fragment.ArticleSectionFragment;
import no.srib.app.client.fragment.LiveRadioSectionFragment;
import no.srib.app.client.fragment.PodcastListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

	public final static int ARTICLE_SECTION_FRAGMENT = 0;
	public final static int LIVERADIO_SECTION_FRAGMENT = 1;
	public final static int PODCAST_FRAGMENT = 2;

	public final static int NUMBER_OF_MAIN_FRAGMENTS = 3;

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		switch (position) {
		case LIVERADIO_SECTION_FRAGMENT:
			return new LiveRadioSectionFragment();
		case PODCAST_FRAGMENT:
			return new PodcastListFragment();
		case ARTICLE_SECTION_FRAGMENT:
			return new ArticleSectionFragment();
		default:
			return null;
		}
	}

	@Override
	public int getIconResId(int index) {
		return R.drawable.ic_launcher;
	}

	@Override
	public int getCount() {
		return NUMBER_OF_MAIN_FRAGMENTS;
	}
}
