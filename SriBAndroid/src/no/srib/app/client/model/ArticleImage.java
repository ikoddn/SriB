package no.srib.app.client.model;

import java.io.Serializable;

public class ArticleImage implements Serializable {

	private static final long serialVersionUID = 1L;

	private int height;
	private int width;
	private String name;
	private String url;

	public ArticleImage() {
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ArticleImage [height=" + height + ", width=" + width
				+ ", name=" + name + ", url=" + url + "]";
	}
}
