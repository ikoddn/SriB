package no.srib.app.client.http;

import no.srib.app.client.R;
import no.srib.app.client.adapter.updater.AdapterUpdater;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.Article;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.widget.Toast;

public class ArticleHttpResponse implements HttpResponseListener {

	private boolean searching;
	private AdapterUpdater<Article, String> updater;
	private Context context;

	public ArticleHttpResponse(final Context context,
			final AdapterUpdater<Article, String> updater,
			final boolean searching) {

		this.context = context;
		this.updater = updater;
		this.searching = searching;
	}

	@Override
	public void onResponse(final int statusCode, final String response) {
		switch (statusCode) {
		case HttpStatus.SC_OK:
			updater.updateFrom(response, !searching);
			break;
		case HttpStatus.SC_NO_CONTENT:
			Toast.makeText(context, R.string.toast_noContent,
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
