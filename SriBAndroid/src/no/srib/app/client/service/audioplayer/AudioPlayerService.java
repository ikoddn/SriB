package no.srib.app.client.service.audioplayer;

import java.io.IOException;

import no.srib.app.client.R;
import no.srib.app.client.event.listener.AudioPlayerListener;
import no.srib.app.client.imageloader.UrlImageLoaderSimple;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.BaseService;
import no.srib.app.client.service.PodcastManager;
import no.srib.app.client.service.audioplayer.state.State;
import no.srib.app.client.service.audioplayer.state.StateHandler;
import no.srib.app.client.service.audioplayer.state.StateListener;
import no.srib.app.client.util.AudioMetaUtil;
import no.srib.app.client.util.DeviceUtil;
import no.srib.app.client.util.Logger;
import no.srib.app.client.view.PodcastView;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.RemoteControlClient;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

/**
 * An audio player service based on Android's {@code MediaPlayer} class.
 * 
 * @author Sveinung
 * 
 */
public class AudioPlayerService extends BaseService {

	private static AudioPlayerService playerService;

	private String podcastNasUrl;

	private DataSourceType dataSourceType;
	private String dataSource;
	private StateHandler stateHandler;
	private AudioPlayerListener audioPlayerListener;
	private MediaPlayer mediaPlayer;

	private Podcast currentPodcast;
	private StreamSchedule currentStream;
	private AudioManager audioManager;
	private ComponentName mediaButtonEventReceiver;
	private RemoteControlClient remoteControlClient;

	private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
				// Pause playback
				Logger.d("audio focus loss transient");
				pause();
			}
			else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				// Resume playback
				Logger.d("audio focus gain");
				start();
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				Logger.d("audio focus loss");
				audioManager.unregisterMediaButtonEventReceiver(mediaButtonEventReceiver);
				audioManager.abandonAudioFocus(afChangeListener);
				// Stop playback
				stop();
			}
		}
	};


	static public AudioPlayerService getService() {
		return playerService;
	}

	public AudioPlayerService() {
		dataSourceType = DataSourceType.NONE;
		dataSource = null;
		stateHandler = new StateHandler();
		mediaPlayer = new MediaPlayer();

		currentPodcast = null;
		currentStream = null;
		playerService = this;
	}

	public DataSourceType getDataSourceType() {
		return dataSourceType;
	}

	public void setListener(final AudioPlayerListener audioPlayerListener) {
		this.audioPlayerListener = audioPlayerListener;
	}

	public Podcast getCurrentPodcast() {
		return currentPodcast;
	}

	public void setCurrentPodcast(final Podcast podcast) {
		currentPodcast = podcast;
	}

	public StreamSchedule getCurrentStream() {
		return currentStream;
	}

	public void setCurrentStream(final StreamSchedule stream) {
		currentStream = stream;
	}

    @Override
	public void onCreate() {
		super.onCreate();

		podcastNasUrl = getResources().getString(R.string.url_podcast_nas);

		mediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener());
		mediaPlayer.setOnCompletionListener(new MediaPlayerCompletedListener());
		mediaPlayer.setOnErrorListener(new MediaPlayerErrorListener());

		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		mediaButtonEventReceiver = new ComponentName(getPackageName(), AudioPlayerBroadcastReceiver.class.getName());
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
	private void setDataSource(String dataSource, DataSourceType dataSourceType)
			throws AudioPlayerException {

		State state = stateHandler.getState();

		if (state != State.UNINITIALIZED) {
			uninitializeMediaPlayer();
		}

		this.dataSource = dataSource;
		this.dataSourceType = dataSourceType;

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		try {
			mediaPlayer.setDataSource(dataSource);
			stateHandler.setState(State.STOPPED);
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			throw new AudioPlayerException(e);
		}
	}

	/**
	 * Starts the audio player.
	 */
	public void start() {
		// Request audio focus for playback
		int result = audioManager.requestAudioFocus(afChangeListener,
				// Use the music stream.
				AudioManager.STREAM_MUSIC,
				// Request permanent focus.
				AudioManager.AUDIOFOCUS_GAIN);

		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			Logger.d("audio focus granted");

			// register stuff...
			audioManager.registerMediaButtonEventReceiver(mediaButtonEventReceiver);

			// only use lockscreen widgets for ice cream sandwich +
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				// build the PendingIntent for the remote control client
				Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
				mediaButtonIntent.setComponent(mediaButtonEventReceiver);

				// create and register the remote control client
				PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);
				remoteControlClient = new RemoteControlClient(mediaPendingIntent);
				remoteControlClient.setTransportControlFlags(
						RemoteControlClient.FLAG_KEY_MEDIA_PLAY
						| RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
				);
				audioManager.registerRemoteControlClient(remoteControlClient);

				RemoteControlClient.MetadataEditor editor = remoteControlClient.editMetadata(true);
				// TODO: fix this
				Bitmap defaultAlbumArt = BitmapFactory.decodeResource(getResources(), R.drawable.griditem_podcast_default_art);

				String title = AudioMetaUtil.getProgramName(this);

				editor.putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, defaultAlbumArt);
				// TODO: fix duration
