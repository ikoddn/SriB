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

      //  String queryString = "SELECT P FROM Podcast P WHERE P.program=:id AND P.softdel=0 ORDER BY P.createdate DESC";
        //TypedQuery<Podcast> query = em.createQuery(queryString, Podcast.class);
        TypedQuery<Podcast> q = em.createNamedQuery("melk", Podcast.class);
        q.setParameter("id", programID);
        //query.setHint("eclipselink.read-only", "true");
        try {
            result = q.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public Podcast getPodcastByRefnr(int refnr) throws DAOException {
        
       
        return new Podcast();
    }

    
}
