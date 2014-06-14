package no.srib.app.server.dao.interfaces;

import java.util.List;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.model.jpa.Programname;

public interface ProgramnameDAO extends AbstractModelDAO<Programname> {

    /**
     * Get a sorted list of all programnames.
     * 
     * @return Sorted list of all programnames.
     * @throws DAOException
     */
    List<Programname> getSortedList() throws DAOException;

}
