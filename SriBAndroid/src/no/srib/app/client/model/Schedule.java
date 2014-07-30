package no.srib.app.client.model;

import java.io.Serializable;

public class Schedule implements Serializable {

	private static final long serialVersionUID = 1L;

	private String program;
	private long time;

	protected Schedule() {
	}

	public Schedule(final String program, final long time) {
		this.program = program;
		this.time = time;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(final String program) {
		this.program = program;
	}

	public long getTime() {
		return time;
	}

	public void setTime(final long time) {
		this.time = time;
	}
}
