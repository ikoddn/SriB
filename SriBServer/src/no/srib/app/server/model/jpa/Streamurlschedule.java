package no.srib.app.server.model.jpa;

import javax.persistence.*;

import no.srib.app.server.model.AbstractModel;

import java.sql.Time;

/**
 * The persistent class for the streamurlschedule database table.
 * 
 */
@Entity
@Table(name = "app_streamurlschedule")
@NamedQuery(name = "Streamurlschedule.findAll", query = "SELECT s FROM Streamurlschedule s")
public class Streamurlschedule extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private int id;
    private byte day;
    private Time fromtime;
    private Time totime;

    protected Streamurlschedule() {
    }

    public Streamurlschedule(final byte day, final Time fromtime,
            final Time totime) {
        this.day = day;
        this.fromtime = fromtime;
        this.totime = totime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    public byte getDay() {
        return this.day;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    @Column(nullable = false)
    public Time getFromtime() {
        return this.fromtime;
    }

    public void setFromtime(Time fromtime) {
        this.fromtime = fromtime;
    }

    @Column(nullable = false)
    public Time getTotime() {
        return this.totime;
    }

    public void setTotime(Time totime) {
        this.totime = totime;
    }

    @Override
    public String toString() {
        return "Streamurlschedule [id=" + id + ", day=" + day + ", fromtime="
                + fromtime + ", totime=" + totime + "]";
    }
}
