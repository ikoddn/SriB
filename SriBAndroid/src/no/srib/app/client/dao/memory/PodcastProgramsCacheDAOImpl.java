package no.srib.app.client.dao.memory;

import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.CacheObject;
import no.srib.app.client.model.PodcastPrograms;

public enum PodcastProgramsCacheDAOImpl
		implements
		CacheObjectDAO<PodcastPrograms> {
	INSTANCE;

	private CacheObject<PodcastPrograms> cacheObject;

	private PodcastProgramsCacheDAOImpl() {
		cacheObject = null;
	}

	@Override
	public CacheObject<PodcastPrograms> get() throws DAOException {
		return cacheObject;
	}

	@Override
	public void set(final CacheObject<PodcastPrograms> cacheObject)
			throws DAOException {
		this.cacheObject = cacheObject;
	}
}
