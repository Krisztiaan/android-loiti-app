package hu.artklikk.android.loiti.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ChefsSecretsWebFragment extends Fragment {
	
	public static final String TAG = ChefsSecretsWebFragment.class.getSimpleName();
	
	private static final String URL_CHEFS_SECRET = "http://blog.menzaetterem.hu/";
	
	private WebView webView;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		webView = new WebView(getActivity());
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return !url.startsWith(URL_CHEFS_SECRET);
			}
		});
		webView.loadUrl(URL_CHEFS_SECRET);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return webView;
	}
	
	public boolean canGoBack() {
		return webView.copyBackForwardList().getCurrentIndex() > 0;
	}
	
	public void goBack() {
		webView.goBack();
	}
}
