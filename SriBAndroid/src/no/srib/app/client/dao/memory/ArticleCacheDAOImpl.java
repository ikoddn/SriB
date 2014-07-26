package no.srib.app.client.dao.memory;

import java.util.List;

import no.srib.app.client.dao.CacheObject;
import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.model.Article;

public enum ArticleCacheDAOImpl implements CacheObjectDAO<List<Article>> {
	INSTANCE;

	private CacheObject<List<Article>> articleCache;

	private ArticleCacheDAOImpl() {
		articleCache = null;
	}

	@Override
	public CacheObject<List<Article>> get() {
		return articleCache;
	}

	@Override
	public void set(final CacheObject<List<Article>> articleCache) {
		this.articleCache = articleCache;
	}
}
