package no.srib.app.client.fragment;

import no.srib.app.client.listener.OnFragmentReadyListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

	private OnFragmentReadyListener readyListener;

	public BaseFragment() {
		readyListener = null;
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);

		try {
			readyListener = (OnFragmentReadyListener) activity;
		} catch (ClassCastException e) {
			readyListener = null;
		}
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (readyListener != null) {
			readyListener.onFragmentReady(this);
		}
	}
}
