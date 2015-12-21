package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FaqWebFragment extends Fragment {
	
	public static final String TAG = FaqWebFragment.class.getSimpleName();
	
	private static String URL_FAQ;
	
	private WebView webView;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		URL_FAQ = getResources().getString(R.string.url_faq);
		webView = new WebView(getActivity());
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(URL_FAQ);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return webView;
	}
	
}
