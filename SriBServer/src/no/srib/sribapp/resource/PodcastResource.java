package no.srib.sribapp.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.PodcastDAOImpl;
import no.srib.sribapp.dao.interfaces.PodcastDAO;

import no.srib.sribapp.model.Podcast;

@Path("/podcast")
@Produces(MediaType.APPLICATION_JSON)
public class PodcastResource {

    private  PodcastDAO dao;
    List<Podcast> podcastList = null;
    
    public PodcastResource() {
        
        dao = new PodcastDAOImpl();
        
        
        try {
            podcastList = dao.getList();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        
        
      
        
   
    }
    
    /**
     * 
     * 
     * @return A list with all recent podcasts.
     */
    @GET
    public final List<Podcast> getAllPodcast() {
        return podcastList;
    }
}
