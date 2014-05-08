package no.srib.app.client.model;

public class ProgramName {

	private int defnr;
	private String name;
	
	public ProgramName() {
		// TODO Auto-generated constructor stub
	}
	
	public ProgramName(int defnr, String name) {
		super();
		this.defnr = defnr;
		this.name = name;
	}

	public int getDefnr() {
		return defnr;
	}

	public void setDefnr(int defnr) {
		this.defnr = defnr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
	
	

}
