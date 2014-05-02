package no.srib.app.client.fragment;

import no.srib.R;
import no.srib.app.client.audioplayer.AudioPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	private static final String BUNDLE_STATUS = "status";
	private static final String BUNDLE_STREAM = "stream";

	private AudioPlayer audioPlayer;
	private TextView statusTextView;
	private TextView streamTextView;

	public void setAudioPlayer(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		audioPlayer.setStateListener(new AudioPlayerStateListener());
	}

	public void setStreamText(CharSequence text) {
		streamTextView.setText(text);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_liveradio,
				container, false);

		statusTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_status);
		streamTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_stream);

		if (savedInstanceState != null) {
			statusTextView.setText(savedInstanceState
					.getCharSequence(BUNDLE_STATUS));
			streamTextView.setText(savedInstanceState
					.getCharSequence(BUNDLE_STREAM));
		} else {
			statusTextView.setText("Live radio fragment");
			streamTextView.setText("PLACEHOLDER TEXT");
		}

		Button playButton = (Button) rootView
				.findViewById(R.id.button_liveradio_play);
		playButton.setOnClickListener(new PlayButtonListener());

		Button pauseButton = (Button) rootView
				.findViewById(R.id.button_liveradio_pause);
		pauseButton.setOnClickListener(new PauseButtonListener());

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putCharSequence(BUNDLE_STATUS, statusTextView.getText());
		outState.putCharSequence(BUNDLE_STREAM, streamTextView.getText());
	}

	private class PlayButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			if (audioPlayer.isPlaying()) {
				audioPlayer.stop();
			} else {
				audioPlayer.start();
			}
		}
	}

	private class PauseButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			audioPlayer.pause();
		}
	}

	private class AudioPlayerStateListener implements AudioPlayer.StateListener {

		@Override
		public void onStateChanged(AudioPlayer.State state) {
			switch (state) {
			case PAUSED:
				statusTextView.setText("paused");
				break;
			case PREPARING:
				statusTextView.setText("preparing");
				break;
			case STARTED:
				statusTextView.setText("started");
				break;
			case STOPPED:
				statusTextView.setText("stopped");
				break;
			case UNINITIALIZED:
				statusTextView.setText("uninitialized");
				break;
			default:
				break;
			}
		}
	}
}
