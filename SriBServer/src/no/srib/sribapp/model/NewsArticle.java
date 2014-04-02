package no.srib.sribapp.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties({ "author", "publicize_URLs", "tags", "categories",
        "attachments", "metadata", "meta" })
public class NewsArticle extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @JsonProperty("ID")
    private int id;

    @JsonProperty("URL")
    private String url;

    private Number comment_count;
    private boolean comments_open;
    private String content;
    private String date;
    private String excerpt;
    private String featured_image;
    private String format;
    private boolean geo;
    private String global_ID;
    private String guid;
    private boolean i_like;
    private boolean is_following;
    private boolean is_reblogged;
    private Number like_count;
    private String modified;
    private boolean parent;
    private String password;
    private boolean pings_open;
    private String short_URL;
    private Number site_ID;
    private String slug;
    private String status;
    private String title;
    private String type;

    public NewsArticle() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Number getComment_count() {
        return this.comment_count;
    }

    public void setComment_count(Number comment_count) {
        this.comment_count = comment_count;
    }

    public boolean getComments_open() {
        return this.comments_open;
    }

    public void setComments_open(boolean comments_open) {
        this.comments_open = comments_open;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExcerpt() {
        return this.excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getFeatured_image() {
        return this.featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean getGeo() {
        return this.geo;
    }

    public void setGeo(boolean geo) {
        this.geo = geo;
    }

    public String getGlobal_ID() {
        return this.global_ID;
    }

    public void setGlobal_ID(String global_ID) {
        this.global_ID = global_ID;
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean getI_like() {
        return this.i_like;
    }

    public void setI_like(boolean i_like) {
        this.i_like = i_like;
    }

    public boolean getIs_following() {
        return this.is_following;
    }

    public void setIs_following(boolean is_following) {
        this.is_following = is_following;
    }

    public boolean getIs_reblogged() {
        return this.is_reblogged;
    }

    public void setIs_reblogged(boolean is_reblogged) {
        this.is_reblogged = is_reblogged;
    }

    public Number getLike_count() {
        return this.like_count;
    }

    public void setLike_count(Number like_count) {
        this.like_count = like_count;
    }

    public String getModified() {
        return this.modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public boolean getParent() {
        return this.parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getPings_open() {
        return this.pings_open;
    }

    public void setPings_open(boolean pings_open) {
        this.pings_open = pings_open;
    }

    public String getShort_URL() {
        return this.short_URL;
    }

    public void setShort_URL(String short_URL) {
        this.short_URL = short_URL;
    }

    public Number getSite_ID() {
        return this.site_ID;
    }

    public void setSite_ID(Number site_ID) {
        this.site_ID = site_ID;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
