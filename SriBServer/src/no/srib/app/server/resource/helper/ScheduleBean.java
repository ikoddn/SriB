package no.srib.app.server.resource.helper;

import java.io.Serializable;
import java.sql.Time;

public class ScheduleBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private int day;
    private Time fromTime;
    private Time toTime;
    private String program;

    public ScheduleBean() {
    }

    public ScheduleBean(int id, int day, Time fromTime, Time toTime,
            String program) {
        super();
        this.id = id;
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.program = program;
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
