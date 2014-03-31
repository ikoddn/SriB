package no.srib.sribapp.resource;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    private static int index = 0;

    private PodcastDAO dao;
    private StreamurlDAO sDao;
    private List<Podcast> podcastList = null;
    private Random ran;
    
    public PodcastResource() {

        ran = new Random();
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

    public final StreamSchedule getCurrentStreamSchedule() throws DAOException {
        Streamurl streamurl = sDao.getList().get(index);
        index = index + 1 % 2;
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 14);
        return new StreamSchedule(streamurl.getUrl(), time);

    }
}
