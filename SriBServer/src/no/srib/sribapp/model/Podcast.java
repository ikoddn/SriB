package no.srib.sribapp.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The persistent class for the podcast database table.
 * 
 */

@XmlRootElement(name = "podcast")
@XmlType(propOrder = {"refnr","bitrate","createdate","createtime","duration","filename","remark","samplerate","title","definition"})
@Entity
@Table(name="podcast")
@NamedQuery(name="Podcast.findAll", query="SELECT p FROM Podcast p")
public class Podcast extends AbstractModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int refnr;
	private int bitrate;
	private int createdate;
	private int createtime;
	private int duration;
	private String filename;
	private String remark;
	private int samplerate;
	private String title;
	private Definition definition;

	public Podcast() {
	}


	@Id
	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	public int getRefnr() {
		return this.refnr;
	}

	public void setRefnr(int refnr) {
		this.refnr = refnr;
	}


	@Column(insertable=false, updatable=false)
	public int getBitrate() {
		return this.bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}


	@Column(insertable=false, updatable=false)
	public int getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(int createdate) {
		this.createdate = createdate;
	}


	@Column(insertable=false, updatable=false)
	public int getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(int createtime) {
		this.createtime = createtime;
	}


	@Column(insertable=false, updatable=false)
	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}


	@Column(insertable=false, updatable=false, length=254)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}


	@Lob
	@Column(insertable=false, updatable=false)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Column(insertable=false, updatable=false)
	public int getSamplerate() {
		return this.samplerate;
	}

	public void setSamplerate(int samplerate) {
		this.samplerate = samplerate;
	}


	@Column(insertable=false, updatable=false, length=80)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	//bi-directional many-to-one association to Definition
	@ManyToOne
	@JoinColumn(name="PROGRAM")
	public Definition getDefinition() {
		return this.definition;
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

}