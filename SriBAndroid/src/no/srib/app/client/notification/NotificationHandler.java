package no.srib.app.client.notification;

import android.widget.RemoteViews;

/**
 * Created by morits on 10/09/14.
 */
public interface NotificationHandler {
	public void update();
	public RemoteViews getContentView();
}
