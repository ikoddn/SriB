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
     * Get a list of all the entities in the model of the given type.
     * 
     * @return A list of entities.
     * @throws DAOException
     */
    List<T> getList() throws DAOException;

    /**
     * Add a entity to the database.
     * 
     * @param el
     *            - Element to be inserted.
     * @throws DAOException
     */

    void addElement(T el) throws DAOException;

    /**
     * Update entity in database.
     * 
     * @param el
     *            - Element to be updated.
     * @throws DAOException
     */

    void updateElement(T el) throws DAOException;

    /**
     * Remove element from database.
     * 
     * @param el
     *            - Element to be removed.
     * @throws DAOException
     */
    void removeElement(T el) throws DAOException;
}
