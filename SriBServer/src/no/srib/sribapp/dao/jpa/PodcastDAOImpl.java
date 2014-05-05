package no.srib.sribapp.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Podcast;

@Stateless
public class PodcastDAOImpl extends AbstractModelDAOImpl<Podcast> implements
        PodcastDAO {

    public PodcastDAOImpl() {
        super(Podcast.class);
    }

    @Override
    public List<Podcast> getPodcasts(final int programID) throws DAOException {
        List<Podcast> result = null;

        String queryString = "SELECT P FROM Podcast P WHERE P.program=:id";
        TypedQuery<Podcast> query = em.createQuery(queryString, Podcast.class);
        query.setParameter("id", programID);

        try {
            result = query.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }
}
