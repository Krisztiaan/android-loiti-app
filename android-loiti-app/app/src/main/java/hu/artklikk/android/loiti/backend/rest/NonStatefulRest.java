package hu.artklikk.android.loiti.backend.rest;

import com.squareup.okhttp.OkHttpClient;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpConnectionParams;

import hu.artklikk.android.loiti.backend.rest.core.RestBase;

public class NonStatefulRest extends RestBase {

	private ClientConnectionManager cm;
	private BasicHttpParams params;
	
	
	@Override
	protected void setHeader(HttpRequestBase requestMethod) {
		super.setHeader(requestMethod);
		requestMethod.setHeader("Accept", "application/json");
		requestMethod.setHeader("Content-type", "application/json");
		requestMethod.setHeader("charset", "utf-8");		
	}

	@Override
	protected OkHttpClient getHttpClient() {
		if(params == null || cm == null) {
			BasicHttpParams params = new BasicHttpParams();
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			cm = new ThreadSafeClientConnManager(params, schemeRegistry);
			
			//timeouts
			HttpConnectionParams.setSoTimeout(params, 30000);
		}
		
		return new DefaultHttpClient(cm, params);
	}

	@Override
	protected String getBaseURL() {
		return RestConnection.getRestBaseURL();
	}

}
