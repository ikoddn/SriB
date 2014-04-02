package no.srib.sribapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsArticle extends AbstractModel {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
    private static final long serialVersionUID = 1L;

    @JsonProperty("ID")
    private int id;

    @JsonProperty("featured_image")
    private String featuredImage;

    @JsonProperty("short_URL")
    private String shortURL;

    private String content;
    private Calendar date;
    private String excerpt;
    private String title;

    public NewsArticle() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getDate() {
        return this.date;
    }

    public void setDate(String dateString) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        try {
            calendar.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
        }

        date = calendar;
    }

    public String getExcerpt() {
        return this.excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getFeaturedImage() {
        return this.featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getShortURL() {
        return this.shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
