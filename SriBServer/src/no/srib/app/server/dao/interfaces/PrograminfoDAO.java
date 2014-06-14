package no.srib.app.server.dao.interfaces;

import java.util.Calendar;
import java.util.List;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.model.jpa.Programinfo;

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
