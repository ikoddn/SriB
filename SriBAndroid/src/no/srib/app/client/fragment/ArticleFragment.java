package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.EmbeddedWebViewClient;
import no.srib.app.client.util.ProgressWebChromeClient;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArticleFragment extends BaseFragment {

	@InjectView(R.id.progressbar_article) ProgressBar progressBar;
	@InjectView(R.id.webview_article) WebView webView;

	private String url;

	public static ArticleFragment newInstance(String url) {
		ArticleFragment fragment = new ArticleFragment();
		fragment.setUrl(url);

		return fragment;
	}

	public ArticleFragment() {
		url = null;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public View onBaseCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_article, container,
				false);
		ButterKnife.inject(this, rootView);

		if (url != null) {
			webView.setWebViewClient(new EmbeddedWebViewClient());
			webView.setWebChromeClient(new ProgressWebChromeClient(progressBar));
			webView.loadUrl(url);
		}

		return rootView;
	}
}
