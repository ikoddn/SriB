package no.srib.app.client.imageloader;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

class UrlImageLoaderIon implements UrlImageLoader {

	@Override
	public void loadFromUrl(final ImageView imageView, final String url) {
		Ion.with(imageView).load(url);
	}

	@Override
	public void loadFromUrl(final ImageView imageView, final int height,
			final int width, final String url, final Drawable placeholder) {

		if (height > 0 && width > 0) {
			Ion.with(imageView).resize(width, height).placeholder(placeholder)
					.error(placeholder).load(url);
		} else {
			Log.d("SriB", "HERE");
		}
	}
}
