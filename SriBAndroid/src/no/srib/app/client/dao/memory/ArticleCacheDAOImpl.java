package no.srib.app.client.dao.memory;

import java.util.List;

import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.model.Article;
import no.srib.app.client.model.CacheObject;

public enum ArticleCacheDAOImpl implements CacheObjectDAO<List<Article>> {
	INSTANCE;

	private CacheObject<List<Article>> cacheObject;

	private ArticleCacheDAOImpl() {
		cacheObject = null;
	}

	@Override
	public CacheObject<List<Article>> get() {
		return cacheObject;
	}

	@Override
	public void set(final CacheObject<List<Article>> cacheObject) {
		this.cacheObject = cacheObject;
	}
}
