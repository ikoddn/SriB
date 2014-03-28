package no.srib.sribapp.model;


import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the programinfo database table.
 * 
 */
@Entity
@Table(name="programinfo")
@NamedQuery(name="Programinfo.findAll", query="SELECT p FROM Programinfo p")
public class Programinfo extends AbstractModel  {
	private static final long serialVersionUID = 1L;
	private int program;
	private String category;
	private String description;
	private String imglink;
	private String subtitle;
	private String summary;
	private String title;
	private List<Podcast> podcasts;

	public Programinfo() {
	}


	@Id
	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	public int getProgram() {
		return this.program;
	}

	public void setProgram(int program) {
		this.program = program;
	}


	@Column(insertable=false, updatable=false, length=60)
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	@Lob
	@Column(insertable=false, updatable=false)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(insertable=false, updatable=false, length=50)
	public String getImglink() {
		return this.imglink;
	}

	public void setImglink(String imglink) {
		this.imglink = imglink;
	}


	@Lob
	@Column(insertable=false, updatable=false)
	public String getSubtitle() {
		return this.subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}


	@Lob
	@Column(insertable=false, updatable=false)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}


	@Column(insertable=false, updatable=false, length=50)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	//bi-directional many-to-one association to Podcast
	@OneToMany(mappedBy="programinfo")
	public List<Podcast> getPodcasts() {
		return this.podcasts;
	}

	public void setPodcasts(List<Podcast> podcasts) {
		this.podcasts = podcasts;
	}

	public Podcast addPodcast(Podcast podcast) {
		getPodcasts().add(podcast);
		podcast.setPrograminfo(this);

		return podcast;
	}

	public Podcast removePodcast(Podcast podcast) {
		getPodcasts().remove(podcast);
		podcast.setPrograminfo(null);

		return podcast;
	}

}