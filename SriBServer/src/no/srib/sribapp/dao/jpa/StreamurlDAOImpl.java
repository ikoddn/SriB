package no.srib.sribapp.dao.jpa;

import javax.ejb.Stateless;

import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.jpa.Streamurl;

@Stateless
public class StreamurlDAOImpl extends AbstractModelDAOImpl<Streamurl> implements
        StreamurlDAO {

    public StreamurlDAOImpl() {
        super(Streamurl.class);
    }
}
