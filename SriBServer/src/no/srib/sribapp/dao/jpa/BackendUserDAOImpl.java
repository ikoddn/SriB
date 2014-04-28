package no.srib.sribapp.dao.jpa;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.BackendUserDAO;
import no.srib.sribapp.model.Backenduser;

@Stateless
public class BackendUserDAOImpl extends AbstractModelDAOImpl<Backenduser>
        implements BackendUserDAO {

    public BackendUserDAOImpl() {
        super(Backenduser.class);
    }

    @Override
    public Backenduser getByUserName(String username) throws DAOException {
        Backenduser result = null;
        String queryString = "SELECT B FROM Backenduser B WHERE B.username=:username";
        TypedQuery<Backenduser> query = em.createQuery(queryString,
                Backenduser.class);
        query.setParameter("username", username);

        try {
            result = query.getSingleResult();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }
}
