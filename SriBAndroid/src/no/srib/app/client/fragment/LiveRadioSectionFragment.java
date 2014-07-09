package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LiveRadioSectionFragment extends SectionFragment {

	private LiveRadioFragment liveRadioFragment;
	private LoadingFragment loadingFragment;
	private boolean loading;

	public LiveRadioSectionFragment() {
		liveRadioFragment = null;
		loadingFragment = null;
		loading = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_liveradiosection,
				container, false);

		setRetainInstance(true);

		if (liveRadioFragment == null) {
			liveRadioFragment = new LiveRadioFragment();
		}

		if (loadingFragment == null) {
			loadingFragment = new LoadingFragment();
		}

		return rootView;
	}

	@Override
	public int getFrameLayoutID() {
		return R.id.framelayout_liveradiosection;
	}

	@Override
	public Fragment getBaseFragment() {
		if (loading) {
			return loadingFragment;
		} else {
			return liveRadioFragment;
		}
	}

	public LoadingFragment getLoadingFragment() {
		return loadingFragment;
	}

	public LiveRadioFragment getLiveRadioFragment() {
		return liveRadioFragment;
	}

	public void replaceLoadingFragment() {
		if (loading) {
			loading = false;
			replaceFragment(liveRadioFragment);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {
			Activity a = getActivity();
			if (a != null) {
				a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
	}
}
