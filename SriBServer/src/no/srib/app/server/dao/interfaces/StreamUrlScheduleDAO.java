package no.srib.app.server.dao.interfaces;

import java.util.Calendar;
import java.util.List;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.model.jpa.Streamurlschedule;

public interface StreamUrlScheduleDAO extends
        AbstractModelDAO<Streamurlschedule> {

    List<Streamurlschedule> getbyDay(int day) throws DAOException;

    List<Streamurlschedule> getUpcomingSchedule(final Calendar currentTime)
            throws DAOException;

    boolean isMainSourceActive() throws DAOException;
}
