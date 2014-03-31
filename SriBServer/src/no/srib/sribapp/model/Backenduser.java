package no.srib.sribapp.model;

import javax.persistence.*;


/**
 * The persistent class for the backenduser database table.
 * 
 */
@Entity
@Table(name="backenduser")
@NamedQuery(name="Backenduser.findAll", query="SELECT b FROM Backenduser b")
public class Backenduser extends AbstractModel  {
	private static final long serialVersionUID = 1L;
	private int id;
	private String password;
	private String username;

	public Backenduser() {
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


	@Column(length=128)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Column(nullable=false, length=50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}