//				editor.putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, getDuration());
//				editor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, "Artist");
				editor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title);
				editor.apply();

				if(getDataSourceType() == DataSourceType.PODCAST) {
					DeviceUtil.DisplayProperties display = DeviceUtil.getDisplayProperties(getApplicationContext());
					String imageUrl = getCurrentPodcast().getImageUrl();
					if(imageUrl != null && !imageUrl.equals("")) {
						UrlImageLoaderSimple.INSTANCE.loadUrl(imageUrl, display.width, display.height, new UrlImageLoaderSimple.ImageLoaderCallback() {

							@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
							@Override
							public void update(Bitmap bitmap) {
								RemoteControlClient.MetadataEditor editor = remoteControlClient.editMetadata(true);

								editor.putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, bitmap);
								editor.apply();
							}
						});
					}
				}

				remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
			}
			// Start playback.
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
		else {
			Logger.d("audio focus not granted");
		}
	}

	/**
	 * Pauses the audio player.
	 */
	public void pause() {
		audioManager.abandonAudioFocus(afChangeListener);
		// only use lockscreen widgets for ice cream sandwich +
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
		}
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
		audioManager.abandonAudioFocus(afChangeListener);
		// only use lockscreen widgets for ice cream sandwich +
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_STOPPED);
		}
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
		stateHandler.addStateListener(stateListener);
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
			stateHandler.setState(State.STARTED);
			mediaPlayer.start();
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

	public void setCurrentPodcastAsSource() throws AudioPlayerException {
		if (currentPodcast == null) {
			throw new AudioPlayerException("Current podcast not set");
		}

		PodcastManager.PodcastLocalInfo podcastMeta = PodcastManager.getInstance().getLocalInfo(currentPodcast);

		String url;
		if(podcastMeta.getDownloadedPercent() > 0)
			url = podcastMeta.getLocalFile().toString();
		else
			url = podcastNasUrl + currentPodcast.getFilename();

		Logger.d("the url for podcast is: " + url);


		if (!url.equals(dataSource)) {
			setDataSource(url, DataSourceType.PODCAST);
		}

		audioPlayerListener.onSwitchToPodcast(currentPodcast);
	}

	public void setCurrentStreamAsSource() throws AudioPlayerException {
		if (currentStream == null) {
			throw new AudioPlayerException("Current stream not set");
		}

		String url = currentStream.getUrl();

		if (!url.equals(dataSource)) {
			setDataSource(url, DataSourceType.LIVE_RADIO);
		}

		audioPlayerListener.onSwitchToStreamSchedule(currentStream);
	}

	public void removeStateHandlers() {
		stateHandler.removeListeners();
	}
}
