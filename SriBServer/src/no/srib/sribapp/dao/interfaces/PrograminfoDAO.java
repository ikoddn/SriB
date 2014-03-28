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

    Programinfo getById(final int id) throws DAOException;
}
