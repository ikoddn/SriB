package no.srib.sribapp.dao.interfaces;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.Podcast;

/**
 * A Data Access Object for Podcast entities.
 * 
 * @author Sveinung
 *
 */
public interface PodcastDAO extends AbstractModelDAO<Podcast> {

    List<Podcast> getPodcasts(final int programID) throws DAOException;
}
