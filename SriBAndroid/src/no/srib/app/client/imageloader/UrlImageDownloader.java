package no.srib.app.client.imageloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import no.srib.app.client.db.DataSource;
import no.srib.app.client.util.Hash;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.Logger;
import no.srib.app.client.util.UI;
import no.srib.app.client.util.ViewUtil;

public class UrlImageDownloader {
	private Context context;
	static final private String DIR_IMAGES = DataSource.podcast().getLocalDir() + "/images/";
	static public UrlImageDownloader INSTANCE;
	private Map<String, LoadImageTask> downloaderTasks = new HashMap<>();

	private UrlImageDownloader() {

	}

	static public void init(Context context) {
		INSTANCE = new UrlImageDownloader();
		INSTANCE.context = context;
		String localDir = DIR_IMAGES;
		File imagesDir = new File(localDir);
		if(!imagesDir.exists())
			imagesDir.mkdir();
	}

	public boolean hasLocalCache(String url) {
		if(url == null)
			return false;

		// TODO: make this check in memory cache before checking filesystem
		String filename = Hash.md5(url);

		String localFile = DIR_IMAGES + filename;
		File file = new File(localFile);
		return file.exists();
	}

	public synchronized void download(String url) {
		// if image is already downloaded, or is downloading...
		String filename = Hash.md5(url);
		String localDir = DIR_IMAGES;
		Logger.d("localDir: " + localDir);
		String localFilename = localDir + filename;
		String cacheKey = Hash.md5(localFilename);

		// ... do nothing

		ImageLoaderData loader = new ImageLoaderData();
		loader.url = url;
		loader.filename = filename;
		loader.localFile = localFilename;

		// check if the image is downloading before going on to file checks
		// because this is a memory check and much faster than io operations
		// if image is already downloading... add callback to download process
		if(downloaderTasks.containsKey(filename))
			return;

		// if image is on file...
		File localFile = new File(localFilename);
		if(localFile.exists())
			return;

		// if all else fails, download the sucker
		downloaderTasks.put(cacheKey, new LoadImageTask());
		// run in multiple processes so not other async tasks in the app stops working
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			downloaderTasks.get(cacheKey).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loader);
		else
			downloaderTasks.get(cacheKey).execute(loader);
	}

	public String getLocalURL(String imageUrl) {
		String filename = Hash.md5(imageUrl);

		return DIR_IMAGES + filename;
	}

	/**
	 * @param imageUrl
	 */
	public static void deleteImage(String imageUrl) {
		// if image is cached use that, else set default to start with
		String filename = Hash.md5(imageUrl);

		String localDir = DIR_IMAGES;
		String localFilename = localDir + filename;

		File file = new File(localFilename);

		if(file.exists())
			file.delete();
	}

	private static class ImageLoaderData {
		private String url;
		private String localFile;
		public String filename;
	}

	private class LoadImageTask extends AsyncTask<ImageLoaderData, ImageLoaderData, Void> {
		/**
		 * Download and cache the images
		 */
		@Override
		protected Void doInBackground(@NotNull ImageLoaderData... urls) {
			// There is actually only one image
			for (final ImageLoaderData loader : urls) {
				File file = new File(loader.localFile);

				if (file.exists()) {
					Logger.d("file exists... did not download");
					return null;
				} else {
					Logger.d("Downloading original image");
					// if our task has been cancelled then let's stop processing
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
							// TODO: close and flush out stream
							if (isCancelled()) return null;
							out.write(buffer, 0, len);
						}

						out.flush();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
	}
}
