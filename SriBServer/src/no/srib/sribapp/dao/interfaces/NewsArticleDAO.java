package no.srib.sribapp.dao.interfaces;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.NewsArticle;

public interface NewsArticleDAO extends AbstractModelDAO<NewsArticle> {

    /**
     * Returns a list of the most recent {@code number} articles.
     * 
     * @param number
     *            - The number of articles.
     * @return A list of articles or {@code null} if none was found.
     * @throws DAOException
     */
    List<NewsArticle> getRecentArticles(final int number) throws DAOException;

    /**
     * Returns the article with the given ID.
     * 
     * @param id
     *            - The article ID.
     * @return The article or {@code null} if no article was found.
     * @throws DAOException
     */
    NewsArticle getById(final int id) throws DAOException;
}
