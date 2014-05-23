package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.Spinner;

public class PodcastFragment extends Fragment {

	private Spinner spinner = null;
	private GridView podcastGridView = null;
	private OnPodcastFragmentReadyListener readyListener = null;
	private OnItemClickListener podcastClickedListener = null;
	private OnItemSelectedListener spinnerSelectedListener = null;

	public PodcastFragment() {

	}

	public GridView getGridView() {

		return podcastGridView;
	}

	public void setPodCastClickedListener(
			OnItemClickListener podcastClickListener) {
		this.podcastClickedListener = podcastClickListener;
	}

	public void setSpinnerListSelectedListener(
			OnItemSelectedListener spinnerListClickListener) {
		this.spinnerSelectedListener = spinnerListClickListener;
	}

	public void setSpinnerListAdapter(
			no.srib.app.client.adapter.SpinnerAdapter spinnerListAdapter) {
		if (spinner != null) {
			spinner.setAdapter(spinnerListAdapter);
		}
	}

	public void setGridArrayAdapter(
			no.srib.app.client.adapter.GridArrayAdapter gridViewAdapter2) {
		if (podcastGridView != null) {
			podcastGridView.setAdapter(gridViewAdapter2);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			readyListener = (OnPodcastFragmentReadyListener) getActivity();
		} catch (ClassCastException e) {
			readyListener = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_podcast, container,
				false);
		setRetainInstance(true);

		spinner = (Spinner) rootView.findViewById(R.id.spinner1);
		podcastGridView = (GridView) rootView
				.findViewById(R.id.gridView_podcastList);

		if (readyListener != null) {
			readyListener.onPodcastFragmentReady();
		}

		spinner.setOnItemSelectedListener(spinnerSelectedListener);
		podcastGridView.setOnItemClickListener(podcastClickedListener);

		return rootView;

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			Activity a = getActivity();
			if (a != null)
				a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
	}

	public interface OnPodcastFragmentReadyListener {
		void onPodcastFragmentReady();
	}

}
