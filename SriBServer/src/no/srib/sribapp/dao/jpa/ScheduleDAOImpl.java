package no.srib.sribapp.dao.jpa;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
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
  
        List<Schedule> list = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int day = time.get(Calendar.DAY_OF_WEEK);
        String timeString = dateFormat.format(time.getTime());
        Time timeNow = Time.valueOf(timeString);
        
        String queryString = "SELECT S FROM Schedule S WHERE S.day=:day AND S.fromtime<=:timeNow AND S.totime>=:timeNow";
        TypedQuery<Schedule> query = em
                .createQuery(queryString, Schedule.class);
        query.setParameter("day", day);
        query.setParameter("timeNow", timeNow);

        try {
          
            list = query.getResultList();
            if(list.isEmpty()){
                list = null;
            }
        }
         catch (Exception e) {
            throw new DAOException(e);
        }
        
        //If someone accidently add a program twice in the schedule
        return list.get(0);
    }

 
}
