package no.srib.app.client.imageloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import no.srib.app.client.notification.NotificationHandler;
import no.srib.app.client.util.Hash;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.ViewUtil;

/**
 * Notifications and widgets uses RemoteView so the Ion loader library wont
 * work for theses uses. Use this simple loader instead
 *
 * @author Jostein Eriksen
 */
public class UrlImageLoaderSimple {
	private Context context;
	static private String DIR_IMAGES = "images/";
	static public UrlImageLoaderSimple INSTANCE;

	static public void init(Context context) {
		INSTANCE = new UrlImageLoaderSimple();
		INSTANCE.context = context;
		String localDir = context.getFilesDir().getAbsolutePath() + "/" + DIR_IMAGES;
		File imagesDir = new File(localDir);
		if(!imagesDir.exists())
			imagesDir.mkdir();
	}

	public void loadBitmapUrl(String url, Runnable callback) {

	}

	/**
	 * Load image from an url. Images are cached
	 *
	 * @param notificationHandler
	 * @param imageViewId
	 * @param url
	 * @param defaultImageResource
	 */
	public void loadUrl(NotificationHandler notificationHandler, int imageViewId, String url, int defaultImageResource) {
		// if image is cached use that, else set default to start with
		String filename = Hash.md5(url);

		String localDir = context.getFilesDir().getAbsolutePath() + "/" + DIR_IMAGES;
		String localFile = localDir + filename;

		File file = new File(localFile);
		if(file.exists()) {
			// decode the cached image and return
			Bitmap image = BitmapFactory.decodeFile(localFile);

			ImageLoaderNotification loader = new ImageLoaderNotification();
			loader.notificationHandler = notificationHandler;
			loader.imageViewId = imageViewId;

			loader.bitmap = image;
			loader.update();
		}
		else {
			// set the default image to start with:
			notificationHandler.getContentView().setImageViewResource(imageViewId, defaultImageResource);
			notificationHandler.update();

			ImageLoaderNotification loader = new ImageLoaderNotification();
			loader.notificationHandler = notificationHandler;
			loader.imageViewId = imageViewId;
			loader.url = url;
			loader.localFile = localFile;

			new LoadImageTask().execute(loader);
		}
	}

	private static class ImageLoaderNotification {
		private NotificationHandler notificationHandler;
		private int imageViewId;
		private String url;
		private String localFile;
		private Bitmap bitmap;

		public void update() {
			notificationHandler.getContentView().setImageViewBitmap(imageViewId, bitmap);
			notificationHandler.update();
		}
	}

	private class LoadImageTask extends AsyncTask<ImageLoaderNotification, Void, Void> {

		/**
		 * Download and cache the images
		 */
		@Override
		protected Void doInBackground(@NotNull ImageLoaderNotification ... urls) {
			// There is actually only one image
			for (ImageLoaderNotification loader : urls) {

				// if our task has been cancelled then let's stop processing
				if(isCancelled()) return null;

				try {
					URL url = new URL(loader.url);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					connection.setDoInput(true);
					connection.connect();

					// saving bitmap to file
					InputStream input = connection.getInputStream();
					FileOutputStream out = new FileOutputStream(loader.localFile);
					int bufferSize = 1024;
					byte[] buffer = new byte[bufferSize];
					int len;
					while ((len = input.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}

					out.flush();
					out.close();

					final BitmapFactory.Options options = new BitmapFactory.Options();
					BitmapFactory.decodeFile(loader.localFile, options);

					Resources r = context.getResources();
					Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(loader.localFile,
							ViewUtil.dipToPixels(r, 85), ViewUtil.dipToPixels(r, 85));

					out = new FileOutputStream(loader.localFile);
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
					out.flush();
					out.close();

					loader.bitmap = bitmap;
					loader.update();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
}
