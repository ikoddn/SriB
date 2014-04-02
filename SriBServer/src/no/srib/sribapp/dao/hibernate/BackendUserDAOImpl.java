package no.srib.sribapp.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.BackendUserDAO;
import no.srib.sribapp.model.Backenduser;

public class BackendUserDAOImpl extends AbstractModelDAOImpl<Backenduser> implements BackendUserDAO {

    public BackendUserDAOImpl() {
        super(Backenduser.class);
      
    }

    @Override
    public Backenduser getByUserName(String username) throws DAOException {
        EntityManager em = getEntityManager();
        Backenduser result = null;
        String queryString = "SELECT B FROM Backenduser B WHERE username=:username";
        TypedQuery<Backenduser> query = em.createQuery(queryString, Backenduser.class);
        query.setParameter("username", username);

        try {
            result = query.getSingleResult();
        } catch (PersistenceException e) {
           
                
           
        } finally {
            em.close();
        }
        return result;
        
    }
    
    
    

}
