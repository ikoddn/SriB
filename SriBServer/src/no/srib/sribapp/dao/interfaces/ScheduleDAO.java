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
     * Returns a list of sorted schedules, sorting on from time, and end time.
     * 
     * 
     * @return A list of Schedule entities.
     * @throws DAOException
     */
    List<Schedule> getSortedSchedule() throws DAOException;

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
