package no.srib.sribapp.dao.jpa;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Schedule> criteria = cb.createQuery(Schedule.class);
        Root<Schedule> schedule = criteria.from(Schedule.class);
        Order o1 = cb.asc(schedule.get("day"));
        Order o2 = cb.asc(schedule.get("fromtime"));
        Order o3 = cb.asc(schedule.get("totime"));
        criteria.orderBy(o1, o2, o3);

        TypedQuery<Schedule> query = em.createQuery(criteria);

        try {
            result = query.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        return result;
    }

    @Override
    public Schedule getScheduleForTime(final Calendar time) throws DAOException {
        Schedule result;
        List<Schedule> list = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        int day = time.get(Calendar.DAY_OF_WEEK);
        String timeString = dateFormat.format(time.getTime());
        Time timeNow = Time.valueOf(timeString);

        CriteriaQuery<Schedule> criteria = cb.createQuery(Schedule.class);
        Root<Schedule> schedule = criteria.from(Schedule.class);
        Predicate p1 = cb.equal(schedule.get("day"), day);
        Predicate p2 = cb.lessThanOrEqualTo(schedule.<Time> get("fromtime"),
                timeNow);
        Predicate p3 = cb.greaterThan(schedule.<Time> get("totime"), timeNow);
        criteria.where(p1, p2, p3);

        TypedQuery<Schedule> query = em.createQuery(criteria);

        try {
            list = query.getResultList();
        } catch (Exception e) {
            throw new DAOException(e);
        }

        if (list == null || list.isEmpty()) {
            result = null;
        } else {
            // If someone has accidently added a program twice in the schedule
            result = list.get(0);
        }

        return result;
    }
}
