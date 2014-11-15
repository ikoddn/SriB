package no.srib.app.client.service;

import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import no.srib.app.client.db.DataSource;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.util.Logger;

public class PodcastManager extends BaseService {
	private static final File localDir = DataSource.podcast().getLocalDir();
	private static final String nasUrl = DataSource.podcast().getNasUrl();

	List<PodcastLocalInfo> downloadQueue = new LinkedList<>();
	HashMap<Integer, PodcastLocalInfo> podcastLocalInfoCache = new HashMap<>();
	PodcastLocalInfo currentDownload;
	AtomicBoolean downloading = new AtomicBoolean();

	private static PodcastManager instance;

	public static synchronized PodcastManager getInstance() {
		if(instance == null)
			new PodcastManager();

		return instance;
	}

	public PodcastManager() {
		if(instance != null)
			throw new RuntimeException("PodcastManagerService has already been instantiated");

		instance = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void deletePodcast(final Podcast podcast) {
		PodcastLocalInfo localInfo = getLocalInfo(podcast);
		if(localInfo.isDownloading) {
			localInfo.postDownloadCallback = new Runnable() {
				@Override
				public void run() {
					deletePodcast(podcast);
				}
			};
			cancelDownload(podcast);
			return;
		}

		boolean fileDeleted = false;
		if(localInfo.localFile.exists())
			fileDeleted = localInfo.localFile.delete();

		if(fileDeleted)
			localInfo.setDownloadedBytes(0);

		Logger.d("file was deleted: " + (fileDeleted? "true": "false"));

		DataSource.podcast().delete(podcast);

		localInfo.dataChanged();
	}

	public void cancelDownload(Podcast podcast) {
		PodcastLocalInfo localInfo = getLocalInfo(podcast);

		synchronized (this) {
			if(downloadQueue.contains(localInfo)) {
				downloadQueue.remove(localInfo);
				localInfo.isQueued = false;
				localInfo.podcast = null;
				localInfo.dataChanged();
			}

			else if(currentDownload.equals(localInfo)) {
				currentDownload.downloadTask.cancel(true);
			}
		}
	}

	public void downloadPodcast(Podcast podcast) {
		PodcastLocalInfo localInfo = getLocalInfo(podcast);
		localInfo.podcast = podcast;
		localInfo.isQueued = true;

		synchronized (this) {
			if (!downloadQueue.contains(localInfo))
				downloadQueue.add(localInfo);

			if(!downloadNext())
				localInfo.dataChanged();
		}
	}

	public synchronized PodcastLocalInfo getLocalInfo(Podcast podcast) {
		if(!podcastLocalInfoCache.containsKey(podcast.getRefnr()))
			podcastLocalInfoCache.put(podcast.getRefnr(), new PodcastLocalInfo(podcast));

		return podcastLocalInfoCache.get(podcast.getRefnr());
	}

	private boolean downloadNext() {
		synchronized (this) {
			if (downloading.get())
				return false;

			if (downloadQueue.size() > 0) {
				downloading.set(true);
				currentDownload = ((LinkedList<PodcastLocalInfo>) downloadQueue).poll();
				(currentDownload.downloadTask = new DownloadPodcastTask(currentDownload)).execute();
			}
			return true;
		}
	}

	public static class PodcastLocalInfo {
		private int podcastId;
		private Podcast podcast;

		public File getLocalFile() {
			return localFile;
		}

		private File localFile;
		private long downloadedBytes;
		private long totalBytes;
		private boolean isDownloading;
		private boolean isQueued;

		public boolean isDownloading() {
			return isDownloading;
		}

		public boolean isQueued() {
			return isQueued;
		}

		public float getDownloadedPercent() {
			return downloadedPercent;
		}

		private float downloadedPercent;
		private Runnable postDownloadCallback;
		private DownloadPodcastTask downloadTask;
		// week reference; we do not want to retain view hierarchy because of a data listener
		private WeakReference<OnDataChangeListener> dataChangeListener;

		public PodcastLocalInfo(Podcast podcast) {
			podcastId = podcast.getRefnr();
			totalBytes = DataSource.podcast().getFileSize(podcast);

			// local file
			localFile = new File(localDir, podcast.getRefnr() + ".mp3");

			if(localFile.exists()) {
				Logger.i("file exists settting initial size: " + localFile.length());
				setDownloadedBytes(localFile.length());
			}
		}

		public void setDownloadedBytes(long bytes) {
			downloadedBytes = bytes;
			downloadedPercent = ((float)downloadedBytes / (float)totalBytes) * 100;
		}

		public void setDatachangeListener(OnDataChangeListener listener) {
			dataChangeListener = new WeakReference<>(listener);
		}

		public void dataChanged() {
			if(dataChangeListener != null && dataChangeListener.get() != null)
				dataChangeListener.get().dataChanged(this);
		}
	}

	private static class DownloadPodcastTask extends AsyncTask<Void, Void, Void> {
		private PodcastLocalInfo podcastLocalInfo;

		public DownloadPodcastTask(PodcastLocalInfo podcastLocalInfo) {
			this.podcastLocalInfo = podcastLocalInfo;
		}

		/**
		 * Download and cache the images
		 */
		@Override
		protected Void doInBackground(@NotNull Void... nullObj) {
			// There is actually only one image
//			for (Podcast podcastLocalInfo : podcasts) {

//				Podcast podcastLocalInfo = (Podcast) t;

//				podcastLocalInfo.queued = true;
//				float percent = (((float)downloadedBytes / (float)totalBytes) * 100);

//				if(progressBar != null)
//					progressBar.updateProgressBar(percent, podcastLocalInfo);

				try {
					podcastLocalInfo.isQueued = false;
					podcastLocalInfo.isDownloading = true;

					podcastLocalInfo.dataChanged();

					URL url = new URL(nasUrl +
							podcastLocalInfo.podcast.getFilename());

					// saving podcastLocalInfo to file
					if(!localDir.exists()) {
						Logger.i("dir does not exists");
						if(!localDir.mkdirs())
							Logger.i("Failed making dirs");
					}

					String podcastName = podcastLocalInfo.podcast.getProgram();
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					// refresh downloaded bytes in case this has changed
					if(podcastLocalInfo.localFile.exists())
						podcastLocalInfo.setDownloadedBytes(podcastLocalInfo.localFile.length());

					Logger.i("bytes already downloed is: " + podcastLocalInfo.downloadedBytes);
					connection.setRequestProperty("Range", "bytes=" + podcastLocalInfo.downloadedBytes + "-");

					connection.connect();
					int fileSize = connection.getContentLength();
					Logger.i("content length reported by server is: " + fileSize);
					podcastLocalInfo.totalBytes = podcastLocalInfo.downloadedBytes + fileSize;
					Logger.i("total file size is: " + podcastLocalInfo.totalBytes);

					// save podcast in the datasource
					DataSource.podcast().addPodcast(podcastLocalInfo.podcast, podcastLocalInfo.totalBytes);

					// release the podcastLocalInfo object to avoid mem leaks, because these objects
					// are instantiated over and over again by the podcast gridview adapter
					podcastLocalInfo.podcast = null;

					InputStream input = connection.getInputStream();
					FileOutputStream out = new FileOutputStream(podcastLocalInfo.localFile,
							podcastLocalInfo.localFile.exists());

					int len,
						bufferSize = 1024;
					byte[] buffer = new byte[bufferSize];
					float prevPercent = -1;

					while ((len = input.read(buffer)) != -1) {
//						Logger.i("read: " + readSize + "/" + fileSize);
						podcastLocalInfo.setDownloadedBytes(podcastLocalInfo.downloadedBytes += len);
//						percent = (((float)downloadedBytes / (float)totalBytes) * 100);
						if((int)podcastLocalInfo.downloadedPercent != (int)prevPercent) {
							Logger.d("downloading(" + podcastName + "): " + podcastLocalInfo.downloadedPercent);
							prevPercent = podcastLocalInfo.downloadedPercent;

							// update the UI
							podcastLocalInfo.dataChanged();
						}
						out.write(buffer, 0, len);

						// if our task has been cancelled then let's stop processing
						if(isCancelled()) {
							Logger.d("breaking out of download loop");
							break;
						}
					}

					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
//			}

			podcastLocalInfo.isDownloading = false;
			// update the UI if visible one last time
			podcastLocalInfo.dataChanged();

			// remove the podcast local info cache to release any potential references to views
			// by the data change listener
			instance.podcastLocalInfoCache.remove(podcastLocalInfo.podcastId);

			// post download callback
			// used if delete is called while downloading
			if(podcastLocalInfo.postDownloadCallback != null) {
				podcastLocalInfo.postDownloadCallback.run();
				podcastLocalInfo.postDownloadCallback = null;
			}

			// download next in queue
			synchronized (instance) {
				instance.currentDownload = null;
				instance.downloading.set(false);
				instance.downloadNext();
			}

			return null;
		}
	}

	public interface OnDataChangeListener {
		public void dataChanged(PodcastLocalInfo localInfo);
	}
}
