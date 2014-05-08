package no.srib.app.client.model;

public class Podcast {

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
