package no.srib.app.client.dao.memory;

import java.util.List;

import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.model.CacheObject;
import no.srib.app.client.model.Podcast;

public enum PodcastCacheDAOImpl implements CacheObjectDAO<List<Podcast>> {
	INSTANCE;

	private CacheObject<List<Podcast>> cacheObject;

	private PodcastCacheDAOImpl() {
		cacheObject = null;
	}

	@Override
	public CacheObject<List<Podcast>> get() {
		return cacheObject;
	}

	@Override
	public void set(final CacheObject<List<Podcast>> cacheObject) {
		this.cacheObject = cacheObject;
	}
}
