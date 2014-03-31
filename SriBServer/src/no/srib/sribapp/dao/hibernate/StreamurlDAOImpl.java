package no.srib.sribapp.dao.hibernate;

import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Streamurl;

public class StreamurlDAOImpl extends AbstractModelDAOImpl<Streamurl> implements
        StreamurlDAO {

    protected StreamurlDAOImpl() {
        super(Streamurl.class);
    }

}
