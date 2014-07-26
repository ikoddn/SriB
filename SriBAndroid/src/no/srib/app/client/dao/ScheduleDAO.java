package no.srib.app.client.dao;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.Schedule;

public interface ScheduleDAO {

	Schedule get() throws DAOException;
}
