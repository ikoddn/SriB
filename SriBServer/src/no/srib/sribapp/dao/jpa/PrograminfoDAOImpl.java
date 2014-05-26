package no.srib.sribapp.dao.jpa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    // SELECT DISTINCT A FROM Podcast P, Programinfo A WHERE A.program=P.program
    // ORDER BY A.title
    // SELECT P FROM (SELECT DISTINCT I.title FROM digas.PODCAST C, "
    // +
    // "digas.PROGRAMINFO I WHERE CREATEDATE > 20130520 AND C.PROGRAM=I.program "
    // +
    // "order by I.TITLE) P UNION SELECT A FROM (SELECT DISTINCT I.title FROM digas.PODCAST C, digas.PROGRAMINFO I WHERE CREATEDATE < 20130520  AND C.PROGRAM=I.program order by I.TITLE) A"
    @Override
    public List<Programinfo> getProgramInfosWithPodcast(Calendar cal,
            boolean afterDate) throws DAOException {

        List<Programinfo> list = null;

        String format = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String stringDate = sdf.format(cal.getTime());
        int date = Integer.parseInt(stringDate);

        String queryString;

        if (afterDate) {
            queryString = "SELECT DISTINCT I FROM Podcast P, Programinfo I WHERE P.createdate > :date AND P.program=I.program order by I.title, P.createdate desc, P.createtime desc";
        } else {
            queryString = "SELECT DISTINCT I FROM Podcast P, Programinfo I WHERE P.createdate <= :date AND P.program=I.program order by I.title,P.createdate desc, P.createtime desc";
        }

        TypedQuery<Programinfo> query = em.createQuery(queryString,
                Programinfo.class);
        query.setParameter("date", date);
        try {
            list = query.getResultList();
            if (list.isEmpty()) {
                list = null;
            }

        } catch (Exception e) {
            throw new DAOException(e);
        }

        for (Programinfo a : list) {
            System.out.println(a.getTitle());
        }

        return list;
    }

    @Override
    public void addElement(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void updateElement(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void removeElement(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }
}
