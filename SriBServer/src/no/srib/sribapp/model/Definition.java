package no.srib.sribapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the definition database table.
 * 
 */
@Entity
@Table(name="definition")
@NamedQuery(name="Definition.findAll", query="SELECT d FROM Definition d")
public class Definition extends AbstractModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int defnr;
	private String name;
	private List<Podcast> podcasts;

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


	//bi-directional many-to-one association to Podcast
	@OneToMany(mappedBy="definition")
	public List<Podcast> getPodcasts() {
		return this.podcasts;
	}

	public void setPodcasts(List<Podcast> podcasts) {
		this.podcasts = podcasts;
	}

	public Podcast addPodcast(Podcast podcast) {
		getPodcasts().add(podcast);
		podcast.setDefinition(this);

		return podcast;
	}

	public Podcast removePodcast(Podcast podcast) {
		getPodcasts().remove(podcast);
		podcast.setDefinition(null);

		return podcast;
	}

}