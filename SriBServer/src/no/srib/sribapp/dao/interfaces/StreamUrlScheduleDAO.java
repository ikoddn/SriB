package no.srib.sribapp.dao.interfaces;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.Streamurlschedule;

public interface StreamUrlScheduleDAO extends
        AbstractModelDAO<Streamurlschedule> {
    
    List<Streamurlschedule> getbyDay(int day) throws DAOException;
    
    Streamurlschedule getNext() throws DAOException;
    
    boolean isMainSourceActive() throws DAOException;

}
