package no.srib.app.client.service;

import java.io.IOException;

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

public class AudioPlayerService extends Service {

	private final IBinder binder;

	private MediaPlayer mediaPlayer;

	public AudioPlayerService() {
		binder = new AudioPlayerBinder();
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

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
		mediaPlayer.setOnCompletionListener(new MediaPlayerCompletedListener());
		mediaPlayer.setOnErrorListener(new MediaPlayerErrorListener());
		//mediaPlayer.prepareAsync();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}

			mediaPlayer.reset();
			mediaPlayer.release();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

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
		mediaPlayer.setOnCompletionListener(new MediaPlayerCompletedListener());
		mediaPlayer.setOnErrorListener(new MediaPlayerErrorListener());
		//mediaPlayer.prepareAsync();

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
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
		}
	}

	private class MediaPlayerCompletedListener implements OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer arg0) {
			stopSelf();
		}
	}

	private class MediaPlayerErrorListener implements OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			StringBuilder sb = new StringBuilder();
			sb.append("Error: ");

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

			Log.e("SriB::AudioPlayerService", sb.toString());

			mediaPlayer.reset();

			return false;
		}
	}
}
