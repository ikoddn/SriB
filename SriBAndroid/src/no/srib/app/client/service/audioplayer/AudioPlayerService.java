package no.srib.app.client.service.audioplayer;

import java.io.IOException;

import no.srib.app.client.service.BaseService;
import no.srib.app.client.service.audioplayer.state.State;
import no.srib.app.client.service.audioplayer.state.StateHandler;
import no.srib.app.client.service.audioplayer.state.StateListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.util.Log;

/**
 * An audio player service based on Android's {@code MediaPlayer} class.
 * 
 * @author Sveinung
 * 
 */
public class AudioPlayerService extends BaseService {

	public enum DataSourceType {
		NONE,
		LIVE_RADIO,
		PODCAST
	}

	private DataSourceType dataSourceType;
	private String dataSource;
	private StateHandler stateHandler;
	private MediaPlayer mediaPlayer;

	public AudioPlayerService() {
		dataSourceType = DataSourceType.NONE;
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
		dataSource = null;
		dataSourceType = DataSourceType.NONE;
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

	/**
	 * Sets the data source URI for the audio player.
	 * 
	 * @param dataSource
	 *            - The URI
	 * @throws AudioPlayerException
	 */
	public void setDataSource(String dataSource, DataSourceType dataSourceType)
			throws AudioPlayerException {
		this.dataSource = dataSource;
		this.dataSourceType = dataSourceType;

		State state = stateHandler.getState();

		if (state != State.UNINITIALIZED) {
			uninitializeMediaPlayer();
		}

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		try {
			mediaPlayer.setDataSource(dataSource);
			stateHandler.setState(State.STOPPED);
		} catch (IllegalArgumentException e) {
			throw new AudioPlayerException(e);
		} catch (SecurityException e) {
			throw new AudioPlayerException(e);
		} catch (IllegalStateException e) {
			throw new AudioPlayerException(e);
		} catch (IOException e) {
			throw new AudioPlayerException(e);
		}
	}

	/**
	 * Starts the audio player.
	 */
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

	/**
	 * Pauses the audio player.
	 */
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

	/**
	 * Stops the audio player.
	 */
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

	/**
	 * Gets the current state of the audio player.
	 * 
	 * @return The current state
	 */
	public State getState() {
		return stateHandler.getState();
	}

	/**
	 * Sets the state listener for the audio player. When the audio player
	 * changes state, {@code onStateChanged} on the listener will be called.
	 * 
	 * @param stateListener
	 *            - The {@code StateListener} implementation
	 */
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

			if (beforeState != State.UNINITIALIZED) {
				try {
					setDataSource(dataSource, dataSourceType);
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
			default:
				break;
			}

			Log.e("SriB", sb.toString());

			uninitializeMediaPlayer();

			return false;
		}
	}

	/**
	 * Returns the current data source, or {@code null} if not set.
	 * 
	 * @return The current data source.
	 */
	public String getDataSource() {
		return dataSource;
	}

	public DataSourceType getDataSourceType() {
		return dataSourceType;
	}
}
