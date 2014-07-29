package no.srib.app.client.event.handler;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import no.srib.app.client.R;
import no.srib.app.client.event.listener.OnInfoClickListener;

public class InfoClickHandler implements OnInfoClickListener {

	private final Context context;

	public InfoClickHandler(final Context context) {
		this.context = context;
	}

	@Override
	public void onFacebookClicked() {
		openURL(R.string.url_facebook);
	}

	@Override
	public void onSpotifyClicked() {
		try {
			// Try to open Spotify app
			openURL(R.string.scheme_uri_spotify);
		} catch (ActivityNotFoundException e) {
			// Spotify app not installed, fall back to profile web site
			openURL(R.string.url_spotify);
		}
	}

	@Override
	public void onSribWebsiteClicked() {
		openURL(R.string.url_sribwebsite);
	}
	
	private void openURL(final int resId) {
		String url = context.getResources().getString(resId);
		final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri
				.parse(url));
		context.startActivity(intent);
	}
}
