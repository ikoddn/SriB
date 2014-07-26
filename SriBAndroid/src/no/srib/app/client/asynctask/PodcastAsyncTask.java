package no.srib.app.client.asynctask;

import java.util.List;

import no.srib.app.client.R;
import no.srib.app.client.adapter.ListBasedAdapter;
import no.srib.app.client.dao.PodcastDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.dao.memory.PodcastCacheDAOImpl;
import no.srib.app.client.dao.retrofit.PodcastDAOImpl;
import no.srib.app.client.model.Podcast;
import android.content.Context;
import android.util.Log;

public class PodcastAsyncTask extends
		CacheAwareAsyncTask<Integer, List<Podcast>> {

	private static final int CACHE_VALIDITY_SECONDS = 3600;

	private final Context context;
	private final ListBasedAdapter<Podcast> adapter;

	private Exception exception;

	public PodcastAsyncTask(final Context context,
			final ListBasedAdapter<Podcast> adapter) {

		super(PodcastCacheDAOImpl.INSTANCE, CACHE_VALIDITY_SECONDS);

		this.context = context;
		this.adapter = adapter;
		exception = null;
	}

	@Override
	protected List<Podcast> doInBackground(final Integer... programIds) {
		List<Podcast> result = null;
		Integer programId = null;

		if (programIds != null && programIds.length > 0) {
			programId = programIds[0];
		} else {
			result = checkCache();
		}

		if (result == null) {
			String restApiUrl = context.getResources().getString(
					R.string.url_restapi);

			PodcastDAO dao = new PodcastDAOImpl(restApiUrl);

			if (programId == null) {
				try {
					result = dao.getList();
				} catch (DAOException e) {
					this.exception = e;
				}

				if (result != null) {
					cache(result);
				}
			} else {
				try {
					result = dao.getListFromProgram(programId);
				} catch (DAOException e) {
					this.exception = e;
				}
			}
		}

		return result;
	}

	@Override
	protected void onProgressUpdate(final List<Podcast>... cacheResult) {
		adapter.setList(cacheResult[0]);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onPostExecute(final List<Podcast> result) {
		if (exception != null) {
			Log.d("SriB", "PodcastAsyncTask onPostExecute(): exception != null");
			exception.printStackTrace();
		} else if (result == null) {
			Log.d("SriB", "PodcastAsyncTask onPostExecute(): result == null");
		} else {
			adapter.setList(result);
			adapter.notifyDataSetChanged();
		}
	}
}
