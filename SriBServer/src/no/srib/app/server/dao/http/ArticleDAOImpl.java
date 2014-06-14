package no.srib.app.server.dao.http;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.ejb.Stateless;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.http.helper.Articles;
import no.srib.app.server.dao.interfaces.ArticleDAO;
import no.srib.app.server.model.json.Article;

import com.fasterxml.jackson.databind.ObjectMapper;

@Stateless
public class ArticleDAOImpl extends AbstractModelDAOImpl<Article> implements
        ArticleDAO {

    private static final ObjectMapper MAPPER;
    private static final String APIURL;
    private static final String POST_TYPE;

    static {
        MAPPER = new ObjectMapper();
        APIURL = "http://srib.no/wp_api/v1/posts";
        POST_TYPE = "?post_type=post";
    }

    @Override
    public List<Article> getList() throws DAOException {
        List<Article> list = null;

        try {
            URL url = new URL(APIURL + POST_TYPE);
            Articles newsArticles = MAPPER.readValue(url, Articles.class);
            list = newsArticles.getPosts();
        } catch (IOException e) {
            throw new DAOException(e);
        }

        return list;
    }

    @Override
    public List<Article> getRecentArticles(final int number)
            throws DAOException {
        List<Article> list = null;

        try {
            URL url = new URL(APIURL + POST_TYPE + "&per_page=" + number);
            Articles newsArticles = MAPPER.readValue(url, Articles.class);
            list = newsArticles.getPosts();
        } catch (IOException e) {
            throw new DAOException(e);
        }

        return list;
    }

    @Override
    public Article getById(final int id) throws DAOException {
        Article result = null;

        try {
            URL url = new URL(APIURL + "/" + id + POST_TYPE);
            result = MAPPER.readValue(url, Article.class);
        } catch (IOException e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public List<Article> search(final String searchString, final int number)
            throws DAOException {

        List<Article> list = null;

        try {
            String urlEncoded = URLEncoder.encode(searchString, "UTF-8");
            URL url = new URL(APIURL + POST_TYPE + "&s=" + urlEncoded
                    + "&per_page=" + number);

            Articles newsArticles = MAPPER.readValue(url, Articles.class);
            list = newsArticles.getPosts();
        } catch (IOException e) {
            throw new DAOException(e);
        }

        return list;
    }

    @Override
    public void add(Article el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void update(Article el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void remove(Article el) throws DAOException {
        throw new DAOException("Not supported");
    }
}
