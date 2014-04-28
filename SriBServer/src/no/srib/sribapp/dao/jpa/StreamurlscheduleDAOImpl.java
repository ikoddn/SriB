package no.srib.sribapp.dao.jpa;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.sribapp.model.Streamurlschedule;

@Stateless
public class StreamurlscheduleDAOImpl extends
        AbstractModelDAOImpl<Streamurlschedule> implements StreamUrlScheduleDAO {

    public StreamurlscheduleDAOImpl() {
        super(Streamurlschedule.class);
       
    }

    @Override
    public List<Streamurlschedule> getbyDay(int day) throws DAOException {
        List<Streamurlschedule> streamurlschedule = null;

        String queryString = "SELECT S FROM streamurlschedule S WHERE S.id=:day";

        TypedQuery<Streamurlschedule> query = em.createQuery(queryString,
                Streamurlschedule.class);
        query.setParameter("day", day);

        try {
            streamurlschedule = query.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return streamurlschedule;
    }

}
