package no.srib.sribapp.dao.http.helper;

import java.util.List;

import no.srib.sribapp.model.NewsArticle;

public class NewsArticles {

    private int found;
    private List<NewsArticle> posts;

    public NewsArticles() {
    }

    public int getFound() {
        return found;
    }

    public void setFound(final int found) {
        this.found = found;
    }

    public List<NewsArticle> getPosts() {
        return posts;
    }

    public void setPosts(final List<NewsArticle> posts) {
        this.posts = posts;
    }
}
