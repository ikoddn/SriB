package no.srib.sribapp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsArticleMedia extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private int id;

    @JsonProperty("alt_text")
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
