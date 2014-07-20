package no.srib.app.client.imageloader;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public interface UrlImageLoader {

	void loadFromUrl(ImageView imageView, String url);

	void loadFromUrl(ImageView imageView, int height, int width, String url,
			Drawable placeholder);
}
