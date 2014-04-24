package no.srib.sribapp.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.AbstractModelDAO;
import no.srib.sribapp.model.AbstractModel;

abstract class AbstractModelDAOImpl<T extends AbstractModel> implements
        AbstractModelDAO<T> {

    @PersistenceContext(unitName = "heroku_test")
    protected EntityManager em;

    private final Class<T> TYPECLASS;
    private final String GET_ALL_NAMED_QUERY;

    protected AbstractModelDAOImpl(final Class<T> typeClass) {
        this.TYPECLASS = typeClass;
        GET_ALL_NAMED_QUERY = typeClass.getSimpleName() + ".findAll";
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
}
