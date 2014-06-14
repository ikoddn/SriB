package no.srib.app.server.dao.interfaces;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.model.jpa.Backenduser;

public interface BackendUserDAO extends AbstractModelDAO<Backenduser> {

    /**
     * @throws DAOException
     *             Returns a backenduser from given username, or {@code null} if
     *             no backenduser match.
     * @param username
     * @return
     * 
     */
    Backenduser getByUserName(final String username) throws DAOException;

}
