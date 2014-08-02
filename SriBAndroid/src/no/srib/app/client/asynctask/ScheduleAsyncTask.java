package no.srib.app.client.asynctask;

import no.srib.app.client.R;
import no.srib.app.client.dao.ScheduleDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.dao.memory.ScheduleCacheDAOImpl;
import no.srib.app.client.dao.retrofit.ScheduleDAOImpl;
import no.srib.app.client.model.Schedule;
import no.srib.app.client.util.NetworkUtil;
import android.content.Context;
import android.util.Log;

public class ScheduleAsyncTask extends
		CacheAwareAsyncTask<Void, Void, Schedule> {

	private final Context context;
	private final Schedule schedule;

	private Exception exception;

	public ScheduleAsyncTask(final Context context, final Schedule schedule) {
		super(ScheduleCacheDAOImpl.INSTANCE);

		this.context = context;
		this.schedule = schedule;
	}

	@Override
	protected Schedule doInBackground(final Void... params) {
		Schedule result = checkCache();

		if (result == null && NetworkUtil.networkAvailable(context)) {
			String restApiUrl = context.getResources().getString(
					R.string.url_restapi);

			ScheduleDAO dao = new ScheduleDAOImpl(restApiUrl);

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
	protected void onExpiredCache(final Schedule cacheResult) {
	}

	@Override
	protected void onPostExecute(final Schedule result) {
		if (exception != null) {
			// TODO display user friendly message
			// textView.setText("Error");
			Log.d("SriB",
					"ScheduleAsyncTask onPostExecute(): exception != null");
			exception.printStackTrace();
		} else if (result == null) {
			// TODO display StreamSchedule name?
			schedule.setProgram("");
			Log.d("SriB", "ScheduleAsyncTask onPostExecute(): result == null");
		} else {
			schedule.setProgram(result.getProgram());
		}
	}
}
