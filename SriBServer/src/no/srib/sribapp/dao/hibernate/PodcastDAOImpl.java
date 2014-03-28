package no.srib.sribapp.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Podcast;

public class PodcastDAOImpl extends AbstractModelDAOImpl<Podcast> implements PodcastDAO {

    public PodcastDAOImpl() {
        super(Podcast.class);
    }
    
    @Override
    public List<Podcast> getList() throws DAOException {
        EntityManager em = getEntityManager();
        List<Podcast> list = null;
        
        try {
            list = em.createNamedQuery("Podcast.findAll", Podcast.class).getResultList();
        } catch (PersistenceException e) {
            throw new DAOException();
        } finally {
            em.close();
        }
        
        return list;
    }
}
