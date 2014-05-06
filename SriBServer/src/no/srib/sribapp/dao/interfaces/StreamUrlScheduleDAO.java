package no.srib.sribapp.dao.interfaces;

import java.util.Calendar;
import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.Streamurlschedule;

public interface StreamUrlScheduleDAO extends
        AbstractModelDAO<Streamurlschedule> {

    List<Streamurlschedule> getbyDay(int day) throws DAOException;

    List<Streamurlschedule> getUpcomingSchedule(final Calendar currentTime)
            throws DAOException;

    boolean isMainSourceActive() throws DAOException;
}
