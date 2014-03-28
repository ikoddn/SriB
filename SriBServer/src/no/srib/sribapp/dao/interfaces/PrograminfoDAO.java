package no.srib.sribapp.dao.interfaces;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.Programinfo;

/**
 * A Data Access Object for Programinfo entities.
 * 
 * @author Sveinung
 * 
 */
public interface PrograminfoDAO extends AbstractModelDAO<Programinfo> {

    /**
     * Returns a Program with the given ID, or {@code null} if the Program
     * doesn't exist.
     * 
     * @param id
     * @return
     * @throws DAOException
     */
    Programinfo getById(final int id) throws DAOException;
}
