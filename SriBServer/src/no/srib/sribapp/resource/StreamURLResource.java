package no.srib.sribapp.resource;

import java.util.Calendar;

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
@Produces(MediaType.APPLICATION_JSON)
@ManagedBean
public class StreamURLResource {
    private static int index = 1;

    @EJB
    private StreamurlDAO streamURLDAO;
    @EJB
    private StreamUrlScheduleDAO streamUrlScheduleDAO;

    @GET
    public final StreamSchedule getCurrentStreamSchedule() throws DAOException {        
        Streamurl stream = streamURLDAO.getById(index);
       // Streamurlschedule st = streamUrlScheduleDAO.getNext();
        //System.out.printf("%s %s",st.getFromtime(),st.getDay());
        System.out.println(streamUrlScheduleDAO.isMainSourceActive());
        if(index == 1){
            index = 11;
        }else{
            index = 1;
        }

        Calendar time = Calendar.getInstance();
        
        
        return new StreamSchedule(stream.getName(), stream.getUrl(), time);
    }
}
