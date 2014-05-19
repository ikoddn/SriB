package no.srib.app.client.model;

import java.sql.Time;

public class Schedule {

	private int id;
	private int day;
	private Time fromTime;
	private Time toTime;
	private String program;

	public Schedule() {

	}

	
	
	public Schedule(int id, int day, Time fromTime, Time toTime, String program) {
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
