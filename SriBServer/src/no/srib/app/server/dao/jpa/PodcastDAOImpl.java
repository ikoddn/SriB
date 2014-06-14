package no.srib.app.server.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.PodcastDAO;
import no.srib.app.server.model.jpa.Podcast;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

@Stateless
public class PodcastDAOImpl extends AbstractModelDAOImpl<Podcast> implements
        PodcastDAO {

    public PodcastDAOImpl() {
        super(Podcast.class);
    }

    @Override
    public List<Podcast> getPodcasts(final int programID) throws DAOException {
        List<Podcast> result = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Podcast> criteria = cb.createQuery(Podcast.class);
        Root<Podcast> podcast = criteria.from(Podcast.class);
        Predicate p1 = cb.equal(podcast.get("program"), programID);
        Predicate p2 = cb.equal(podcast.get("softdel"), 0);
        criteria.where(p1, p2);
        Order o1 = cb.desc(podcast.get("createdate"));
        Order o2 = cb.desc(podcast.get("createtime"));
        criteria.orderBy(o1, o2);

        TypedQuery<Podcast> query = em.createQuery(criteria);
        query.setHint(QueryHints.READ_ONLY, HintValues.TRUE);

        try {
            result = query.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public void add(Podcast el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void update(Podcast el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void remove(Podcast el) throws DAOException {
        throw new DAOException("Not supported");
    }
}
