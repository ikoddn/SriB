package no.srib.sribapp.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.AbstractModelDAO;
import no.srib.sribapp.model.AbstractModel;


class AbstractModelDAOImpl<T extends AbstractModel> implements AbstractModelDAO<T> {

    private final static EntityManagerFactory emf;
    private final Class<T> typeClass;
    private final String getAllNamedQuery;
    
    static {
        emf = Persistence.createEntityManagerFactory("heroku_test");
    }
    
    protected AbstractModelDAOImpl(final Class<T> typeClass) {
        this.typeClass = typeClass;
        getAllNamedQuery = typeClass.getSimpleName() + ".findAll";
    }
    
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public List<T> getList() throws DAOException {
        EntityManager em = getEntityManager();
        List<T> list = null;
        
        try {
            list = em.createNamedQuery(getAllNamedQuery, typeClass).getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        } finally {
            em.close();
        }
        
        return list;
    }
}
