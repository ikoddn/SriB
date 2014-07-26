package no.srib.app.client.asynctask;

import java.util.List;

import no.srib.app.client.R;
import no.srib.app.client.adapter.ListBasedAdapter;
import no.srib.app.client.dao.ArticleDAO;
import no.srib.app.client.dao.CacheObject;
import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.dao.memory.ArticleCacheDAOImpl;
import no.srib.app.client.dao.retrofit.ArticleDAOImpl;
import no.srib.app.client.model.Article;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ArticleAsyncTask extends
		AsyncTask<String, List<Article>, List<Article>> {

	private static final int CACHE_VALIDITY_SECONDS = 3600;

	private final Context context;
	private final ListBasedAdapter<Article> adapter;

	private Exception exception;

	public ArticleAsyncTask(final Context context,
			final ListBasedAdapter<Article> adapter) {

		this.context = context;
		this.adapter = adapter;
		exception = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Article> doInBackground(final String... queries) {
		List<Article> result = null;
		String query = null;
		CacheObjectDAO<List<Article>> cacheDAO = ArticleCacheDAOImpl.INSTANCE;

		if (queries != null && queries.length > 0) {
			// Searching
			query = queries[0];
		} else {
			// Not searching, we can check the cache
			CacheObject<List<Article>> articleCache;

			try {
				articleCache = cacheDAO.get();
			} catch (DAOException e) {
				articleCache = null;
			}

			if (articleCache != null) {
				result = articleCache.getData();

				long diffSeconds = (System.currentTimeMillis() - articleCache
						.getTimeMillis()) / 1000;

				if (diffSeconds > CACHE_VALIDITY_SECONDS) {
					publishProgress(result);
					result = null;
				}
			}
		}

		if (result == null) {
			String restApiUrl = context.getResources().getString(
					R.string.url_restapi);

			ArticleDAO articleDAO = new ArticleDAOImpl(restApiUrl);

			try {
				result = articleDAO.getList(query);
			} catch (DAOException e) {
				this.exception = e;
			}

			// If query == null then the result is not a search result
			if (query == null && result != null) {
				final long now = System.currentTimeMillis();

				try {
					cacheDAO.set(new CacheObject<List<Article>>(result, now));
				} catch (DAOException e) {
				}
			}
		}

		return result;
	}

	@Override
	protected void onProgressUpdate(final List<Article>... cacheResult) {
		adapter.setList(cacheResult[0]);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onPostExecute(final List<Article> result) {
		if (exception != null) {
			Log.d("SriB", "ArticleAsyncTask onPostExecute(): exception != null");
			exception.printStackTrace();
		} else if (result == null) {
			// 204
			Log.d("SriB", "ArticleAsyncTask onPostExecute(): result == null");
		} else {
			adapter.setList(result);
			adapter.notifyDataSetChanged();
		}
	}
}
