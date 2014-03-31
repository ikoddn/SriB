package no.srib.sribapp.dao.interfaces;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.Streamurl;

/**
 * A Data Access Object for Streamurl entities.
 * 
 * @author Sveinung
 * 
 */
public interface StreamurlDAO extends AbstractModelDAO<Streamurl> {

    
    public Streamurl getById(int id) throws DAOException;
}


