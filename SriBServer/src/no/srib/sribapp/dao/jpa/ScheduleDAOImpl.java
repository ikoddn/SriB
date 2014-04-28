package no.srib.sribapp.dao.jpa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.ScheduleDAO;
import no.srib.sribapp.model.Schedule;

@Stateless
public class ScheduleDAOImpl extends AbstractModelDAOImpl<Schedule> implements
        ScheduleDAO {

    public ScheduleDAOImpl() {
        super(Schedule.class);
    }

    @Override
    public List<Schedule> getSortedSchedule() throws DAOException {
        List<Schedule> result = null;

        String queryString = "SELECT S FROM Schedule S ORDER BY S.day, S.fromtime, S.totime";
        TypedQuery<Schedule> query = em
                .createQuery(queryString, Schedule.class);

        try {
            result = query.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public Schedule getScheduleForTime(final Calendar time) throws DAOException {
        Schedule result = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int day = time.get(Calendar.DAY_OF_WEEK);
        String timeString = dateFormat.format(time.getTime());

        String queryString = "SELECT S FROM Schedule S WHERE S.day=:day AND S.fromtime<=:timeString AND S.totime>:timeString";
        TypedQuery<Schedule> query = em
                .createQuery(queryString, Schedule.class);
        query.setParameter("day", day);
        query.setParameter("timeString", timeString);

        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public void deleteSchedule(int id) throws DAOException {
        em.getTransaction().begin();
        String queryString = "DELETE FROM Schedule S WHERE S.id=:id";
        Query query = em.createQuery(queryString);
        query.setParameter("id", id);
        try {
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new DAOException(e);
        }
    }
}
