package no.srib.sribapp.dao.interfaces;

import java.util.Calendar;
import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.Schedule;

/**
 * A Data Access Object for Schedule entities.
 * 
 * @author Sveinung
 * 
 */
public interface ScheduleDAO extends AbstractModelDAO<Schedule> {

    /**
     * Returns a list of schedules for the given day.
     * 
     * @param day
     *            - The day as an integer in the range 1-7, where 1 is Sunday.
     * @return A list of Schedule entities.
     * @throws DAOException
     */
    List<Schedule> getSchedulesForDay(final int day) throws DAOException;

    /**
     * Returns a schedule for the given instant in time.
     * 
     * @param time
     *            - The instant in time.
     * @return The schedule entity or {@code null} if none was found.
     * @throws DAOException
     */
    Schedule getScheduleForTime(final Calendar time) throws DAOException;
}
