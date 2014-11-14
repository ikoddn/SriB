package no.srib.app.client.model;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import no.srib.app.client.R;
import no.srib.app.client.db.DataSource;
import no.srib.app.client.db.PodcastDataSource;
import no.srib.app.client.fragment.PodcastActivity;
import no.srib.app.client.util.Logger;
import no.srib.app.client.view.PodcastView;

public class Podcast implements Serializable {
	public static final long serialVersionUID = 1L;
	private transient static final File localDir = DataSource.podcast().getLocalDir();
	private transient static final String nasUrl = DataSource.podcast().getNasUrl();

	private int refnr;
	private int createdate;
	private int createtime;
	private int duration;
	private String filename;
	private String program;
	private int programId;
	private String remark;
	private String title;
	private String imageUrl;

	// members used for downloaded podcasts
	private boolean isLocal;
//	private transient File localFile;
	private long downloadedBytes;
	private long totalBytes;

//	private transient Map<View, PodcastActivity> progressBars = new HashMap<>();
	private transient AsyncTask downloaderTask;
	private transient PodcastActivity progressBar;
	private boolean downloading = false;
	private transient Runnable postDownloadCallback;
	private boolean queued = false;

	public Podcast() {
		// TODO Auto-generated constructor stub
		File file = new File(localDir, refnr + ".mp3");

		if(file.exists()) {
			Logger.i("file exists settting initial size: " + file.length());
			downloadedBytes = file.length();
		}
	}

	private void readObject(java.io.ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		init();
//		progressBars = new HashMap<>();
	}

	public int getPercentDownloaded() {
		if(totalBytes == 0)
			return 0;

		return (int)(((float)downloadedBytes / (float)totalBytes) * 100);
	}


	@JsonCreator
	public Podcast(@JsonProperty("refnr") int refnr,
				   @JsonProperty("createdate") int createdate,
				   @JsonProperty("createtime") int createtime,
				   @JsonProperty("duration") int duration,
				   @JsonProperty("filename") String filename,
				   @JsonProperty("program") String program,
				   @JsonProperty("programId") int programId,
				   @JsonProperty("remark") String remark,
				   @JsonProperty("title") String title,
				   @JsonProperty("imageUrl") String imageUrl) {
		super();
		this.refnr = refnr;
		this.createdate = createdate;
		this.createtime = createtime;
		this.duration = duration;
		this.filename = filename;
		this.program = program;
		this.programId = programId;
		this.remark = remark;
		this.title = title;
		this.imageUrl = imageUrl;
		this.totalBytes = DataSource.podcast().getFileSize(this);
		this.isLocal = this.totalBytes != -1;
		init();
	}

	public void init() {
		File file = new File(localDir, refnr + ".mp3");

		if(file.exists()) {
			Logger.i("file exists settting initial size: " + file.length());
			downloadedBytes = file.length();
		}

		this.downloading = false;
		queued = false;
	}

	public boolean isCompletelyDownloaded() { return downloadedBytes - totalBytes <= 0; }

	public boolean isLocal() { return isLocal; }

	public void download() {
		downloaderTask = new DownloadPodcastTask();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			downloaderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
		else
			downloaderTask.execute(this);
	}

	public void pauseDownload() {
		downloaderTask.cancel(true);
	}

	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public int getRefnr() {
		return refnr;
	}


	public void setRefnr(int refnr) {
		this.refnr = refnr;
	}


	public int getCreatedate() {
		return createdate;
	}


	public void setCreatedate(int createdate) {
		this.createdate = createdate;
	}


	public int getCreatetime() {
		return createtime;
	}


	public void setCreatetime(int createtime) {
		this.createtime = createtime;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}


	public String getProgram() {
		return program;
	}


	public void setProgram(String program) {
		this.program = program;
	}


	public int getProgramId() {
		return programId;
	}


	public void setProgramId(int programId) {
		this.programId = programId;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getProgram() + " " + getTitle();
	}

