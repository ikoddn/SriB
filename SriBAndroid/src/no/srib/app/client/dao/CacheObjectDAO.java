package no.srib.app.client.dao;

import no.srib.app.client.dao.exception.DAOException;

public interface CacheObjectDAO<T> {

	CacheObject<T> get() throws DAOException;

	void set(CacheObject<T> articleCache) throws DAOException;
}
