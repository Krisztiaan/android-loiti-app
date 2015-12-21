package hu.artklikk.android.loiti.backend.rest.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import hu.artklikk.android.loiti.backend.rest.core.RestExecuteTask.HttpMethod;

/**
 * Base class for Rest calls.
 * 
 * @author Tam�s Holl�si
 * 
 */
public abstract class RestBase {
	
	private static String languageHeaderName;
	private static String languageHeaderValue;
	
	public static void setLanguageHeader(String name, String value) {
		languageHeaderName = name;
		languageHeaderValue = value;
	}
	
	private List<RestExecuteTask> runningTasks = new ArrayList<RestExecuteTask>();
	
	/**
	 * Make a POST call.
	 * 
	 * @param url
	 *            The rest call's URL after base URL.
	 * @param request
	 *            The request object which content will insert into the body.
	 * @param responseClazz
	 *            The response object's type.
	 * @param callback
	 *            This will call when the request finished.
	 */
	public RestExecuteTask doPost(final String url, final Object request, final TypeReference<?> responseClazz,
			final RestCallFinishListener callback) {
		
		return execute(getBaseURL() + url, HttpMethod.POST, request, responseClazz, callback);
	}
	
	/**
	 * Make a GET call.
	 * 
	 * @param url
	 *            The rest call's URL after base URL.
	 * @param responseClazz
	 *            The response object's type.
	 * @param callback
	 *            This will call when the request finished.
	 */
	public RestExecuteTask doGet(final String url, final TypeReference<?> responseClazz,
			final RestCallFinishListener callback) {
		
		return execute(getBaseURL() + url, HttpMethod.GET, null, responseClazz, callback);
	}
	
	/**
	 * Make a DELETE call.
	 * 
	 * @param url
	 *            The rest call's URL after base URL.
	 * @param responseClazz
	 *            The response object's type.
	 * @param callback
	 *            This will call when the request finished.
	 */
	public RestExecuteTask doDelete(final String url, final TypeReference<?> responseClazz,
			final RestCallFinishListener callback) {
		
		return execute(getBaseURL() + url, HttpMethod.DELETE, null, responseClazz, callback);
	}
	
	/**
	 * Make a PUT call.
	 * 
	 * @param url
	 *            The rest call's URL after base URL.
	 * @param request
	 *            The request object which content will insert into the body.
	 * @param responseClazz
	 *            The response object's type.
	 * @param callback
	 *            This will call when the request finished.
	 */
	public RestExecuteTask doPut(final String url, final Object request, final TypeReference<?> responseClazz,
			final RestCallFinishListener callback) {
		
		return execute(getBaseURL() + url, HttpMethod.PUT, request, responseClazz, callback);
	}
	
	/**
	 * Kills all the currently running requests.
	 */
	public void killRunningRequests() {
		for (RestExecuteTask t : runningTasks)
			t.cancel(true);
	}
	
	/**
	 * Execute a rest call asynchronously. The callback's methods are running on
	 * UIThread!
	 * 
	 * @param url
	 *            The URL sub part over the base URL.
	 * @param method
	 *            The HTTP method.
	 * @param request
	 *            The data send in the request's body. It will convert to JSON
	 *            with DaoConverter.
	 * @param responseClazz
	 *            The response's data object class representation. This will the
	 *            type of the response object.
	 * @param callback
	 *            This will call when the request finished. It's runs on
	 *            UIThread!
	 */
	private RestExecuteTask execute(final String url, final HttpMethod method, final Object request,
			final TypeReference<?> responseClazz, final RestCallFinishListener callback) {
		
		RestExecuteTask task = new RestExecuteTask(this, method, request, responseClazz, new OnRestExecutorFinished() {
			
			@Override
			public void onFinish(RestExecuteTask task, Object result) {
				// remove this from running list
				runningTasks.remove(task);
				
				if (callback != null && !task.isCancelled())
					callback.onFinish(result);
			}
			
			@Override
			public void onException(RestExecuteTask task, Exception e) {
				// remove this from running list
				runningTasks.remove(task);
				
				if (callback != null && !task.isCancelled())
					callback.onException(e);
			}
		});
		
		task.execute(url);
		
		runningTasks.add(task);
		
		return task;
	}
	
	/**
	 * This method is for setting the header parameters for all request.
	 * 
	 * @param request
	 *            Set header parameter on this request.
	 */
	protected void setHeader(Request.Builder request) {
		if (languageHeaderName != null && languageHeaderValue != null && !languageHeaderName.trim().isEmpty()
				&& !languageHeaderValue.trim().isEmpty()) {
			request.addHeader(languageHeaderName, languageHeaderValue);
		}
	}
	
	/**
	 * This method creates the {@link OkHttpClient}s for all request. Every
	 * request call this method to get a client. If you give new client every
	 * time this rest client will non statefull. But if you use the same client
	 * every time it will a statefull rest client.
	 * 
	 * @return The client which will use on requests.
	 */
	protected abstract OkHttpClient getHttpClient();
	
	/**
	 * This method gives the base URL for every request.
	 * 
	 * @return Base URL for the requests.
	 */
	protected abstract String getBaseURL();
	
	/**
	 * Make a NON Rest GET call to any URL. This means it won't insert the base
	 * URL of the REST calls into the URL.
	 * 
	 * @param url
	 *            The full URL of the request.
	 * @param callback
	 *            This will call when the request finished. It's runs on
	 *            UIThread!
	 */
	public void doNonRestGet(final String url, final RestCallFinishListener callback) {
		execute(url, HttpMethod.GET, null, null, callback);
	}
}
