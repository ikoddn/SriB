package no.srib.sribapp.model.json;

import java.util.List;

import no.srib.sribapp.model.AbstractModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleMedia extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private int id;

    @JsonProperty("alt_text")
    private String altText;
    private List<ArticleImage> sizes;

    public ArticleMedia() {
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

    public List<ArticleImage> getSizes() {
        return sizes;
    }

    public void setSizes(List<ArticleImage> sizes) {
        this.sizes = sizes;
    }
}
