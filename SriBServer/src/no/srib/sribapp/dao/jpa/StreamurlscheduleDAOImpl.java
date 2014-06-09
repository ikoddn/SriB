package no.srib.sribapp.dao.jpa;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.sribapp.model.jpa.Streamurlschedule;

@Stateless
public class StreamurlscheduleDAOImpl extends
        AbstractModelDAOImpl<Streamurlschedule> implements StreamUrlScheduleDAO {

    public StreamurlscheduleDAOImpl() {
        super(Streamurlschedule.class);
    }

    @Override
    public List<Streamurlschedule> getbyDay(final int day) throws DAOException {
        List<Streamurlschedule> result = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Streamurlschedule> criteria = cb
                .createQuery(Streamurlschedule.class);
        Root<Streamurlschedule> streamurlschedule = criteria
                .from(Streamurlschedule.class);
        Predicate p = cb.equal(streamurlschedule.get("day"), day);
        criteria.where(p);

        TypedQuery<Streamurlschedule> query = em.createQuery(criteria);

        try {
            result = query.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public List<Streamurlschedule> getUpcomingSchedule(
            final Calendar currentTime) throws DAOException {

        List<Streamurlschedule> result = null;

        String queryString = "SELECT S FROM Streamurlschedule S ORDER BY CASE WHEN (S.day=:day) THEN (CASE WHEN (S.totime>:time) THEN 0 ELSE 3 END) WHEN (S.day<:day) THEN 2 ELSE 1 END, S.day, S.fromtime";
        TypedQuery<Streamurlschedule> query = em.createQuery(queryString,
                Streamurlschedule.class);
        query.setParameter("day", currentTime.get(Calendar.DAY_OF_WEEK));
        query.setParameter("time",
                new java.sql.Time(currentTime.getTimeInMillis()));

        try {
            result = query.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public boolean isMainSourceActive() throws DAOException {
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
