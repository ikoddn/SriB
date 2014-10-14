package no.srib.app.client.fragment;

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

import no.srib.app.client.R;
import no.srib.app.client.adapter.PodcastGridAdapter;
import no.srib.app.client.adapter.ProgramSpinnerAdapter;

public class LocalPodcastFragment extends BaseFragment {

	private GridView podcastGridView;

	public LocalPodcastFragment() {
		podcastGridView = null;
	}

	public GridView getGridView() {
		return podcastGridView;
	}

	public void setPodcastClickedListener(final OnItemClickListener listener) {
		if (podcastGridView != null) {
			podcastGridView.setOnItemClickListener(listener);
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
		View rootView = inflater.inflate(R.layout.fragment_local_podcast, container,
				false);
		setRetainInstance(true);

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
