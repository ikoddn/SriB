package no.srib.sribapp.dao.hibernate;

import no.srib.sribapp.dao.interfaces.DefinitionDAO;
import no.srib.sribapp.model.Definition;

public class DefinitionDAOImpl extends AbstractModelDAOImpl<Definition> implements
        DefinitionDAO {

    public DefinitionDAOImpl() {
        super(Definition.class);
      
    }
    
    

}
