package no.srib.sribapp.dao.interfaces;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.model.AbstractModel;

public interface AbstractModelDAO<T extends AbstractModel> {

    /**
     * Get a list of all the entities in the model of the given type.
     * 
     * @return A list of entities.
     * @throws DAOException
     */
    List<T> getList() throws DAOException;
}
