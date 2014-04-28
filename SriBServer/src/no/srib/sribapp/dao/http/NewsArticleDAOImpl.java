package no.srib.sribapp.dao.http;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.ejb.Stateless;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.http.helper.NewsArticles;
import no.srib.sribapp.dao.interfaces.NewsArticleDAO;
import no.srib.sribapp.model.NewsArticle;

@Stateless
public class NewsArticleDAOImpl extends AbstractModelDAOImpl<NewsArticle>
        implements NewsArticleDAO {

    private static final ObjectMapper MAPPER;
    private static final String APIURL;

    static {
        MAPPER = new ObjectMapper();
        APIURL = "http://srib.no/wp_api/v1/";
    }

    @Override
    public List<NewsArticle> getList() throws DAOException {
        List<NewsArticle> list = null;

        try {
            URL url = new URL(APIURL + "posts?post_type=post");
            NewsArticles newsArticles = MAPPER.readValue(url,
                    NewsArticles.class);
            list = newsArticles.getPosts();
        } catch (IOException e) {
            throw new DAOException(e);
        }

        return list;
    }

    @Override
    public List<NewsArticle> getRecentArticles(final int number)
            throws DAOException {
        List<NewsArticle> list = null;

        try {
            URL url = new URL(APIURL + "posts?post_type=post&per_page="
                    + number);
            NewsArticles newsArticles = MAPPER.readValue(url,
                    NewsArticles.class);
            list = newsArticles.getPosts();
        } catch (IOException e) {
            throw new DAOException(e);
        }

        return list;
    }

    @Override
    public NewsArticle getById(final int id) throws DAOException {
        NewsArticle result = null;

        try {
            URL url = new URL(APIURL + "posts/" + id);
            result = MAPPER.readValue(url, NewsArticle.class);
        } catch (IOException e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public void addElement(NewsArticle el) throws DAOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateElement(NewsArticle el) throws DAOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeElement(NewsArticle el) throws DAOException {
        // TODO Auto-generated method stub
        
    }
}
