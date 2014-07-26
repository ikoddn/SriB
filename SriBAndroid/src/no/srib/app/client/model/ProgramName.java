package no.srib.app.client.model;

public class ProgramName {

	private int defnr;
	private String name;

	public ProgramName() {
	}

	public ProgramName(final int defnr, final String name) {
		super();
		this.defnr = defnr;
		this.name = name;
	}

	public int getDefnr() {
		return defnr;
	}

	public void setDefnr(final int defnr) {
		this.defnr = defnr;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
