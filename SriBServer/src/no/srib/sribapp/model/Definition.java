package no.srib.sribapp.model;

import javax.persistence.*;


/**
 * The persistent class for the definition database table.
 * 
 */
@Entity
@Table(name="definition")
@NamedQuery(name="Definition.findAll", query="SELECT d FROM Definition d")
public class Definition extends AbstractModel  {
	private static final long serialVersionUID = 1L;
	private int defnr;
	private String name;

	public Definition() {
	}


	@Id
	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	public int getDefnr() {
		return this.defnr;
	}

	public void setDefnr(int defnr) {
		this.defnr = defnr;
	}


	@Column(insertable=false, updatable=false, length=254)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}