package eu.chitic.syp.gui.common;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author A510944
 * this is used to force the webview to load SSL and not open a new intent to the OS
 */
public class AbstractWebViewClient extends WebViewClient {
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		handler.proceed();

	}

}
