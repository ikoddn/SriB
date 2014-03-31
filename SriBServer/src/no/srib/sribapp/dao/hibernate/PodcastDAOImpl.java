package no.srib.sribapp.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Podcast;

public class PodcastDAOImpl extends AbstractModelDAOImpl<Podcast> implements PodcastDAO {

    public PodcastDAOImpl() {
        super(Podcast.class);
    }

    @Override
    public List<Podcast> getPodcasts(final int programID) throws DAOException {
        List<Podcast> result = null;
        EntityManager em = getEntityManager();
        
        String queryString = "SELECT P FROM PODCAST P WHERE PROGRAM=:id";
        TypedQuery<Podcast> query = em.createQuery(queryString, Podcast.class);
        query.setParameter("id", programID);

        try {
            result = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        } finally {
            em.close();
        }
        return result;
    }
}
