package no.srib.app.client.fragment;

import no.srib.R;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.audioplayer.AudioPlayer;
import no.srib.app.client.audioplayer.AudioPlayerException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	private AudioPlayer audioPlayer;
	private TextView label;

	public void setAudioPlayer(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		audioPlayer.setStateListener(new AudioPlayerStateListener());
	}

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

		Button dataSourceButton = (Button) rootView
				.findViewById(R.id.button_liveradio_datasource);
		dataSourceButton.setOnClickListener(new DataSourceButtonListener());

		HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
		httpAsyncTask.execute("http://10.0.2.2:8080/SriBServer/rest/radiourl");
		return rootView;
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

	private class DataSourceButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				audioPlayer.setDataSource("http://radio.srib.no:8888/srib.mp3");
			} catch (AudioPlayerException e) {
				// TODO Auto-generated catch block
				Log.e("SriB", e.getMessage());
			}
		}
	}

	private class AudioPlayerStateListener implements AudioPlayer.StateListener {

		@Override
		public void onStateChanged(AudioPlayer.State state) {
			switch (state) {
			case PAUSED:
				label.setText("paused");
				break;
			case PREPARING:
				label.setText("preparing");
				break;
			case STARTED:
				label.setText("started");
				break;
			case STOPPED:
				label.setText("stopped");
				break;
			case UNINITIALIZED:
				label.setText("uninitialized");
				break;
			default:
				break;
			}
		}
	}
}
