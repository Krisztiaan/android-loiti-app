package hu.artklikk.android.loiti.backend.rest;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import hu.artklikk.android.loiti.backend.rest.core.RestBase;

public class StatefulRest extends RestBase {

	

	protected void setHeader(Request.Builder requestBuilder) {
		super.setHeader(requestBuilder);
		requestBuilder.header("Accept", "application/json");
		requestBuilder.header("Content-type", "application/json");
		requestBuilder.header("charset", "utf-8");
	}
	
	
	protected OkHttpClient getHttpClient() {
		return StatefulHTTPConnection.getHttpClient();
	}

	@Override
	protected String getBaseURL() {
		return RestConnection.getRestBaseURL();
	}
}
