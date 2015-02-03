package no.srib.app.client.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;

public interface UrlImageLoader {

	void loadFromUrl(ImageView imageView, String url);

	void loadFromUrl(ImageView imageView, int height, int width, String url,
			Drawable placeholder);

	void loadFromUrlWithCallback(Context context, int width, int height, String url, FutureCallback<Bitmap> callback);
}
