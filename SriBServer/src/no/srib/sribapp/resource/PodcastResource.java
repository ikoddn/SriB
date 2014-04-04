package no.srib.sribapp.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.PodcastDAOImpl;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Podcast;

@Path("podcast")
@Produces(MediaType.APPLICATION_JSON)
public class PodcastResource {

    private PodcastDAO podcastDAO;

    public PodcastResource() {
        podcastDAO = new PodcastDAOImpl();
    }

    /**
     * 
     * 
     * @return A list with all recent podcasts.
     */
    @GET
    public final List<Podcast> getAllPodcast() {
        List<Podcast> list = null;

        try {
            list = podcastDAO.getList();
        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (list == null || list.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        return list;
    }
}
