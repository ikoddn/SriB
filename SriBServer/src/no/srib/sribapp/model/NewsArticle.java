package no.srib.sribapp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsArticle extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private int id;
    private String date;
    private String title;
    private String permalink;

    @JsonProperty("excerpt_display")
    private String excerptDisplay;

    @JsonProperty("content_display")
    private String contentDisplay;

    private List<NewsArticleMedia> media;

    public NewsArticle() {
    }

    public List<NewsArticleMedia> getMedia() {
        return media;
    }

    public void setMedia(List<NewsArticleMedia> media) {
        this.media = media;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getExcerptDisplay() {
        return excerptDisplay;
    }

    public void setExcerptDisplay(String excerptDisplay) {
        this.excerptDisplay = excerptDisplay;
    }

    public String getContentDisplay() {
        return contentDisplay;
    }

    public void setContentDisplay(String contentDisplay) {
        this.contentDisplay = contentDisplay;
    }
}
