package no.srib.app.server.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.AbstractModelDAO;
import no.srib.app.server.model.AbstractModel;

abstract class AbstractModelDAOImpl<T extends AbstractModel> implements
        AbstractModelDAO<T> {

    @PersistenceContext(unitName = "srib")
    protected EntityManager em;

    private final Class<T> TYPECLASS;
    private final String GET_ALL_NAMED_QUERY;

    protected AbstractModelDAOImpl(final Class<T> typeClass) {
        this.TYPECLASS = typeClass;
        GET_ALL_NAMED_QUERY = typeClass.getSimpleName() + ".findAll";
    }

    @Override
    public T getById(final int id) throws DAOException {
        try {
            return em.find(TYPECLASS, id);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<T> getList() throws DAOException {
        List<T> list = null;

        try {
            list = em.createNamedQuery(GET_ALL_NAMED_QUERY, TYPECLASS)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return list;
    }

    @Override
    public void add(T el) throws DAOException {
        try {
            em.persist(el);
        } catch (Exception e) {
            throw new DAOException(e);
        }

    }

    @Override
    public void update(T el) throws DAOException {
        try {
            em.merge(el);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void remove(T el) throws DAOException {
        try {
            em.remove(em.merge(el));
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }
}
