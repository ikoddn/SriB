package no.srib.app.client.model;

import java.io.Serializable;
import java.util.List;

public class NewsArticleMedia implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String altText;
	private List<NewsArticleImage> sizes;

	public NewsArticleMedia() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAltText() {
		return altText;
	}

	public void setAlt_text(String altText) {
		this.altText = altText;
	}

	public List<NewsArticleImage> getSizes() {
		return sizes;
	}

	public void setSizes(List<NewsArticleImage> sizes) {
		this.sizes = sizes;
	}
}
