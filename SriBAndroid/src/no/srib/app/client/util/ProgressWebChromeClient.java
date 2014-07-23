package no.srib.app.client.util;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ProgressWebChromeClient extends WebChromeClient {

	private ProgressBar progressBar;

	public ProgressWebChromeClient(final ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void onProgressChanged(final WebView webView, final int progress) {
		if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
			progressBar.setVisibility(ProgressBar.VISIBLE);
		}

		progressBar.setProgress(progress);

		if (progress == 100) {
			progressBar.setVisibility(ProgressBar.GONE);
		}
	}
}
