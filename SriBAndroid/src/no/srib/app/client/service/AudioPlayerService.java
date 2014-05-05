package no.srib.app.client.service;

import no.srib.app.client.audioplayer.AudioPlayer;
import no.srib.app.client.audioplayer.AudioPlayerException;
import no.srib.app.client.audioplayer.StateHandler;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AudioPlayerService extends Service implements AudioPlayer {

	private final IBinder binder;

	private boolean streaming;
	private String dataSource;
	private StateHandler stateHandler;
	private MediaPlayer mediaPlayer;

	public AudioPlayerService() {
		binder = new AudioPlayerBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		streaming = false;
		dataSource = null;
		stateHandler = new StateHandler();
		mediaPlayer = new MediaPlayer();

		mediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener());
		mediaPlayer.setOnCompletionListener(new MediaPlayerCompletedListener());
		mediaPlayer.setOnErrorListener(new MediaPlayerErrorListener());
	}

	private void uninitializeMediaPlayer() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}

		mediaPlayer.reset();
		stateHandler.setState(State.UNINITIALIZED);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mediaPlayer != null) {
			uninitializeMediaPlayer();
			mediaPlayer.release();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void setDataSource(String dataSource) throws AudioPlayerException {
		this.dataSource = dataSource;

		State state = stateHandler.getState();

		if (state != State.UNINITIALIZED) {
			uninitializeMediaPlayer();
		}

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		streaming = true;

		try {
			mediaPlayer.setDataSource(dataSource);
			stateHandler.setState(State.STOPPED);
		} catch (Exception e) {
			throw new AudioPlayerException(e);
		}
	}

	@Override
	public void start() {
		switch (stateHandler.getState()) {
		case STOPPED:
			mediaPlayer.prepareAsync();
			stateHandler.setState(State.PREPARING);
			break;
		case PAUSED:
			mediaPlayer.start();
			stateHandler.setState(State.STARTED);
			break;
		default:
			break;
		}
	}

	@Override
	public void pause() {
		switch (stateHandler.getState()) {
		case STARTED:
			mediaPlayer.pause();
			stateHandler.setState(State.PAUSED);
			break;
		default:
			break;
		}
	}

	@Override
	public void stop() {
		switch (stateHandler.getState()) {
		case STARTED:
		case PAUSED:
			mediaPlayer.stop();
			stateHandler.setState(State.STOPPED);
			break;
		default:
			break;
		}
	}

	@Override
	public State getState() {
		return stateHandler.getState();
	}

	@Override
	public void setStateListener(StateListener stateListener) {
		stateHandler.setStateListener(stateListener);
	}

	public class AudioPlayerBinder extends Binder {
		public AudioPlayerService getService() {
			return AudioPlayerService.this;
		}
	}

	private class MediaPlayerPreparedListener implements OnPreparedListener {

		@Override
		public void onPrepared(MediaPlayer mediaPlayer) {
			mediaPlayer.start();
			stateHandler.setState(State.STARTED);
		}
	}

	private class MediaPlayerCompletedListener implements OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer arg0) {
			if (streaming && stateHandler.getState() != State.UNINITIALIZED) {
				try {
					setDataSource(dataSource);
					start();
				} catch (AudioPlayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			stateHandler.setState(State.COMPLETED);
		}
	}

	private class MediaPlayerErrorListener implements OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			StringBuilder sb = new StringBuilder();
			sb.append("AudioPlayerService Error: ");

			switch (what) {
			case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				sb.append("Unknown");
				break;
			case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
				sb.append("Server died");
				break;
			}

			sb.append(" - ");

			switch (extra) {
			case MediaPlayer.MEDIA_ERROR_IO:
				sb.append("IO");
				break;
			case MediaPlayer.MEDIA_ERROR_MALFORMED:
				sb.append("Malformed");
				break;
			case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
				sb.append("Unsupported");
				break;
			case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
				sb.append("Timed out");
				break;
			}

			Log.e("SriB", sb.toString());

			uninitializeMediaPlayer();

			return false;
		}
	}
}
