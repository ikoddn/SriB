package no.srib.app.client.fragment;

import no.srib.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	private boolean playing;
	
	public LiveRadioFragment() {
		playing = false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_liveradio,
				container, false);
		TextView textView = (TextView) rootView
				.findViewById(R.id.label_liveradio);
		textView.setText("Live radio fragment");
		
		Button playButton = (Button) rootView.findViewById(R.id.button_liveradio);
		playButton.setOnClickListener(new PlayButtonListener());
		return rootView;
	}
	
	private class PlayButtonListener implements OnClickListener {
		
		@Override
		public void onClick(View button) {
			playing = !playing;
			
			TextView label = (TextView) getActivity().findViewById(R.id.label_liveradio);
			
			if (playing) {
				label.setText("Playing");
			} else {
				label.setText("Paused");
			}
		}
	}
}
