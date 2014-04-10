package no.srib.adapter;

import no.srib.fragment.ArticleFragment;
import no.srib.fragment.LiveRadioFragment;

import no.srib.fragment.PodcastListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new ArticleFragment();
			
		case 1:
			return new LiveRadioFragment();
			
	
		case 2:
			return new PodcastListFragment();
		}
		
		
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
