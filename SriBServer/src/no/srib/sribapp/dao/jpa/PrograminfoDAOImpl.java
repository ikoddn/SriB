package no.srib.sribapp.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.PrograminfoDAO;
import no.srib.sribapp.model.Programinfo;

@Stateless
public class PrograminfoDAOImpl extends AbstractModelDAOImpl<Programinfo>
        implements PrograminfoDAO {

    public PrograminfoDAOImpl() {
        super(Programinfo.class);
    }

    @Override
    public Programinfo getById(final int id) throws DAOException {
        Programinfo result = null;

        String queryString = "SELECT P FROM PROGRAMINFO P WHERE P.PROGRAM=:id";
        TypedQuery<Programinfo> query = em.createQuery(queryString,
                Programinfo.class);
        query.setParameter("id", id);

        try {
            result = query.getSingleResult();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public List<Programinfo> getProgramInfosWithPodcast() throws DAOException {
        List<Programinfo> list = null;
        String queryString = "SELECT DISTINCT A FROM Podcast P, Programinfo A WHERE A.program=P.program ORDER BY A.title ";
        TypedQuery<Programinfo> query = em.createQuery(queryString,
                Programinfo.class);

        try {
            list = query.getResultList();
            if (list.isEmpty()) {
                list = null;
            }

        } catch (Exception e) {
            throw new DAOException(e);
        }

        return list;
    }
}
