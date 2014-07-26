package no.srib.app.client.asynctask;

import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.CacheObject;
import android.os.AsyncTask;

public abstract class CacheAwareAsyncTask<Params, Result> extends
		AsyncTask<Params, Result, Result> {

	private final int cacheValidity;
	private final CacheObjectDAO<Result> cacheDAO;

	protected CacheAwareAsyncTask(final CacheObjectDAO<Result> cacheDAO,
			final int cacheValidity) {
		this.cacheDAO = cacheDAO;
		this.cacheValidity = cacheValidity;
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

			long diffSeconds = (System.currentTimeMillis() - cacheObject
					.getTimeMillis()) / 1000;

			if (diffSeconds > cacheValidity) {
				publishProgress(result);
				result = null;
			}
		}

		return result;
	}

	protected void cache(final Result result) {
		final long now = System.currentTimeMillis();

		try {
			cacheDAO.set(new CacheObject<Result>(result, now));
		} catch (DAOException e) {
		}
	}
}
