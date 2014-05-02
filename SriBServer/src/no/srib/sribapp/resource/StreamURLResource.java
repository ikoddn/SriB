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
@Produces(MediaType.APPLICATION_JSON)
@ManagedBean
public class StreamURLResource {
    private static int index = 1;

    @EJB
    private StreamurlDAO streamURLDAO;
    @EJB
    private StreamUrlScheduleDAO streamUrlScheduleDAO;
    
    
    private final int MAIN_SOURCE = 0;
    private final int SECOND_SOURCE = 1;
    

    @GET
    public final StreamSchedule getCurrentStreamSchedule() throws DAOException {        
        
        List<Streamurl> streamList = streamURLDAO.getList();
        Streamurl stream = null;
        Streamurlschedule stuc = null; 
        String name = null;
        String url = null;
        Calendar time = Calendar.getInstance();
        //TODO HER MÅ DET LEGGAST INN LOGIKK FOR Å GI UT RETT VERDI TIL KLIENT
        if(false) {
           time.add(Calendar.DAY_OF_YEAR, 1);
        }
        long timeInMs = time.getTimeInMillis() + 30000;
        System.out.println(streamUrlScheduleDAO.isMainSourceActive());
        if(streamUrlScheduleDAO.isMainSourceActive()){
            stream = streamList.get(MAIN_SOURCE);
            
            name = stream.getName();
            url = stream.getUrl();
       
            return new StreamSchedule(name, url, timeInMs);
        }else{
            stream = streamList.get(SECOND_SOURCE);
            name = stream.getName();
            url = stream.getUrl();
         
            
            
            return new StreamSchedule(name, url, timeInMs);
        }


       

        

    }
}
