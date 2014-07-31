package no.srib.app.client.event.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import no.srib.app.client.adapter.ListBasedAdapter;
import no.srib.app.client.asynctask.ArticleAsyncTask;
import no.srib.app.client.event.listener.OnSearchListener;
import no.srib.app.client.model.Article;
import android.content.Context;

public class ArticleSearchHandler implements OnSearchListener {

	private final Context context;
	private final ListBasedAdapter<Article> adapter;

	public ArticleSearchHandler(final Context context,
			final ListBasedAdapter<Article> adapter) {
		this.context = context;
		this.adapter = adapter;
	}

	@Override
	public void onSearch(final String query) {
		try {
			String urlEncodedQuery = URLEncoder.encode(query, "UTF-8");
			new ArticleAsyncTask(context, adapter).execute(urlEncodedQuery);
		} catch (UnsupportedEncodingException e) {
		}
	}

	@Override
	public void restorePreSearchData() {
		new ArticleAsyncTask(context, adapter).execute();
	}
}
