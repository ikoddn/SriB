package no.srib.app.server.dao.jpa;

import javax.ejb.Stateless;

import no.srib.app.server.dao.interfaces.StreamurlDAO;
import no.srib.app.server.model.jpa.Streamurl;

@Stateless
public class StreamurlDAOImpl extends AbstractModelDAOImpl<Streamurl> implements
        StreamurlDAO {

    public StreamurlDAOImpl() {
        super(Streamurl.class);
    }
}
