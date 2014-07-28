package no.srib.app.client.dao;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.StreamSchedule;

public interface StreamScheduleDAO {

	StreamSchedule get() throws DAOException;
}
