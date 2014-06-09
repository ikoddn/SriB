package no.srib.sribapp.resource;

import java.sql.Time;
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

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.jpa.Streamurl;
import no.srib.sribapp.model.jpa.Streamurlschedule;
import no.srib.sribapp.resource.helper.StreamSchedule;

@Path("radiourl")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@ManagedBean
public class StreamURLResource {

    @EJB
    private StreamurlDAO streamURLDAO;
    @EJB
    private StreamUrlScheduleDAO streamUrlScheduleDAO;

    private final int MAIN_SOURCE = 0;
    private final int SECOND_SOURCE = 1;
    private final int FALLBACK_INTERVAL_MINUTES = 10;

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
        Calendar time = Calendar.getInstance();

        try {
            upcomingSchedule = streamUrlScheduleDAO.getUpcomingSchedule(now);
        } catch (DAOException e) {
            upcomingSchedule = null;
        }

        if (upcomingSchedule != null && !upcomingSchedule.isEmpty()) {
            Streamurlschedule nextSchedule = upcomingSchedule.get(0);

            Time hourAndMinutes;
            if (nextSchedule.getFromtime().after(now.getTime())) {
                hourAndMinutes = nextSchedule.getFromtime();
            } else {
                hourAndMinutes = nextSchedule.getTotime();
            }

            time.setTimeInMillis(hourAndMinutes.getTime());

            time.set(Calendar.YEAR, now.get(Calendar.YEAR));
            time.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR));

            if (now.get(Calendar.HOUR_OF_DAY) > time.get(Calendar.HOUR_OF_DAY)) {
                time.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else {
            time.add(Calendar.MINUTE, FALLBACK_INTERVAL_MINUTES);
        }

        String name = stream.getName();
        String url = stream.getUrl();
        long timeInMs = time.getTimeInMillis();

        return new StreamSchedule(name, url, timeInMs);
    }
}
