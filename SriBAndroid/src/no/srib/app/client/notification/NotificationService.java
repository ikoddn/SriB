package no.srib.app.client.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.koushikdutta.async.future.FutureCallback;
import com.squareup.otto.Subscribe;

import org.jetbrains.annotations.Nullable;

import no.srib.app.client.MainActivity;
import no.srib.app.client.R;
import no.srib.app.client.event.ManualExitEvent;
import no.srib.app.client.imageloader.UrlImageLoaderProvider;
import no.srib.app.client.service.audioplayer.AudioPlayerService;
import no.srib.app.client.service.audioplayer.state.State;
import no.srib.app.client.service.audioplayer.state.StateListener;
import no.srib.app.client.util.AudioMetaUtil;
import no.srib.app.client.util.BusProvider;

/**
 * @author Jostein Eriksen
 */
public class NotificationService implements NotificationHandler {
	private static final int NOTIFICATION_ID = 1;
	private static final int NOTIFICATION_DEFAULT_ART = R.drawable.notification_default_art;
	private static final int NOTIFICATION_ICON_SMALL = R.drawable.ic_notification;
	// store the default bitmap in a static variable to save memory, we only need one instance of this
//	private static Bitmap defaultNotificationBitmap;

	private final Context context;
	private static NotificationManager notificationManager;
	private final NotificationCompat.Builder notificationBuilder;
	private final RemoteViews contentView;
	private final AudioPlayerService audioService;
	private static NotificationService notification;

	public NotificationService(Context context, AudioPlayerService audioService) {
		this.context = context;
		this.audioService = audioService;
		notification = this;

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
				.setSmallIcon(NOTIFICATION_ICON_SMALL)
				.setOngoing(true)
				.setContentIntent(contentPendingIntent);

		setNotificationText("Pause", "Studentradioen i Bergen");

		notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		update();
	}

	public static NotificationService getNotificationService() {
		return notification;
	}

	private void setNotificationText(String title, String content) {
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			notificationBuilder.setContentText(content);
			notificationBuilder.setContentTitle(title);
		}
		else {
			contentView.setTextViewText(R.id.notification_title, title);
			contentView.setTextViewText(R.id.notification_text, content);
		}
	}

	@Override
	public void update() {
//		System.out.println("Update notification called");
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
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.ic_notification_play);
					break;
				case PREPARING:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.ic_notification_pause);
					break;
				case STARTED:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.ic_notification_pause);
					String programName = "";
					switch (audioService.getDataSourceType()) {
						case LIVE_RADIO:
							String liveText = context.getResources().getString(
									R.string.textView_liveradio_time_live);

							programName = AudioMetaUtil.getProgramName(audioService);
							setNotificationText(liveText, programName);
//							contentView.setTextViewText(R.id.notification_title, liveText);
//							contentView.setTextViewText(R.id.notification_text, programName);
							contentView.setImageViewResource(R.id.notification_image, NOTIFICATION_DEFAULT_ART);
							break;

						case PODCAST:
							programName = AudioMetaUtil.getProgramName(audioService);
							setNotificationText("Podcast", programName);
//							contentView.setTextViewText(R.id.notification_title, "Podcast");
//							contentView.setTextViewText(R.id.notification_text, programName);

							String imageUrl = audioService.getCurrentPodcast().getImageUrl();
							if(imageUrl != null && !imageUrl.equals("")) {
								contentView.setImageViewResource(R.id.notification_image, NOTIFICATION_DEFAULT_ART);

								UrlImageLoaderProvider.INSTANCE.get().loadFromUrlWithCallback(context, 85, 85, imageUrl, new FutureCallback<Bitmap>() {
									@Override
									public void onCompleted(Exception e, Bitmap bitmap) {
										contentView.setImageViewBitmap(R.id.notification_image, bitmap);
										NotificationService.this.update();
									}
								});
//								if(false) {
//									UrlImageLoaderSimple.INSTANCE.loadUrl(imageUrl, 85, 85, new UrlImageLoaderSimple.ImageLoaderCallback() {
//
//										@Override
//										public void update(Bitmap bitmap) {
//											contentView.setImageViewBitmap(R.id.notification_image, bitmap);
//											NotificationService.this.update();
//										}
//									});
//								}
							}
							else {
								contentView.setImageViewResource(R.id.notification_image, NOTIFICATION_DEFAULT_ART);
							}

							break;
						case NONE:
						default:
							break;
					}

					break;
				case STOPPED:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.ic_notification_play);
					break;
				case UNINITIALIZED:
					break;
				case COMPLETED:
					contentView.setImageViewResource(R.id.button_playpause, R.drawable.ic_notification_play);
					break;
			}
			update();
		}
	}

	public static void hide(Context context) {

		if(notificationManager == null)
			initManager(context);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	public static void init(Context context, AudioPlayerService service) {
		if(notification == null)
			new NotificationService(context, service);
	}

	/**
	 *
	 * @param context
	 */
	private static void initManager(Context context) {
		notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void show() {
		update();
	}

	@Subscribe
	public void destroy(@Nullable ManualExitEvent e) {
		BusProvider.INSTANCE.get().unregister(this);
		hide(context);
	}
}
