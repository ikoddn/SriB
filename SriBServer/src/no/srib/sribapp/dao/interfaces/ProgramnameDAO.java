package no.srib.sribapp.dao.interfaces;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.Programname;

public interface ProgramnameDAO extends AbstractModelDAO<Programname> {

    /**
     * Return element by id.
     * @param id - The id for the element.
     * @return The element.
     * @throws DAOException
     */
    
    Programname getById(int id) throws DAOException;
    
    
}
