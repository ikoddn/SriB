package no.srib.sribapp.resource;

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
import no.srib.sribapp.dao.interfaces.ProgramnameDAO;
import no.srib.sribapp.dao.interfaces.ScheduleDAO;
import no.srib.sribapp.model.jpa.Programname;
import no.srib.sribapp.model.jpa.Schedule;
import no.srib.sribapp.resource.helper.ScheduleBean;

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
            Schedule schedule = scheduleDAO.getScheduleForTime(Calendar
                    .getInstance());

            if (schedule != null) {
                result = new ScheduleBean();
                
                int id = schedule.getProgram();
                Programname programName = programNameDAO.getById(id);
                String program = programName.getName();

                result.setDay(schedule.getDay());
                result.setProgram(program);
                result.setFromTime(schedule.getFromtime());
                result.setToTime(schedule.getTotime());
                result.setId(schedule.getId());
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
