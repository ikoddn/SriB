package no.srib.sribapp.dao.interfaces;

import java.util.Calendar;
import java.util.List;

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
     * Returns a List with {@link Programinfo} with minimum one podcast in
     * database and is created after or before the given Calendar object.
     * 
     * @param cal
     * @param beforeDate
     * @return
     * @throws DAOException
     */
    List<Programinfo> getProgramInfosWithPodcast(Calendar cal, boolean afterDate)
            throws DAOException;

}
