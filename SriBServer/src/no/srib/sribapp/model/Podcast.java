package no.srib.sribapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

/**
 * The persistent class for the podcast database table.
 * 
 */
@Entity
@Table(name = "PODCAST")
@NamedQueries({
        @NamedQuery(name = "melk", query = "SELECT P FROM Podcast P WHERE P.program=:id AND P.softdel=0 ORDER BY P.createdate DESC", hints = {
                @javax.persistence.QueryHint(name = "eclipselink.query-results-cache", value = "true"),
                @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.size", value = "500") }),
        @NamedQuery(name = "Podcast.findAll", query = "SELECT p FROM Podcast p WHERE p.softdel=0 AND p.program <> 0 ORDER BY p.createdate DESC,p.createtime DESC") })
@XmlType(name = "")
// Remove "@type" from the marshalled JSON
public class Podcast extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private int refnr;
    private int bitrate;
    private int createdate;
    private int createtime;
    private int duration;
    private String filename;
    private int program;
    private String remark;
    private int samplerate;
    private String title;
    private int softdel;

    public Podcast() {
    }

    @Id
    @Column(insertable = false, unique = true, nullable = false)
    public int getRefnr() {
        return this.refnr;
    }

    public void setRefnr(int refnr) {
        this.refnr = refnr;
    }

    @Column(insertable = false, updatable = false)
    public int getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    @Column(insertable = false, updatable = false)
    public int getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(int createdate) {
        this.createdate = createdate;
    }

    @Column(insertable = false, updatable = false)
    public int getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    @Column(insertable = false, updatable = false)
    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Column(insertable = false, updatable = false, length = 254)
    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Column(insertable = false, updatable = false)
    public int getProgram() {
        return this.program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    @Lob
    @Column(insertable = false, updatable = false)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(insertable = false, updatable = false)
    public int getSamplerate() {
        return this.samplerate;
    }

    public void setSamplerate(int samplerate) {
        this.samplerate = samplerate;
    }

    @Column(insertable = false, updatable = false, length = 80)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSoftdel() {
        return softdel;
    }

    public void setSoftdel(int softdel) {
        this.softdel = softdel;
    }

}
