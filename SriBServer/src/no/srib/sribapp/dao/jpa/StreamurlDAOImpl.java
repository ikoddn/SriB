package no.srib.sribapp.dao.jpa;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Streamurl;

@Stateless
public class StreamurlDAOImpl extends AbstractModelDAOImpl<Streamurl> implements
        StreamurlDAO {

    public StreamurlDAOImpl() {
        super(Streamurl.class);
    }

    @Override
    public Streamurl getById(final int id) throws DAOException {
        Streamurl stream = null;

        String queryString = "SELECT S FROM Streamurl S WHERE S.id=:id";

        TypedQuery<Streamurl> query = em.createQuery(queryString,
                Streamurl.class);
        query.setParameter("id", id);

        try {
            stream = query.getSingleResult();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return stream;
    }
}
