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
import no.srib.sribapp.dao.hibernate.StreamurlDAOImpl;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Podcast;
import no.srib.sribapp.model.Streamurl;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class PodcastResource {

    private PodcastDAO dao;
    private StreamurlDAO sDao;
    private List<Podcast> podcastList = null;
   
    
    public PodcastResource() {
        dao = new PodcastDAOImpl();
        sDao = new StreamurlDAOImpl();
        
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
    public final Streamurl getCurrentRadioURL() throws DAOException{
       
       
        
        
        
            return sDao.getList().get(1);
      
        
    }
}
