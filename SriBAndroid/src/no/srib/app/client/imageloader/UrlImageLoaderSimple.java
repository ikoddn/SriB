package no.srib.app.client.imageloader;

import android.app.Activity;
import android.app.ActivityManager;
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
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

import no.srib.app.client.MainActivity;
import no.srib.app.client.db.DataSource;
import no.srib.app.client.db.PodcastDataSource;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.notification.NotificationHandler;
import no.srib.app.client.util.Hash;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.Logger;
import no.srib.app.client.util.UI;
import no.srib.app.client.util.ViewUtil;

/**
 * Notifications and widgets uses RemoteView so the Ion loader library wont
 * work for theses uses. Use this simple loader instead
 *
 * @author Jostein Eriksen
 */
public class UrlImageLoaderSimple {
	private static final int MAX_CACHE_SIZE = 20;
	private Context context;
	static final private String DIR_IMAGES = DataSource.podcast().getLocalDir() + "/images/";
	static public UrlImageLoaderSimple INSTANCE;
//	private Map<String, Semaphore> downloading = new HashMap<>();
	private Map<String, LoadImageTask> downloaderTasks = new HashMap<>();
	private PriorityQueue<ImageCacheMetaData> imageCacheMetas = new PriorityQueue<>(MAX_CACHE_SIZE);
	private HashMap<String, Bitmap> imageCaches = new HashMap<>(MAX_CACHE_SIZE);

	private UrlImageLoaderSimple() {

	}

	private void removeCache(String key) {
		if(imageCaches.containsKey(key))
			imageCaches.remove(key);
	}

	private void popCache() {
		String key;
		do {
			key = imageCacheMetas.remove().key;
		} while(imageCacheMetas.size() > 0 && !imageCaches.containsKey(key));
		removeCache(key);
	}

	private synchronized void addImageCache(String key, Bitmap imageCache) {
		// check heap size
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		Logger.d("available mem memClass: " + Integer.toString(memoryClass));
		if(imageCaches.size() >= MAX_CACHE_SIZE) {
			popCache();
		}
		imageCaches.put(key, imageCache);
		imageCacheMetas.add(new ImageCacheMetaData(key));
	}

	static public void init(Context context) {
		INSTANCE = new UrlImageLoaderSimple();
		INSTANCE.context = context;
		String localDir = DIR_IMAGES;
		File imagesDir = new File(localDir);
		if(!imagesDir.exists())
			imagesDir.mkdir();
	}

	public boolean hasInCache(String url) {
		// TODO: make this check in memory cache before checking filesystem
		String filename = Hash.md5(url);

		String localFile = DIR_IMAGES + filename;
		File file = new File(localFile);
		return file.exists();
	}

	public void onLowMemory() {
		popCache();
	}

	public synchronized void download(String url) {
		loadUrl(url, -1, -1, new ImageLoaderCallback() {
			@Override
			public void update(Bitmap bitmap) {
				// do nothing
			}
		});
	}

