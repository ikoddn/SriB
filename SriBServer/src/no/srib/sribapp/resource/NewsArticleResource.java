package no.srib.sribapp.resource;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.NewsArticleDAO;
import no.srib.sribapp.model.NewsArticle;

@Path("news")
@Produces(MediaType.APPLICATION_JSON)
@ManagedBean
public class NewsArticleResource {

    private static final int DEFAULT_NUMBER_OF_ARTICLES = 10;

    @EJB
    private NewsArticleDAO newsArticleDAO;

    @GET
    public List<NewsArticle> getRecentArticles(
            @QueryParam("number") final int paramNumber) {

        int number;

        if (paramNumber > 0) {
            number = paramNumber;
        } else if (paramNumber == 0) {
            number = DEFAULT_NUMBER_OF_ARTICLES;
        } else {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        List<NewsArticle> list = null;

        try {
            list = newsArticleDAO.getRecentArticles(number);
        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (list == null || list.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        return list;
    }
}
