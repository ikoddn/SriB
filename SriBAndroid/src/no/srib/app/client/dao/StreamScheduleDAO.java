package no.srib.app.client.dao;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.StreamSchedule;

public interface StreamScheduleDAO {

	/**
	 * Gets the persisted {@link StreamSchedule} object.
	 * 
	 * @return the {@code StreamSchedule} object, or {@code null} if none has
	 *         previously been persisted.
	 * @throws DAOException
	 */
	StreamSchedule get() throws DAOException;

	/**
	 * Persists the given {@link StreamSchedule} object, replacing any
	 * previously persisted object.
	 * 
	 * @param streamSchedule
	 *            - the {@code StreamSchedule} object to persist.
	 * @throws DAOException
	 */
	void set(StreamSchedule streamSchedule) throws DAOException;
}
