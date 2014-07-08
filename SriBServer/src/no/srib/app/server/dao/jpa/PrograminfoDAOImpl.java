package no.srib.app.server.dao.jpa;

import javax.ejb.Stateless;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.PrograminfoDAO;
import no.srib.app.server.model.jpa.Programinfo;

@Stateless
public class PrograminfoDAOImpl extends AbstractModelDAOImpl<Programinfo>
        implements PrograminfoDAO {

    public PrograminfoDAOImpl() {
        super(Programinfo.class);
    }

    @Override
    public void add(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void update(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }

    @Override
    public void remove(Programinfo el) throws DAOException {
        throw new DAOException("Not supported");
    }
}
