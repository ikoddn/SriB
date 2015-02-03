package no.srib.app.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.io.Serializable;

import no.srib.app.client.imageloader.UrlImageDownloader;
import no.srib.app.client.util.Logger;

public class Podcast implements Serializable {
	public static final long serialVersionUID = 1L;

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

	public Podcast() {
		// TODO Auto-generated constructor stub
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

		// images for downloaded podcasts should be available even when there is no net
		initImage();
	}

	private void readObject(java.io.ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		initImage();
	}

	private void initImage() {
		Logger.d("The image url is: " + imageUrl);
		if(UrlImageDownloader.INSTANCE.hasLocalCache(imageUrl)) {
			this.imageUrl = UrlImageDownloader.INSTANCE.getLocalURL(imageUrl);
			Logger.d("Local image for: " + program + " found: reassigning imageurl to: " + this.imageUrl);
		}
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
}
