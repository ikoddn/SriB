package no.srib.sribapp.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.exception.DuplicateEntryException;
import no.srib.sribapp.dao.interfaces.ProgramnameDAO;
import no.srib.sribapp.model.jpa.Programname;

import org.eclipse.persistence.exceptions.DatabaseException;

@Stateless
public class ProgramnameDAOImpl extends AbstractModelDAOImpl<Programname>
        implements ProgramnameDAO {

    public ProgramnameDAOImpl() {
        super(Programname.class);
    }

    public List<Programname> getSortedList() throws DAOException {
        List<Programname> list = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Programname> criteria = cb.createQuery(Programname.class);
        Root<Programname> programname = criteria.from(Programname.class);
        Order ordering = cb.asc(programname.get("name"));
        criteria.orderBy(ordering);

        TypedQuery<Programname> query = em.createQuery(criteria);

        try {
            list = query.getResultList();
        } catch (NoResultException e) {
            list = null;
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return list;
    }

    @Override
    public void add(Programname programName) throws DAOException,
            DuplicateEntryException {
        try {
            em.persist(programName);
            em.flush();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException) {
                throw new DuplicateEntryException();
            } else {
                throw new DAOException();
            }
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    @Override
    public void update(Programname programName) throws DAOException,
            DuplicateEntryException {
        try {
            em.merge(programName);
            em.flush();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException) {
                System.out.println("yololololo");
                throw new DuplicateEntryException();
            } else {
                throw new DAOException();
            }
        } catch (Exception e) {
            throw new DAOException();
        }
    }
}
