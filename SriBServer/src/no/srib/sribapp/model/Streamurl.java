package no.srib.sribapp.model;

import javax.persistence.*;


/**
 * The persistent class for the streamurl database table.
 * 
 */
@Entity
@Table(name="streamurl")
@NamedQuery(name="Streamurl.findAll", query="SELECT s FROM Streamurl s")
public class Streamurl extends no.srib.sribapp.model.AbstractModel  {
	private static final long serialVersionUID = 1L;
	private int id;
	private String url;

	public Streamurl() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column(nullable=false, length=150)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}