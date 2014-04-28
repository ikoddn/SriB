package no.srib.sribapp.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the streamurl database table.
 * 
 */
@Entity
@Table(name="streamurl")
@NamedQuery(name="Streamurl.findAll", query="SELECT s FROM Streamurl s")
@XmlType(name = "") // Remove "@type" from the marshalled JSON
public class Streamurl extends AbstractModel  {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String url;

	public Streamurl() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column(nullable=false, length=100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(nullable=false, length=150)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}