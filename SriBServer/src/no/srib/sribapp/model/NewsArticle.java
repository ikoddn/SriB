package no.srib.sribapp.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({ "author", "meta", "taxonomies", "media" })
public class NewsArticle extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private String type;
    private Integer parent;
    private String parent_str;
    private String date;
    private String status;
    private String comment_status;
    private Integer menu_order;
    private String title;
    private String name;
    private String excerpt;
    private String content;
    // private Author author;
    private Integer id;
    private String id_str;
    private String permalink;
    private String modified;
    private Integer comment_count;
    private String excerpt_display;
    private String content_display;
    private String mime_type;

    // private Meta_ meta;
    // private Taxonomies taxonomies;
    // private List<Medium> media = new ArrayList<Medium>();
    // private Map<String, Object> additionalProperties = new HashMap<String,
    // Object>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getParent_str() {
        return parent_str;
    }

    public void setParent_str(String parent_str) {
        this.parent_str = parent_str;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }

    public Integer getMenu_order() {
        return menu_order;
    }

    public void setMenu_order(Integer menu_order) {
        this.menu_order = menu_order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /*
     * public Author getAuthor() { return author; }
     * 
     * public void setAuthor(Author author) { this.author = author; }
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public String getExcerpt_display() {
        return excerpt_display;
    }

    public void setExcerpt_display(String excerpt_display) {
        this.excerpt_display = excerpt_display;
    }

    public String getContent_display() {
        return content_display;
    }

    public void setContent_display(String content_display) {
        this.content_display = content_display;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    /*
     * public Meta_ getMeta() { return meta; }
     * 
     * public void setMeta(Meta_ meta) { this.meta = meta; }
     * 
     * public Taxonomies getTaxonomies() { return taxonomies; }
     * 
     * public void setTaxonomies(Taxonomies taxonomies) { this.taxonomies =
     * taxonomies; }
     * 
     * public List<Medium> getMedia() { return media; }
     * 
     * public void setMedia(List<Medium> media) { this.media = media; }
     * 
     * public Map<String, Object> getAdditionalProperties() { return
     * this.additionalProperties; }
     * 
     * public void setAdditionalProperty(String name, Object value) {
     * this.additionalProperties.put(name, value); }
     */
}
