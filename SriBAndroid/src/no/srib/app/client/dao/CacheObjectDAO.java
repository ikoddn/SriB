package no.srib.app.client.dao;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.CacheObject;

public interface CacheObjectDAO<T> {

	CacheObject<T> get() throws DAOException;

	void set(CacheObject<T> cacheObject) throws DAOException;
}
