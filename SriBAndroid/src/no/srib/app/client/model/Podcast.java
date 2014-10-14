package no.srib.app.client.model;

import android.os.AsyncTask;

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

import no.srib.app.client.R;
import no.srib.app.client.db.DataSource;
import no.srib.app.client.db.PodcastDataSource;
import no.srib.app.client.util.Logger;

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
	private File localFile;
	/**
	 * This member holds the total file size reported by the podcast nas server
	 * Not the downloaded file size
	 */
	private long fullFileSize;
	
	
	public Podcast() {
		// TODO Auto-generated constructor stub
	}


	public Podcast(int refnr, int createdate, int createtime, int duration,
			String filename, String program, int programId, String remark,
			String title, String imageUrl) {
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
		this.fullFileSize = DataSource.podcast().getFileSize(this);
		this.isLocal = this.fullFileSize != -1;
	}

	public void download() {
		new DownloadPodcastTask().execute(this);
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

	private static class DownloadPodcastTask extends AsyncTask<Podcast, Void, Void> {

		/**
		 * Download and cache the images
		 */
		@Override
		protected Void doInBackground(@NotNull Podcast ... podcasts) {
			// There is actually only one image
			for (Podcast podcast : podcasts) {

				// if our task has been cancelled then let's stop processing
				if(isCancelled()) return null;

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

					long downloaded = 0;
					if(file.exists())
						downloaded = file.length();

					Logger.i("already downloed is: " + downloaded);
					connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

					connection.connect();
					int fileSize = connection.getContentLength();
					Logger.i("Podcast length is: " + fileSize);
					podcast.isLocal = true;
					podcast.fullFileSize = fileSize;
					DataSource.podcast().addPodcast(podcast, fileSize + downloaded);

					InputStream input = connection.getInputStream();
					FileOutputStream out = new FileOutputStream(file, file.exists());
					int bufferSize = 1024;
					byte[] buffer = new byte[bufferSize];
					int len;
					int readSize = 0;
					while ((len = input.read(buffer)) != -1) {
						readSize += len;
						Logger.i("read: " + readSize + "/" + fileSize);
						out.write(buffer, 0, len);
					}

					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
}
