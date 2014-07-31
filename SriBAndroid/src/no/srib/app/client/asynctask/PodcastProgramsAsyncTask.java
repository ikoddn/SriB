package no.srib.app.client.asynctask;

import java.util.LinkedList;
import java.util.List;

import no.srib.app.client.R;
import no.srib.app.client.adapter.ProgramSpinnerAdapter;
import no.srib.app.client.dao.PodcastProgramsDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.dao.memory.PodcastProgramsCacheDAOImpl;
import no.srib.app.client.dao.retrofit.PodcastProgramsDAOImpl;
import no.srib.app.client.model.PodcastPrograms;
import no.srib.app.client.model.ProgramName;
import no.srib.app.client.util.NetworkUtil;
import android.content.Context;
import android.util.Log;

public class PodcastProgramsAsyncTask extends
		CacheAwareAsyncTask<Void, Void, PodcastPrograms> {

	private static final int CACHE_VALIDITY_SECONDS = 3600;

	private final Context context;
	private final ProgramSpinnerAdapter adapter;

	private Exception exception;
	private PodcastPrograms cacheResult;

	public PodcastProgramsAsyncTask(final Context context,
			final ProgramSpinnerAdapter adapter) {

		super(PodcastProgramsCacheDAOImpl.INSTANCE);

		this.context = context;
		this.adapter = adapter;
		exception = null;
	}

	@Override
	protected PodcastPrograms doInBackground(final Void... params) {
		PodcastPrograms result = checkCache();

		if (result == null && NetworkUtil.networkAvailable(context)) {
			String restApiUrl = context.getResources().getString(
					R.string.url_restapi);

			PodcastProgramsDAO dao = new PodcastProgramsDAOImpl(restApiUrl);

			try {
				result = dao.get();
			} catch (DAOException e) {
				this.exception = e;
			}

			if (result != null) {
				long expiration = System.currentTimeMillis()
						+ CACHE_VALIDITY_SECONDS * 1000;
				cache(result, expiration);
			}
		}

		return result;
	}

	@Override
	protected void onExpiredCache(final PodcastPrograms cacheResult) {
		this.cacheResult = cacheResult;
		publishProgress();
	}

	@Override
	protected void onProgressUpdate(final Void... values) {
		updateAdapter(cacheResult);
	}

	@Override
	protected void onPostExecute(final PodcastPrograms result) {
		if (exception != null) {
			Log.d("SriB",
					"PodcastProgramsAsyncTask onPostExecute(): exception != null");
			exception.printStackTrace();
		} else if (result == null) {
			Log.d("SriB",
					"PodcastProgramsAsyncTask onPostExecute(): result == null");
		} else {
			updateAdapter(result);
		}
	}

	private void updateAdapter(final PodcastPrograms podcastPrograms) {
		List<ProgramName> result = new LinkedList<ProgramName>();

		List<ProgramName> newer = podcastPrograms.getNewer();

		result.addAll(newer);
		result.addAll(podcastPrograms.getOlder());

		adapter.setNewerCount(newer.size());
		adapter.setList(result);
		adapter.notifyDataSetChanged();
	}
}
