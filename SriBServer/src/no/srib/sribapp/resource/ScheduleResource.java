package no.srib.sribapp.resource;

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.ScheduleDAOImpl;
import no.srib.sribapp.dao.interfaces.ScheduleDAO;
import no.srib.sribapp.model.Schedule;

@Path("schedule")
@Produces(MediaType.APPLICATION_JSON)
public class ScheduleResource {
    
    private ScheduleDAO scheduleDAO;

    public ScheduleResource() {
        scheduleDAO = new ScheduleDAOImpl();
    }
    
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
    @Path("{day}")
    public final List<Schedule> getSchedulesForDay(@PathParam("day") final int day) {
        if (day < Calendar.SUNDAY || day > Calendar.SATURDAY) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        
        List<Schedule> list = null;
        
        try {
            list = scheduleDAO.getSchedulesForDay(day);
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
    public final Schedule getCurrentSchedule() {
        try {
            return scheduleDAO.getScheduleForTime(Calendar.getInstance());
        } catch (DAOException e) {
            e.printStackTrace();
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
    }
}
