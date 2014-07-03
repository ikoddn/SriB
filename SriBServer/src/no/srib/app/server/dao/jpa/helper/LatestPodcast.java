package no.srib.app.server.dao.jpa.helper;

public class LatestPodcast {

    private int refnr;
    private int program;
    private int createdate;

    LatestPodcast() {
    }

    public LatestPodcast(final int refnr, final int program,
            final int createdate) {
        this.refnr = refnr;
        this.program = program;
        this.createdate = createdate;
    }

    public int getProgram() {
        return program;
    }

    public void setProgram(final int program) {
        this.program = program;
    }

    public int getCreatedate() {
        return createdate;
    }

    public void setCreatedate(final int createdate) {
        this.createdate = createdate;
    }

    public int getRefnr() {
        return refnr;
    }

    public void setRefnr(final int refnr) {
        this.refnr = refnr;
    }

    @Override
    public String toString() {
        return "LatestPodcast [refnr=" + refnr + ", program=" + program
                + ", createdate=" + createdate + "]";
    }
}
