package no.srib.sribapp.dao.jpa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.PrograminfoDAO;
import no.srib.sribapp.model.jpa.Programinfo;

@Stateless
public class PrograminfoDAOImpl extends AbstractModelDAOImpl<Programinfo>
        implements PrograminfoDAO {

    public PrograminfoDAOImpl() {
        super(Programinfo.class);
    }

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
    public void add(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void update(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void remove(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }
}
