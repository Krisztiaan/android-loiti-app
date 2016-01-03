package hu.artklikk.android.loiti.backend.rest;


import com.squareup.okhttp.OkHttpClient;

/**
 * A singleton of a static HTTP client. It automatically handle cookies.
 *
 * @author Tamás Hollósi
 */
public class StatefulHTTPConnection {

    /**
     * Static instance of HTTP Client.
     */
    private static OkHttpClient client;

    /**
     * Gives the singleton instance of the client.
     *
     * @return A statefull client.
     */
    public static OkHttpClient getHttpClient() {
        if (client == null) {
            client = new OkHttpClient();

            // TODO: 12/22/2015 Kell meg valami?

//			BasicHttpParams params = new BasicHttpParams();
//			SchemeRegistry schemeRegistry = new SchemeRegistry();
//			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//			final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
//			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
//			ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
//
//			//timeouts
//			HttpConnectionParams.setSoTimeout(params, 30000);
//
//			client = new DefaultHttpClient(cm, params);
        }

        return client;
    }
}