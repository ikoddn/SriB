package no.srib.app.client.util;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class EmbeddedWebViewClient extends WebViewClient {

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}
}
