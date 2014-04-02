package no.srib.sribapp.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.ScheduleDAO;
import no.srib.sribapp.model.Schedule;

public class ScheduleDAOImpl extends AbstractModelDAOImpl<Schedule> implements
        ScheduleDAO {

    public ScheduleDAOImpl() {
        super(Schedule.class);
    }

    @Override
    public List<Schedule> getSchedulesForDay(final int day) throws DAOException {
        List<Schedule> result = null;
        EntityManager em = getEntityManager();

        String queryString = "SELECT S FROM Schedule S WHERE DAY=:day";
        TypedQuery<Schedule> query = em
                .createQuery(queryString, Schedule.class);
        query.setParameter("day", day);

        try {
            result = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        } finally {
            em.close();
        }

        return result;
    }

    @Override
    public Schedule getScheduleForTime(final Calendar time) throws DAOException {
        Schedule result = null;
        EntityManager em = getEntityManager();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int day = time.get(Calendar.DAY_OF_WEEK);
        String timeString = dateFormat.format(time.getTime());

        String queryString = "SELECT S FROM Schedule S WHERE DAY=:day AND FROMTIME<=:timeString AND TOTIME>:timeString";
        TypedQuery<Schedule> query = em
                .createQuery(queryString, Schedule.class);
        query.setParameter("day", day);
        query.setParameter("timeString", timeString);

        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        } catch (PersistenceException e) {
            throw new DAOException(e);
        } finally {
            em.close();
        }

        return result;
    }
}
