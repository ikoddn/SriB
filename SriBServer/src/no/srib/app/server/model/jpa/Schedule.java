package no.srib.app.server.model.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;

import no.srib.app.server.model.AbstractModel;

import java.sql.Time;

/**
 * The persistent class for the schedule database table.
 * 
 */
@Entity
@Table(name = "app_schedule")
@NamedQuery(name = "Schedule.findAll", query = "SELECT s FROM Schedule s")
@XmlType(name = "")
// Remove "@type" from the marshalled JSON
public class Schedule extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private int id;
    private byte day;
    private Time fromtime;
    private Time totime;
    private int program;

    protected Schedule() {
    }

    public Schedule(final byte day, final Time fromtime, final Time totime,
            final int program) {
        this.day = day;
        this.fromtime = fromtime;
        this.totime = totime;
        this.program = program;
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
    public int getProgram() {
        return this.program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    @Column(nullable = false)
    public Time getTotime() {
        return this.totime;
    }

    public void setTotime(Time totime) {
        this.totime = totime;
    }

}
