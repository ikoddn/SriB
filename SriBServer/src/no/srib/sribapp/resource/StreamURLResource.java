package no.srib.sribapp.resource;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Streamurl;
import no.srib.sribapp.model.Streamurlschedule;
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

    @GET
    public final StreamSchedule getCurrentStreamSchedule() throws DAOException {
        List<Streamurl> streamList = streamURLDAO.getList();
        Calendar now = Calendar.getInstance();

        List<Streamurlschedule> upcomingSchedule = streamUrlScheduleDAO
                .getUpcomingSchedule(now);
        Streamurlschedule nextSchedule = upcomingSchedule.get(0);

        Time hourAndMinutes;
        if (nextSchedule.getFromtime().after(now.getTime())) {
            hourAndMinutes = nextSchedule.getFromtime();
        } else {
            hourAndMinutes = nextSchedule.getTotime();
        }

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(hourAndMinutes.getTime());

        time.set(Calendar.YEAR, now.get(Calendar.YEAR));
        time.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR));

        if (now.get(Calendar.HOUR_OF_DAY) > time.get(Calendar.HOUR_OF_DAY)) {
            time.add(Calendar.DAY_OF_MONTH, 1);
        }

        Streamurl stream;
        if (streamUrlScheduleDAO.isMainSourceActive()) {
            stream = streamList.get(MAIN_SOURCE);
        } else {
            stream = streamList.get(SECOND_SOURCE);
        }

        String name = stream.getName();
        String url = stream.getUrl();
        long timeInMs = time.getTimeInMillis();

        return new StreamSchedule(name, url, timeInMs);
    }
}
