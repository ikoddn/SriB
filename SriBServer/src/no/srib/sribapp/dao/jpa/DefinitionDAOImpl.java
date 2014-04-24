package no.srib.sribapp.dao.jpa;

import javax.ejb.Stateless;

import no.srib.sribapp.dao.interfaces.DefinitionDAO;
import no.srib.sribapp.model.Definition;

@Stateless
public class DefinitionDAOImpl extends AbstractModelDAOImpl<Definition>
        implements DefinitionDAO {

    public DefinitionDAOImpl() {
        super(Definition.class);
    }
}
