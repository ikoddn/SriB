package no.srib.sribapp.dao.interfaces;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.jpa.Programname;

public interface ProgramnameDAO extends AbstractModelDAO<Programname> {

    /**
     * Get a sorted list of all programnames.
     * 
     * @return Sorted list of all programnames.
     * @throws DAOException
     */
    List<Programname> getSortedList() throws DAOException;

}