	public void registerProgressBar(PodcastActivity podcastView) {
		progressBar = podcastView;
//		progressBars.put(podcastView, podcastView);
	}

	public void unregisterProgressBar() {
		progressBar = null;
	}

	public void deletePodcast() {
		if(downloading) {
			postDownloadCallback = new Runnable() {
				@Override
				public void run() {
					deletePodcast();
				}
			};
			pauseDownload();
			return;
		}

		File file = new File(localDir, getRefnr() + ".mp3");

		boolean fileDeleted = false;
		if(file.exists())
			fileDeleted = file.delete();

		if(fileDeleted)
			downloadedBytes = 0;

		Logger.d("file was deleted: " + (fileDeleted? "true": "false"));

		DataSource.podcast().delete(this);

		if(progressBar != null)
			progressBar.updateProgressBar(0, this);
	}

	public boolean isDownloading() {
		return downloading;
	}

	public boolean isQueued() {
		return queued;
	}

	private class DownloadPodcastTask extends AsyncTask<Object, Void, Void> {

		/**
		 * Download and cache the images
		 */
		@Override
		protected Void doInBackground(@NotNull Object... podcasts) {
			// There is actually only one image
			for (Object t : podcasts) {

				Podcast podcast = (Podcast) t;

				podcast.queued = true;
				float percent = (((float)downloadedBytes / (float)totalBytes) * 100);

				if(progressBar != null)
					progressBar.updateProgressBar(percent, podcast);

				try {
					URL url = new URL(nasUrl +
							podcast.getFilename());

					// saving podcast to file
					if(!localDir.exists()) {
						Logger.i("dir does not exists");
						if(!localDir.mkdirs())
							Logger.i("Failed making dirs");
					}

					File file = new File(localDir, podcast.getRefnr() + ".mp3");

					HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					if(file.exists())
						downloadedBytes = file.length();

					Logger.i("already downloed is: " + downloadedBytes);
					connection.setRequestProperty("Range", "bytes=" + downloadedBytes + "-");

					connection.connect();
					int fileSize = connection.getContentLength();
					totalBytes = fileSize;

					Logger.i("Podcast length is: " + fileSize);
					podcast.isLocal = true;
					DataSource.podcast().addPodcast(podcast, fileSize + downloadedBytes);

					InputStream input = connection.getInputStream();
					FileOutputStream out = new FileOutputStream(file, file.exists());
					int bufferSize = 1024;
					byte[] buffer = new byte[bufferSize];
					int len;
					int readSize = 0;
					float prevPercent = -1;
					podcast.downloading = true;
					podcast.queued = false;
					while ((len = input.read(buffer)) != -1) {
						readSize += len;
//						Logger.i("read: " + readSize + "/" + fileSize);
						downloadedBytes += len;
						percent = (((float)downloadedBytes / (float)totalBytes) * 100);
						if((int)percent != (int)prevPercent) {
							prevPercent = percent;
							if(progressBar != null)
								progressBar.updateProgressBar(percent, podcast);
//							for (Map.Entry<View, PodcastActivity> v : progressBars.entrySet()) {
//								((PodcastActivity) v.getValue()).updateProgressBar((int) percent);
//							}
						}
						// TODO: how do we update the progress bar from here?
						out.write(buffer, 0, len);
						// if our task has been cancelled then let's stop processing
						if(isCancelled()) {
							Logger.d("breaking out of download loop");
							break;
						}
					}
					podcast.downloading = false;
					progressBar.updateProgressBar(percent, podcast);

					out.flush();
					out.close();
				} catch (IOException e) {
					podcast.downloading = false;
					progressBar.updateProgressBar(percent, podcast);
					e.printStackTrace();
				}
			}

			if(postDownloadCallback != null) {
				postDownloadCallback.run();
				postDownloadCallback = null;
			}

			return null;
		}
	}
}
