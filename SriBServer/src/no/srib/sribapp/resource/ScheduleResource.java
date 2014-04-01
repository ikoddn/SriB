package no.srib.sribapp.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    public List<Schedule> getAllSchedules() {
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
}
