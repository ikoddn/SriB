package no.srib.sribapp.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.srib.sribapp.model.Podcast;

@Path("/podcast")
@Produces(MediaType.APPLICATION_JSON)
public class PodcastResource {

    private List<Podcast> listPodcast;
    
    public PodcastResource() {
        listPodcast = new ArrayList<Podcast>();
        Podcast p = new Podcast();
        p.setTitle("YOLO SHOW");
        
        listPodcast.add(p);
    }
    
    /**
     * 
     * @return A list with all recent podcasts.
     */
    @GET
    public final List<Podcast> getAllPodcast() {
        return listPodcast;
    }
}
