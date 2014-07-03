package no.srib.app.server.model.json;

public class PodcastBean {

    private int refnr;
    private int createdate;
    private int createtime;
    private int duration;
    private int programId;
    private String filename;
    private String remark;
    private String title;
    private String program;
    private String imageUrl;

    protected PodcastBean() {
    }

    public PodcastBean(final int refnr, final int createdate,
            final int createtime, final int duration, final int programId,
            final String filename, final String remark, final String title) {

        this.refnr = refnr;
        this.createdate = createdate;
        this.createtime = createtime;
        this.duration = duration;
        this.programId = programId;
        this.filename = filename;
        this.remark = remark;
        this.title = title;

        program = null;
        imageUrl = null;
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

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int i) {
        this.programId = i;
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
}
