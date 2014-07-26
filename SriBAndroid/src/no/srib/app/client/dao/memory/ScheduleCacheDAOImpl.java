package no.srib.app.client.dao.memory;

import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.model.CacheObject;
import no.srib.app.client.model.Schedule;

public enum ScheduleCacheDAOImpl implements CacheObjectDAO<Schedule> {
	INSTANCE;

	private CacheObject<Schedule> cacheObject;

	private ScheduleCacheDAOImpl() {
		cacheObject = null;
	}

	@Override
	public CacheObject<Schedule> get() {
		return cacheObject;
	}

	@Override
	public void set(final CacheObject<Schedule> cacheObject) {
		this.cacheObject = cacheObject;
	}
}
