package no.srib.sribapp.dao.jpa;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

        String queryString = "SELECT S FROM streamurlschedule S WHERE S.day=:day";

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
    
    

    @Override
    public Streamurlschedule getNextShiftTime() throws DAOException {
        
        return null;
    }

    @Override
    public boolean isMainSourceActive() throws DAOException {
        Streamurlschedule str = null;
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        String nowString = new SimpleDateFormat("HH:mm:ss").format(new Date());
        Time time = Time.valueOf(nowString);
        String queryString = "SELECT S FROM Streamurlschedule S WHERE S.day =:day AND S.totime >:time AND S.fromtime<:time";
        TypedQuery<Streamurlschedule> query = em.createQuery(queryString,
                Streamurlschedule.class);
        query.setParameter("day", day);
        query.setParameter("time", time);
        try {

            return query.getResultList().isEmpty();

        } catch (Exception e) {
            throw new DAOException(e);
        }

    }

}