	/**
	 * Load image from an url. Images are cached
	 *
	 * @param url
	 * @param width
	 * @param height
	 * @param cb
	 */
	public synchronized void loadUrl(String url, int width, int height, ImageLoaderCallback cb) {
		// if image is cached use that, else set default to start with
		String filename = Hash.md5(url);
		String cacheKey = Hash.md5(String.format("%s%dx%d", filename, width, height));

		TempShit ts = new TempShit();
		ts.width = width;
		ts.height = height;
		ts.cacheKey = cacheKey;
		ts.cb = cb;

		String localDir = DIR_IMAGES;
		Logger.d("localDir: " + localDir);
		String localFilename = localDir + filename;

		ImageLoaderData loader = new ImageLoaderData();
		loader.url = url;
		loader.filename = filename;
		loader.localFile = localFilename;
		loader.width = width;
		loader.height = height;
		loader.ts = ts;

		// add the semaohore to the hashmap while we are synchronized
//		if(!downloading.containsKey(filename)) {
//			downloading.put(filename, new Semaphore(1));
//		}

		// if image is in cache: return it
		if(imageCaches.containsKey(cacheKey)) {
			cb.update(imageCaches.get(cacheKey));
			return;
		}

		// check if the image is downloading before going on to file checks
		// because this is a memory check and much faster than io operations
		// if image is already downloading... add callback to download process
		if(downloaderTasks.containsKey(filename)) {
			downloaderTasks.get(filename).addImageLoaderCallback(ts);
			return;
		}

		// if image is on file... load it
		File localFile = new File(localFilename);

		if(localFile.exists()) {
			loadLocalFile(loader);
			return;
		}

		// if all else fails, download the sucker
		downloaderTasks.put(cacheKey, new LoadImageTask());
		// run in multiple processes so not other async tasks in the app stops working
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			downloaderTasks.get(cacheKey).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loader);
		else
			downloaderTasks.get(cacheKey).execute(loader);
	}

	private void loadLocalFile(ImageLoaderData data) {
		if(data.height == -1 && data.width == -1) {
			Logger.d("only downloading image do not load it");
			return;
		}
		Logger.d("loading file from local file");
		// check if there is a local version of the current dimentions
		String currentDimensionsFileURI = String.format("%s-%dx%d", data.localFile, data.width, data.height);
		File currentDimensionsFile = new File(currentDimensionsFileURI);

		if(!currentDimensionsFile.exists()) {
			Logger.d("making image in " + data.width + "x" + data.height);
			Resources r = context.getResources();
			Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(data.localFile,
					ViewUtil.dipToPixels(r, data.width), ViewUtil.dipToPixels(r, data.height));

			FileOutputStream out = null;
			try {
//				currentDimensionsFile.createNewFile();
				out = new FileOutputStream(currentDimensionsFileURI);

				bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

				out.flush();
				out.close();
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
				Logger.d("exception happened does not call cb");
				return;
			}
		}

		// release the semaphore; your job is done
//		Logger.d("releasing semaphore");
//		downloading.get(data.filename).release();

		// decode the cached image and return
		Bitmap imageBitmap = BitmapFactory.decodeFile(currentDimensionsFileURI);

		// synchronize the updating of local cache
		synchronized (UrlImageLoaderSimple.INSTANCE) {
			addImageCache(data.ts.cacheKey, imageBitmap);
		}

		data.ts.cb.update(imageBitmap);
	}

	public String getLocalURL(String imageUrl) {
		String filename = Hash.md5(imageUrl);

		return DIR_IMAGES + filename;
	}

	/**
	 * TODO: should this also remove the cache???
	 *
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
		public int width;
		public int height;
//		public ImageLoaderCallback cb;
		public String filename;
		public TempShit ts;
	}

	private class LoadImageTask extends AsyncTask<ImageLoaderData, ImageLoaderData, Void> {
		private ArrayList<TempShit> onCompletedListeners = new ArrayList<>();

		private void addImageLoaderCallback(TempShit imageLoaderCallback) {
			onCompletedListeners.add(imageLoaderCallback);
		}

		/**
		 * Download and cache the images
		 */
		@Override
		protected Void doInBackground(@NotNull ImageLoaderData... urls) {
			// There is actually only one image
			for (final ImageLoaderData loader : urls) {
				addImageLoaderCallback(loader.ts);
				// wait your turn
//				try {
//					Logger.d("aquiring semaphore");
//					downloading.get(loader.filename).acquire();
//					Logger.d("semaphore aquired");
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					Logger.d("Semaphore interupted");
//					return null;
//				}


				File file = new File(loader.localFile);

				if (file.exists()) {
					Logger.d("file exists");
					UI.runOnUI(new Runnable() {
						@Override
						public void run() {
							loadLocalFile(loader);
						}
					});
					return null;
				} else {
					Logger.d("Downloading original image");


					// if our task has been cancelled then let's stop processing
					if (isCancelled()) return null;

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

						UI.runOnUI(new Runnable() {
							@Override
							public void run() {
								// TODO: test if synchronized blocks work as you think it does
								synchronized (UrlImageLoaderSimple.INSTANCE) {
									for (TempShit ts : onCompletedListeners) {
										if (imageCaches.containsKey(ts.cacheKey))
											ts.cb.update(imageCaches.get(ts.cacheKey));
										else {
											loader.width = ts.width;
											loader.height = ts.height;
											loadLocalFile(loader);
										}
									}
								}
							}
						});
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

//		@Override
//		public void onProgressUpdate(ImageLoaderData... values) {
//			super.onProgressUpdate(values);
//			for (ImageLoaderData loader : values) {
//				loadLocalFile(loader);
//			}
//		}
	}

	private class TempShit {
		private int width;
		private int height;
		private String cacheKey;
		private ImageLoaderCallback cb;
	}

	public interface ImageLoaderCallback {
		public void update(Bitmap bitmap);
	}

	private static class ImageCacheMetaData implements Comparable<ImageCacheMetaData> {
		private static int nextCacheId = 0;
		private int cacheId = nextCacheId++;
		private String key;

		public ImageCacheMetaData(String key) {
			this.key = key;
		}

		@Override
		public int compareTo(ImageCacheMetaData another) {
			return another.cacheId - cacheId;
		}
	}
}


