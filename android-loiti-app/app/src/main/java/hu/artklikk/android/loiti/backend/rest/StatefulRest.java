package hu.artklikk.android.loiti.backend.rest;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import hu.artklikk.android.loiti.backend.rest.core.RestBase;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public class StatefulRest extends RestBase {

	

	protected void setHeader(Request.Builder requestMethod) {
		super.setHeader(requestMethod);
		requestMethod.header("Accept", "application/json");
		requestMethod.header("Content-type", "application/json");
		requestMethod.header("charset", "utf-8");
	}
	
	
	protected OkHttpClient getHttpClient() {
		return StatefulHTTPConnection.getHttpClient();
	}

	@Override
	protected String getBaseURL() {
		return RestConnection.getRestBaseURL();
	}
}
