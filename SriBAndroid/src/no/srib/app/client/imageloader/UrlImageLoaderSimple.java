package no.srib.app.client.imageloader;

import android.content.Context;
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

	public void loadUrl(NotificationHandler notificationHandler, int imageViewId, String url, int defaultImageResource) {
		// if image is cached use that, else set default to start with
		String filename = Hash.md5(url);

		String localDir = context.getFilesDir().getAbsolutePath() + "/" + DIR_IMAGES;
		String localFile = localDir + filename;

		File file = new File(localFile);
		if(file.exists()) {
			Bitmap image = BitmapFactory.decodeFile(localFile);
			// set the cached image and return
			notificationHandler.getContentView().setImageViewBitmap(imageViewId, image);
			notificationHandler.update();
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
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					loader.bitmap = myBitmap;

					// saving bitmap to file
					FileOutputStream out = new FileOutputStream(loader.localFile);
					myBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
					out.flush();
					out.close();

					loader.update();
				} catch (IOException e) {
//					e.printStackTrace();
				}
			}
			return null;
		}
	}
}
