package no.srib.sribapp.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.exception.DuplicateEntryException;
import no.srib.sribapp.dao.interfaces.ProgramnameDAO;
import no.srib.sribapp.model.Programname;

import org.eclipse.persistence.exceptions.DatabaseException;

@Stateless
public class ProgramnameDAOImpl extends AbstractModelDAOImpl<Programname>
        implements ProgramnameDAO {

    protected ProgramnameDAOImpl() {
        super(Programname.class);

    }

    @Override
    public Programname getById(int id) throws DAOException {
        Programname progName = null;
        String queryString = "SELECT P FROM Programname P WHERE P.id=:id";
        TypedQuery<Programname> query = em.createQuery(queryString,
                Programname.class);
        query.setParameter("id", id);

        try {
            progName = query.getSingleResult();
        } catch (NoResultException e) {
            progName = null;
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return progName;
    }

    public List<Programname> getSortedList() throws DAOException {
        List<Programname> list = null;
        String queryString = "SELECT P FROM Programname P ORDER BY P.name ";
        TypedQuery<Programname> query = em.createQuery(queryString,
                Programname.class);

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
    public void addProgramName(Programname programName) throws DAOException,
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
        }

        catch (Exception e) {
            throw new DAOException();
        }

    }

    @Override
    public void updateProgramName(Programname programName) throws DAOException,
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
