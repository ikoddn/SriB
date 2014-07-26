package no.srib.app.client.asynctask;

import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.CacheObject;
import android.os.AsyncTask;

public abstract class CacheAwareAsyncTask<Params, Result> extends
		AsyncTask<Params, Result, Result> {

	private final CacheObjectDAO<Result> cacheDAO;

	protected CacheAwareAsyncTask(final CacheObjectDAO<Result> cacheDAO) {
		this.cacheDAO = cacheDAO;
	}

	@Override
	protected abstract void onProgressUpdate(Result... cacheResult);

	@Override
	protected abstract void onPostExecute(final Result result);

	@SuppressWarnings("unchecked")
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
				publishProgress(result);
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
