package no.srib.app.client.dao;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.CacheObject;

public interface CacheObjectDAO<T> {

	/**
	 * Gets the persisted {@code CacheObject}.
	 * 
	 * @return the {@code CacheObject}, or {@code null} if none has previously
	 *         been persisted.
	 * @throws DAOException
	 */
	CacheObject<T> get() throws DAOException;

	/**
	 * Persists the given {@code CacheObject}, replacing any previously
	 * persisted object.
	 * 
	 * @param cacheObject
	 *            - the {@code CacheObject} to persist.
	 * @throws DAOException
	 */
	void set(CacheObject<T> cacheObject) throws DAOException;
}
