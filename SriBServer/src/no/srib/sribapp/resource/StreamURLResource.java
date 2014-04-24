package no.srib.sribapp.resource;

import java.util.Calendar;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Streamurl;
import no.srib.sribapp.resource.helper.StreamSchedule;

@Path("radiourl")
@Produces(MediaType.APPLICATION_JSON)
@ManagedBean
public class StreamURLResource {
    private static int index = 0;

    @EJB
    private StreamurlDAO streamURLDAO;

    @GET
    public final StreamSchedule getCurrentStreamSchedule() throws DAOException {        
        Streamurl stream = streamURLDAO.getById(index);
        index = (index + 1) % 2;

        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 14);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        
        return new StreamSchedule(stream.getName(), stream.getUrl(), time);
    }
}
