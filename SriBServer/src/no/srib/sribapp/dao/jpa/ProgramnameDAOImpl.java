package no.srib.sribapp.dao.jpa;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.ProgramnameDAO;
import no.srib.sribapp.model.Programname;

@Stateless
public class ProgramnameDAOImpl extends AbstractModelDAOImpl<Programname> implements ProgramnameDAO {

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
        } catch(NoResultException e){ 
            progName = null;
        }
        catch (Exception e) {
            throw new DAOException(e);
        }
        return progName;
    }

}
