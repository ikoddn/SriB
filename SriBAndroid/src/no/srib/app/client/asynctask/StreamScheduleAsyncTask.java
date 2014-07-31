package no.srib.app.client.asynctask;

import no.srib.app.client.R;
import no.srib.app.client.dao.StreamScheduleDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.dao.retrofit.StreamScheduleDAOImpl;
import no.srib.app.client.dao.sharedpreferences.StreamScheduleCacheDAOImpl;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.StreamUpdaterService;
import no.srib.app.client.util.NetworkUtil;
import android.content.Context;
import android.util.Log;

public class StreamScheduleAsyncTask extends
		CacheAwareAsyncTask<Void, Void, StreamSchedule> {

	private final Context context;
	private final StreamUpdaterService streamUpdater;

	private Exception exception;

	public StreamScheduleAsyncTask(final Context context,
			final StreamUpdaterService streamUpdater) {
		super(new StreamScheduleCacheDAOImpl(context));

		this.context = context;
		this.streamUpdater = streamUpdater;
		exception = null;
	}

	@Override
	protected StreamSchedule doInBackground(final Void... params) {
		StreamSchedule result = checkCache();

		if (result == null && NetworkUtil.networkAvailable(context)) {
			String restApiUrl = context.getResources().getString(
					R.string.url_restapi);

			StreamScheduleDAO dao = new StreamScheduleDAOImpl(restApiUrl);

			try {
				result = dao.get();
			} catch (DAOException e) {
				this.exception = e;
			}

			if (result != null) {
				long expiration = result.getTime() * 1000;
				cache(result, expiration);
			}
		}

		return result;
	}

	@Override
	protected void onExpiredCache(final StreamSchedule cacheResult) {
		publishProgress();
	}

	@Override
	protected void onProgressUpdate(final Void... values) {
		// TODO "connecting..."
	}

	@Override
	protected void onPostExecute(final StreamSchedule result) {
		if (exception != null) {
			Log.d("SriB",
					"StreamScheduleAsyncTask onPostExecute(): exception != null");
			exception.printStackTrace();
			streamUpdater.onUpdateFailed();
		} else if (result == null) {
			Log.d("SriB",
					"StreamScheduleAsyncTask onPostExecute(): result == null");
			streamUpdater.onUpdateFailed();
		} else {
			streamUpdater.onUpdateSuccess(result);
		}
	}
}
