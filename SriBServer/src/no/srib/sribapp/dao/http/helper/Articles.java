package no.srib.sribapp.dao.http.helper;

import java.util.List;

import no.srib.sribapp.model.json.Article;

public class Articles {

    private List<Article> posts;

    public Articles() {
    }

    public List<Article> getPosts() {
        return posts;
    }

    public void setPosts(final List<Article> posts) {
        this.posts = posts;
    }
}
