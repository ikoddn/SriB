package no.srib.app.client.asynctask;

import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.CacheObject;
import android.os.AsyncTask;

public abstract class CacheAwareAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {

	private final CacheObjectDAO<Result> cacheDAO;

	protected CacheAwareAsyncTask(final CacheObjectDAO<Result> cacheDAO) {
		this.cacheDAO = cacheDAO;
	}

	protected abstract void onExpiredCache(final Result cacheResult);

	@Override
	protected abstract void onPostExecute(final Result result);

	protected Result checkCache() {
		Result result = null;
		CacheObject<Result> cacheObject;

		try {
			cacheObject = cacheDAO.get();
		} catch (DAOException e) {
			cacheObject = null;
		}

		if (cacheObject != null) {
			result = cacheObject.getData();

			if (System.currentTimeMillis() > cacheObject.getExpirationTime()) {
				onExpiredCache(result);
				result = null;
			}
		}

		return result;
	}

	protected void cache(final Result result, final long expirationTime) {
		try {
			cacheDAO.set(new CacheObject<Result>(result, expirationTime));
		} catch (DAOException e) {
		}
	}
}
