package no.srib.app.client.fragment;

import no.srib.R;
import no.srib.app.client.MainActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	private boolean paused;

	public LiveRadioFragment() {
		paused = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_liveradio,
				container, false);
		TextView textView = (TextView) rootView
				.findViewById(R.id.label_liveradio);
		textView.setText("Live radio fragment");

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
			MediaPlayer mediaPlayer = ((MainActivity) getActivity())
					.getAudioPlayerService().getMediaPlayer();
			TextView label = (TextView) getActivity().findViewById(
					R.id.label_liveradio);

			if (paused) {
				mediaPlayer.start();
				paused = false;
				label.setText("start");
			} else if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				label.setText("stop");
			} else {
				mediaPlayer.prepareAsync();
				label.setText("prepareAsync");
			}
		}
	}

	private class PauseButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			MediaPlayer mediaPlayer = ((MainActivity) getActivity())
					.getAudioPlayerService().getMediaPlayer();
			TextView label = (TextView) getActivity().findViewById(
					R.id.label_liveradio);

			if (!paused && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				paused = true;
				label.setText("pause");
			}
		}
	}

}
