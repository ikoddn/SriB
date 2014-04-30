package no.srib.app.client.fragment;

import no.srib.R;
import no.srib.app.client.MainActivity;
import no.srib.app.client.service.AudioPlayerService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	private TextView label;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_liveradio,
				container, false);
		label = (TextView) rootView.findViewById(R.id.label_liveradio);
		label.setText("Live radio fragment");

		Button playButton = (Button) rootView
				.findViewById(R.id.button_liveradio_play);
		playButton.setOnClickListener(new PlayButtonListener());

		Button pauseButton = (Button) rootView
				.findViewById(R.id.button_liveradio_pause);
		pauseButton.setOnClickListener(new PauseButtonListener());

		return rootView;
	}

	private class PlayButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			AudioPlayerService audioPlayer = ((MainActivity) getActivity())
					.getAudioPlayerService();

			if (audioPlayer.getState() == AudioPlayerService.State.STARTED) {
				audioPlayer.stop();
				label.setText("stop");
			} else {
				audioPlayer.start();
				label.setText("start");
			}
		}
	}

	private class PauseButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			AudioPlayerService audioPlayer = ((MainActivity) getActivity())
					.getAudioPlayerService();

			audioPlayer.pause();

			if (audioPlayer.getState() == AudioPlayerService.State.PAUSED) {
				label.setText("pause");
			}
		}
	}

}
