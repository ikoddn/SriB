package no.srib.app.client.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import no.srib.app.client.MainActivity;
import no.srib.app.client.R;
import no.srib.app.client.imageloader.UrlImageLoader;
import no.srib.app.client.imageloader.UrlImageLoaderProvider;
import no.srib.app.client.imageloader.UrlImageLoaderSimple;
import no.srib.app.client.service.audioplayer.AudioPlayerService;
import no.srib.app.client.service.audioplayer.state.State;
import no.srib.app.client.service.audioplayer.state.StateListener;
import no.srib.app.client.util.AudioMetaUtil;
import no.srib.app.client.util.BitmapUtil;
import no.srib.app.client.util.BusProvider;
import no.srib.app.client.view.ImageViewWithCallback;

/**
 * @author Jostein Eriksen
 */
public class NotificationService implements NotificationHandler {
	private final int NOTIFICATION_ID = 1;

	private final Context context;
	private final NotificationManager notificationManager;
	private final NotificationCompat.Builder notificationBuilder;
	private final RemoteViews contentView;
	private final AudioPlayerService audioService;


	public NotificationService(Context context, AudioPlayerService audioService) {
		this.context = context;
		this.audioService = audioService;
		audioService.setStateListener(new AudioPlayerStateListener());

		contentView = new RemoteViews(context.getPackageName(), R.layout.notification_small);

		Intent playPauseIntent = new Intent("no.srib.app.client.PLAY_PAUSE");
		PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(context, 0, playPauseIntent, 0);

		Intent contentIntent = new Intent(context, MainActivity.class);
		PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent closeIntent = new Intent("no.srib.app.client.EXIT");
		PendingIntent closePendingIntent = PendingIntent.getBroadcast(context, 0, closeIntent, 0);

		contentView.setOnClickPendingIntent(R.id.button_close, closePendingIntent);
		contentView.setOnClickPendingIntent(R.id.button_playpause, playPausePendingIntent);


		notificationBuilder = new NotificationCompat.Builder(context)
				.setContent(contentView)
				.setSmallIcon(R.drawable.app_icon)
				.setOngoing(true)
				.setContentIntent(contentPendingIntent)
				;

		notificationManager =
				(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
	}

	@Override
	public void update() {
		notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
	}

	@Override
	public RemoteViews getContentView() {
		return contentView;
	}

	public class AudioPlayerStateListener implements StateListener {

		@Override
		public void onStateChanged(State state) {
			switch (state) {
				case PAUSED:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.liveradio_play_notification);
					break;
				case PREPARING:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.liveradio_pause_notification);
					break;
				case STARTED:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.liveradio_pause_notification);
					String programName = "";
					switch (audioService.getDataSourceType()) {
						case LIVE_RADIO:
							String liveText = context.getResources().getString(
									R.string.textView_liveradio_time_live);

							programName = AudioMetaUtil.getProgramName(audioService);
							contentView.setTextViewText(R.id.notification_title, liveText);
							contentView.setTextViewText(R.id.notification_text, programName);
							contentView.setImageViewResource(R.id.notification_image, R.drawable.podcast_default_art);
							break;

						case PODCAST:
							programName = AudioMetaUtil.getProgramName(audioService);
							contentView.setTextViewText(R.id.notification_title, "Podcast");
							contentView.setTextViewText(R.id.notification_text, programName);

							String imageUrl = audioService.getCurrentPodcast().getImageUrl();
							if(imageUrl != null && !imageUrl.equals("")) {
								UrlImageLoaderSimple.INSTANCE.loadUrl(NotificationService.this, R.id.notification_image, imageUrl, R.drawable.podcast_default_art);
							}
							else {
								contentView.setImageViewResource(R.id.notification_image, R.drawable.podcast_default_art);
							}

							break;
						case NONE:
						default:
							break;
					}

					break;
				case STOPPED:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.liveradio_play_notification);
					break;
				case UNINITIALIZED:
					break;
				case COMPLETED:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.liveradio_play_notification);
					break;
			}
			update();
		}
	}

	public void hide() {
		notificationManager.cancel(NOTIFICATION_ID);
	}

	public void show() {
		update();
	}

	public void destroy() {
		BusProvider.INSTANCE.get().unregister(this);
		hide();
	}
}
