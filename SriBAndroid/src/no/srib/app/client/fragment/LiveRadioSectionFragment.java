package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class LiveRadioSectionFragment extends SectionFragment {

	private LiveRadioFragment liveRadioFragment;

	public LiveRadioSectionFragment() {
		liveRadioFragment = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_liveradiosection,
				container, false);

		TextView textView = (TextView) rootView
				.findViewById(R.id.textview_liveradiosection);
		textView.setText("LiveRadioSectionFragment");

		if (liveRadioFragment == null) {
			liveRadioFragment = LiveRadioFragment
					.newInstance(new InfoClickedListener());
		}

		return rootView;
	}

	@Override
	public int getFrameLayoutID() {
		return R.id.framelayout_liveradiosection;
	}

	@Override
	public Fragment getBaseFragment() {
		return liveRadioFragment;
	}

	private class InfoClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			pushFragment(new InfoFragment());
		}
	}
}
