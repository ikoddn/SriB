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
import no.srib.app.server.dao.interfaces.ProgramnameDAO;
import no.srib.app.server.dao.interfaces.ScheduleDAO;
import no.srib.app.server.model.jpa.Programname;
import no.srib.app.server.model.jpa.Schedule;
import no.srib.app.server.model.json.ScheduleBean;
import no.srib.app.server.util.TimeUtil;

@Path("/schedule")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@ManagedBean
public class ScheduleResource {

    @EJB
    private ScheduleDAO scheduleDAO;
    @EJB
    private ProgramnameDAO programNameDAO;

    @GET
    public final List<Schedule> getAllSchedules() {
        List<Schedule> list = null;

        try {
            list = scheduleDAO.getList();
        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (list == null || list.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        return list;
    }

    @GET
    @Path("now")
    public final ScheduleBean getCurrentSchedule() {
        ScheduleBean result = null;

        try {
            Calendar calendar = Calendar.getInstance();

            Schedule schedule = scheduleDAO.getScheduleForTime(calendar);

            if (schedule != null) {
                Programname programName = programNameDAO.getById(schedule
                        .getProgram());

                TimeUtil.setDayOfWeekAndTimeGoForward(calendar,
                        schedule.getDay(), schedule.getTotime());

                String program = programName.getName();
                long unixTime = calendar.getTimeInMillis() / 1000;
                result = new ScheduleBean(program, unixTime);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (result == null) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        return result;
    }
}
