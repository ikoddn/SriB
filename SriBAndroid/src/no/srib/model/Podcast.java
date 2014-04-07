package no.srib.model;

public class Podcast {

	private String title;
	private String filename;
	private int createdate;
	private int createtime;
	private String remark;
	private int duration;
	
	public Podcast() {
		
	
	}

	public Podcast(String title, String filename, int createdate,
			int createtime, String remark, int duration) {
		super();
		this.title = title;
		this.filename = filename;
		this.createdate = createdate;
		this.createtime = createtime;
		this.remark = remark;
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
