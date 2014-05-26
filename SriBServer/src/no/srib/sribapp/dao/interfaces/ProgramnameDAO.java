package no.srib.sribapp.dao.interfaces;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.exception.DuplicateEntryException;
import no.srib.sribapp.model.Programname;

public interface ProgramnameDAO extends AbstractModelDAO<Programname> {

    /**
     * Return element by id.
     * 
     * @param id
     *            - The id for the element.
     * @return The element.
     * @throws DAOException
     */
    Programname getById(int id) throws DAOException;

    /**
     * Get a sorted list of all programnames.
     * 
     * @return Sorted list of all programnames.
     * @throws DAOException
     */
    List<Programname> getSortedList() throws DAOException;

    /**
     * Add a {@link Programname} to the database.
     * 
     * @param programName
     *            - The {@link Programname} object.
     * @throws DAOException
     * @throws DuplicateEntryException
     *             When trying to add element that is already in database.
     */
    void addProgramName(Programname programName) throws DAOException,
            DuplicateEntryException;

    /**
     * Update {@link Programname} in database.
     * 
     * @param programName
     * @throws DAOException
     * @throws DuplicateEntryException
     *             When trying to update element to something that exist in
     *             database.
     */
    void updateProgramName(Programname programName) throws DAOException,
            DuplicateEntryException;
}
