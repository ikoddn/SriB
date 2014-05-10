package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ArticleFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_article, container,
				false);

		TextView textView = (TextView) rootView
				.findViewById(R.id.textview_article);
		textView.setText("ArticleFragment");

		WebView webView = (WebView) rootView.findViewById(R.id.webview_article);
		webView.setWebViewClient(new ArticleWebViewClient());
		webView.loadUrl("http://www.google.com");

		return rootView;
	}

	private class ArticleWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
