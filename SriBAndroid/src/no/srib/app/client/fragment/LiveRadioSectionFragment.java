package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LiveRadioSectionFragment extends Fragment {

	private LiveRadioFragment liveRadioFragment;
	
	public LiveRadioFragment getLiveRadioFragment() {
		return liveRadioFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_liveradiosection,
				container, false);

		TextView textView = (TextView) rootView
				.findViewById(R.id.textview_liveradiosection);
		textView.setText("LiveRadioSectionFragment");

		FragmentManager manager = getChildFragmentManager();

		if (manager.getBackStackEntryCount() == 0) {
			liveRadioFragment = new LiveRadioFragment();

			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.framelayout_liveradiosection, liveRadioFragment);
			transaction.commit();
		}

		return rootView;
	}

	public boolean backPressed() {
		FragmentManager manager = getChildFragmentManager();

		if (manager.getBackStackEntryCount() > 0) {
			manager.popBackStack();
			return true;
		}

		return false;
	}
}
