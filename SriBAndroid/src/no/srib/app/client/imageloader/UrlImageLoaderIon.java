package no.srib.app.client.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import no.srib.app.client.util.ImageUtil;

class UrlImageLoaderIon implements UrlImageLoader {

	@Override
	public void loadFromUrl(final ImageView imageView, final String url) {
		Ion.with(imageView).load(url);
	}

	@Override
	public void loadFromUrl(final ImageView imageView, final int height,
			final int width, final String url, final Drawable placeholder) {

		if (height > 0 && width > 0) {
			Ion.with(imageView)
					.resize(width, height)
					.placeholder(placeholder)
					.error(placeholder)
					.load(url);
		} else {
			Log.e("SriB", "Image with 0 width or height");
		}
	}

	@Override
	public void loadFromUrlWithCallback(Context context, final int width, final int height, String url, final FutureCallback<Bitmap> callback) {
		Ion.with(context)
				.load(url)
				.asByteArray()
				.setCallback(new FutureCallback<byte[]>() {
					@Override
					public void onCompleted(Exception e, byte[] bytes) {
						Bitmap bitmap = ImageUtil.decodeSampledBitmapFromByteArray(bytes, width, height);

						callback.onCompleted(e, bitmap);
					}
				});
	}
}
