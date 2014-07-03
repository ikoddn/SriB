package no.srib.app.server.model.json;

import java.io.Serializable;
import java.sql.Time;

public class ScheduleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int day;
    private Time fromTime;
    private Time toTime;
    private String program;

    protected ScheduleBean() {
    }

    public ScheduleBean(final int id, final int day, final Time fromTime,
            final Time toTime) {

        this.id = id;
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;

        program = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Time getFromTime() {
        return fromTime;
    }

    public void setFromTime(Time fromTime) {
        this.fromTime = fromTime;
    }

    public Time getToTime() {
        return toTime;
    }

    public void setToTime(Time toTime) {
        this.toTime = toTime;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}
