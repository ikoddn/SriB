package no.srib.sribapp.model;

import javax.persistence.*;
import java.sql.Time;


/**
 * The persistent class for the streamurlschedule database table.
 * 
 */
@Entity
@Table(name="streamurlschedule")
@NamedQuery(name="Streamurlschedule.findAll", query="SELECT s FROM Streamurlschedule s")
public class Streamurlschedule extends AbstractModel  {
	private static final long serialVersionUID = 1L;
	private int id;
	private byte day;
	private Time fromtime;
	private Time totime;

	public Streamurlschedule() {
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


	@Column(nullable=false)
	public byte getDay() {
		return this.day;
	}

	public void setDay(byte day) {
		this.day = day;
	}


	@Column(nullable=false)
	public Time getFromtime() {
		return this.fromtime;
	}

	public void setFromtime(Time fromtime) {
		this.fromtime = fromtime;
	}


	@Column(nullable=false)
	public Time getTotime() {
		return this.totime;
	}

	public void setTotime(Time totime) {
		this.totime = totime;
	}

}