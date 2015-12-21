package hu.artklikk.android.loiti.backend.rest;

import hu.artklikk.android.loiti.backend.rest.core.RestBase;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public class StatefulRest extends RestBase {

	

	protected void setHeader(HttpRequestBase requestMethod) {
		super.setHeader(requestMethod);
		requestMethod.setHeader("Accept", "application/json");
		requestMethod.setHeader("Content-type", "application/json");
		requestMethod.setHeader("charset", "utf-8");
	}
	
	
	protected HttpClient getHttpClient() {
		return StatefulHTTPConnection.getHttpClient();
	}

	@Override
	protected String getBaseURL() {
		return RestConnection.getRestBaseURL();
	}
}
