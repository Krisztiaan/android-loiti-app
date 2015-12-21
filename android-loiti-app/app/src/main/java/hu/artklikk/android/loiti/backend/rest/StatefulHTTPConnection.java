package hu.artklikk.android.loiti.backend.rest;


import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpConnectionParams;

/**
 * A singleton of a static HTTP client. It automatically handle cookies.
 * 
 * @author Tamás Hollósi
 * 
 */
public class StatefulHTTPConnection {

	/** Static instance of HTTP Client. */
	private static HttpClient client;

	/**
	 * Gives the singleton instance of the client.
	 * 
	 * @return A statefull client.
	 */
	public static HttpClient getHttpClient() {
		if (client == null) {
			BasicHttpParams params = new BasicHttpParams();
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
			
			//timeouts
			HttpConnectionParams.setSoTimeout(params, 30000);
			
			client = new DefaultHttpClient(cm, params);
		}
		return client;
	}

}
