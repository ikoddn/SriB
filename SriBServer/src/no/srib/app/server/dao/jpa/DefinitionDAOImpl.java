package no.srib.app.server.dao.jpa;

import javax.ejb.Stateless;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.DefinitionDAO;
import no.srib.app.server.model.jpa.Definition;

@Stateless
public class DefinitionDAOImpl extends AbstractModelDAOImpl<Definition>
        implements DefinitionDAO {

    public DefinitionDAOImpl() {
        super(Definition.class);
    }

    @Override
    public void add(Definition el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void update(Definition el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void remove(Definition el) throws DAOException {
        throw new DAOException("Not supported");
    }
}
