package no.srib.sribapp.dao.jpa;

import javax.ejb.Stateless;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.DefinitionDAO;
import no.srib.sribapp.model.jpa.Definition;

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
