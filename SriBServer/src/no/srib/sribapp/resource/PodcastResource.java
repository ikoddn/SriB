package no.srib.sribapp.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.PodcastDAOImpl;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Podcast;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class PodcastResource {

    private PodcastDAO dao;
    private List<Podcast> podcastList = null;
    
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
    @Path("podcast")
    @GET
    public final List<Podcast> getAllPodcast() {
        return podcastList;
    }
    
    
    
    @Path("radiourl")
    @GET
    public final String getCurrentRadioURL(){
       
        
        return "http://lyd.nrk.no/nrk_radio_p2_mp3_h";
        
    }
}
