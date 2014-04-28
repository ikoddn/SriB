package no.srib.app.client.fragment;

import java.io.IOException;

import no.srib.R;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
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
	private MediaPlayer mediaPlayer;

	public LiveRadioFragment() {
		paused = false;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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

		try {
			mediaPlayer.setDataSource("http://radio.srib.no:8888/srib.mp3");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener());

		return rootView;
	}

	private class PlayButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {
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
			TextView label = (TextView) getActivity().findViewById(
					R.id.label_liveradio);

			if (!paused && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				label.setText("pause");
				paused = true;
			}
		}
	}

	private class MediaPlayerPreparedListener implements OnPreparedListener {

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();
		}
	}
}
