package no.srib.app.client.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.inputmethod.InputMethodManager;

public class PageChangeListener implements OnPageChangeListener {

	private InputMethodManager inputManager;
	private ViewPager viewPager;

	public PageChangeListener(final Context context, final ViewPager viewPager) {

		this.viewPager = viewPager;
		inputManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/*
	 * Hiding the soft keyboard from fragments other than the article section.
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(final int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			if (viewPager.getCurrentItem() != SectionsPagerAdapter.ARTICLE_SECTION_FRAGMENT) {
				inputManager.hideSoftInputFromWindow(
						viewPager.getWindowToken(), 0);
			}
		}
	}

	@Override
	public void onPageScrolled(final int position, final float positionOffset,
			final int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(final int position) {
	}
}
