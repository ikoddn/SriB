package no.srib.sribapp.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.PrograminfoDAO;
import no.srib.sribapp.model.Programinfo;

public class PrograminfoDAOImpl extends AbstractModelDAOImpl<Programinfo> implements PrograminfoDAO {

    public PrograminfoDAOImpl() {
        super(Programinfo.class);
    }

    @Override
    public Programinfo getById(final int id) throws DAOException {
        Programinfo result = null;
        EntityManager em = getEntityManager();
        
        String queryString = "SELECT P FROM PROGRAMINFO P WHERE PROGRAM=:id";
        TypedQuery<Programinfo> query = em.createQuery(queryString, Programinfo.class);
        query.setParameter("id", id);

        try {
            result = query.getSingleResult();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        } finally {
            em.close();
        }
        
        return result;
    }
}
