package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.adapter.PodcastGridAdapter;
import no.srib.app.client.adapter.ProgramSpinnerAdapter;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.Spinner;

public class PodcastListFragment extends BaseFragment {

	private GridView podcastGridView;
	private Spinner programSpinner;

	public PodcastListFragment() {
		podcastGridView = null;
		programSpinner = null;
	}

	public GridView getGridView() {
		return podcastGridView;
	}

	public void setPodcastClickedListener(final OnItemClickListener listener) {
		if (podcastGridView != null) {
			podcastGridView.setOnItemClickListener(listener);
		}
	}

	public void setSpinnerListSelectedListener (
			final OnItemSelectedListener listener) {

		if (programSpinner != null) {
			programSpinner.setOnItemSelectedListener(listener);
		}
	}

	public void setSpinnerListAdapter(final ProgramSpinnerAdapter adapter) {
		if (programSpinner != null) {
			programSpinner.setAdapter(adapter);
		}
	}

	public void setGridArrayAdapter(final PodcastGridAdapter adapter) {
		if (podcastGridView != null) {
			podcastGridView.setAdapter(adapter);
		}
	}

	@Override
	public View onBaseCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_podcast, container,
				false);
		setRetainInstance(true);

		programSpinner = (Spinner) rootView
				.findViewById(R.id.spinner_podcast_program);
		podcastGridView = (GridView) rootView
				.findViewById(R.id.gridView_podcast_list);

		return rootView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {
			Activity activity = getActivity();

			if (activity != null) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
			}
		}
	}
}
