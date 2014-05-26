package no.srib.sribapp.dao.interfaces;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.AbstractModel;

/**
 * A Data Access Object for generic types. It should not be used directly, you
 * should rather make a specialization based upon this.
 * 
 * @param <T>
 *            - the type of the data.
 */
public interface AbstractModelDAO<T extends AbstractModel> {

    /**
     * Gets the element with the given ID.
     * 
     * @param id
     * @return
     * @throws DAOException
     */
    T getById(final int id) throws DAOException;

    /**
     * Get a list of all the entities in the model of the given type.
     * 
     * @return A list of entities.
     * @throws DAOException
     */
    List<T> getList() throws DAOException;

    /**
     * Add a entity to the database.
     * 
     * @param element
     *            - Element to be inserted.
     * @throws DAOException
     */

    void add(T element) throws DAOException;

    /**
     * Update entity in database.
     * 
     * @param element
     *            - Element to be updated.
     * @throws DAOException
     */

    void update(T element) throws DAOException;

    /**
     * Remove element from database.
     * 
     * @param element
     *            - Element to be removed.
     * @throws DAOException
     */
    void remove(T element) throws DAOException;
}
