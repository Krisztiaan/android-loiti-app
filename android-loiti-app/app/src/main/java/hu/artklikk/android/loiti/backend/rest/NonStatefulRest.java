package hu.artklikk.android.loiti.backend.rest;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import hu.artklikk.android.loiti.backend.rest.core.RestBase;

public class NonStatefulRest extends RestBase {

	@Override
	protected void setHeader(Request.Builder requestBuilder) {
		super.setHeader(requestBuilder);
		requestBuilder.header("Accept", "application/json");
		requestBuilder.header("Content-type", "application/json");
		requestBuilder.header("charset", "utf-8");
	}

	@Override
	protected OkHttpClient getHttpClient() {
//		if(params == null || cm == null) {
//			BasicHttpParams params = new BasicHttpParams();
//			SchemeRegistry schemeRegistry = new SchemeRegistry();
//			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//			final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
//			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
//			cm = new ThreadSafeClientConnManager(params, schemeRegistry);
//
//			//timeouts
//			HttpConnectionParams.setSoTimeout(params, 30000);
//		}
//
//		return new DefaultHttpClient(cm, params);
//	}

		return new OkHttpClient();
	}

	@Override
	protected String getBaseURL() {
		return RestConnection.getRestBaseURL();
	}

}
