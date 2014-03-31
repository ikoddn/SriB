package no.srib.sribapp.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Streamurl;

public class StreamurlDAOImpl extends AbstractModelDAOImpl<Streamurl> implements
        StreamurlDAO {

    public StreamurlDAOImpl() {
        super(Streamurl.class);
    }

    @Override
    public Streamurl getById(final int id) throws DAOException {
        Streamurl stream = null;
        EntityManager em = getEntityManager();

        String queryString = "SELECT S FROM STREAMURL S WHERE ID=:id";

        TypedQuery<Streamurl> query = em.createQuery(queryString,
                Streamurl.class);
        query.setParameter("id", id);

        try {
            stream = query.getSingleResult();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        } finally {
            em.close();
        }

        return stream;
    }

}
