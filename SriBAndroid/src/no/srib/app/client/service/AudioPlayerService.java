package no.srib.app.client.service;

import no.srib.app.client.audioplayer.AudioPlayer;
import no.srib.app.client.audioplayer.AudioPlayerException;
import no.srib.app.client.audioplayer.StateHandler;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.util.Log;

public class AudioPlayerService extends BaseService implements AudioPlayer {

	private boolean streaming;
	private String dataSource;
	private StateHandler stateHandler;
	private MediaPlayer mediaPlayer;

	public AudioPlayerService() {
		streaming = false;
		dataSource = null;
		stateHandler = new StateHandler();
		mediaPlayer = new MediaPlayer();

	}

	@Override
	public void onCreate() {
		super.onCreate();

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
		case COMPLETED:
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

	public int getDuration() {
		if (mediaPlayer != null) {
			return mediaPlayer.getDuration();
		} else {
			return 0;
		}
	}

	public int getProgress() {
		if (mediaPlayer != null) {
			return mediaPlayer.getCurrentPosition();
		} else {
			return 0;
		}
	}

	public void seekTo(int msFromStart) {
		if (mediaPlayer != null) {
			mediaPlayer.seekTo(msFromStart);
		}
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
			State beforeState = stateHandler.getState();
			stateHandler.setState(State.COMPLETED);

			if (streaming && beforeState != State.UNINITIALIZED) {
				try {
					setDataSource(dataSource);
				} catch (AudioPlayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
			default:
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
