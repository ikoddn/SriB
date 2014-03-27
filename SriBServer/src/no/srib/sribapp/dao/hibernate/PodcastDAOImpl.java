package no.srib.sribapp.dao.hibernate;

import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Podcast;

public class PodcastDAOImpl extends AbstractModelDAOImpl<Podcast> implements PodcastDAO {

    public PodcastDAOImpl() {
        super(Podcast.class);
    }
}
