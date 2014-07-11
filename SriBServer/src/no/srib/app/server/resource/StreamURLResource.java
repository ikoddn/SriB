package no.srib.app.server.resource;

import java.util.Calendar;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.app.server.dao.interfaces.StreamurlDAO;
import no.srib.app.server.model.jpa.Streamurl;
import no.srib.app.server.model.jpa.Streamurlschedule;
import no.srib.app.server.model.json.StreamSchedule;
import no.srib.app.server.util.TimeUtil;

@Path("radiourl")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@ManagedBean
public class StreamURLResource {

    @EJB
    private StreamurlDAO streamURLDAO;
    @EJB
    private StreamUrlScheduleDAO streamUrlScheduleDAO;

    private final static int MAIN_SOURCE = 0;
    private final static int SECOND_SOURCE = 1;
    private final static int FALLBACK_INTERVAL_MINUTES = 10;

    @GET
    public final StreamSchedule getCurrentStreamSchedule() {
        List<Streamurl> streamList = null;
        List<Streamurlschedule> upcomingSchedule = null;

        try {
            streamList = streamURLDAO.getList();
        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (streamList == null || streamList.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        boolean mainSourceActive;
        try {
            mainSourceActive = streamUrlScheduleDAO.isMainSourceActive();
        } catch (DAOException e) {
            mainSourceActive = true;
        }

        Streamurl stream;
        if (mainSourceActive || streamList.size() == 1) {
            stream = streamList.get(MAIN_SOURCE);
        } else {
            stream = streamList.get(SECOND_SOURCE);
        }

        final Calendar now = Calendar.getInstance();
        Calendar time;

        try {
            upcomingSchedule = streamUrlScheduleDAO.getUpcomingSchedule(now);
        } catch (DAOException e) {
            upcomingSchedule = null;
        }

        if (upcomingSchedule != null && !upcomingSchedule.isEmpty()) {
            Streamurlschedule nextSchedule = upcomingSchedule.get(0);
            time = nextStreamURLChangeTime(now, nextSchedule);
        } else {
            time = (Calendar) now.clone();
            time.add(Calendar.MINUTE, FALLBACK_INTERVAL_MINUTES);
        }

        String name = stream.getName();
        String url = stream.getUrl();
        long unixTime = time.getTimeInMillis() / 1000;

        return new StreamSchedule(name, url, unixTime);
    }

    public static Calendar nextStreamURLChangeTime(final Calendar now,
            final Streamurlschedule nextSchedule) {

        Calendar result = (Calendar) now.clone();
        int fromCompareValue = TimeUtil
                .compare(now, nextSchedule.getFromtime());
        int toCompareValue = TimeUtil.compare(now, nextSchedule.getTotime());

        int dayDiff = (int) nextSchedule.getDay()
                - now.get(Calendar.DAY_OF_WEEK);

        if (dayDiff < 0 || (dayDiff == 0 && toCompareValue > 0)) {
            dayDiff += 7;
        }

        if (dayDiff > 0) {
            result.add(Calendar.DAY_OF_YEAR, dayDiff);
        }

        java.sql.Time sqlTime;

        if (dayDiff > 0 || (dayDiff == 0 && fromCompareValue < 0)) {
            sqlTime = nextSchedule.getFromtime();
        } else {
            sqlTime = nextSchedule.getTotime();
        }

        Calendar sqlTimeCalendar = Calendar.getInstance();
        sqlTimeCalendar.setTimeInMillis(sqlTime.getTime());

        result.set(Calendar.HOUR_OF_DAY,
                sqlTimeCalendar.get(Calendar.HOUR_OF_DAY));
        result.set(Calendar.MINUTE, sqlTimeCalendar.get(Calendar.MINUTE));
        result.set(Calendar.SECOND, sqlTimeCalendar.get(Calendar.SECOND));
        result.set(Calendar.MILLISECOND, 0);

        return result;
    }
}
