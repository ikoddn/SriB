package no.srib.app.client.fragment;

import no.srib.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_liveradio,
				container, false);
		TextView textView = (TextView) rootView
				.findViewById(R.id.label_liveradio);
		textView.setText("Live radio fragment");
		return rootView;
	}
}
