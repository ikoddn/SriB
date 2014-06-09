package no.srib.sribapp.dao.interfaces;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.json.Article;

public interface ArticleDAO extends AbstractModelDAO<Article> {

    /**
     * Returns a list of the most recent {@code number} articles.
     * 
     * @param number
     *            - The number of articles.
     * @return A list of articles or {@code null} if none was found.
     * @throws DAOException
     */
    List<Article> getRecentArticles(final int number) throws DAOException;

    /**
     * Returns a list of the most recent {@code number} articles containing the
     * given search string.
     * 
     * @param searchString
     *            - The string to search for.
     * @param number
     *            - The max number of results.
     * @return A list of articles or {@code null} if none was found.
     * @throws DAOException
     */
    List<Article> search(final String searchString, final int number)
            throws DAOException;
}
