package hu.artklikk.android.loiti.backend.rest.core;

import hu.artklikk.android.loiti.backend.dto.ErrorInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This task can execute a REST request. Can only runs only one request at time
 * regardless of how many instance of this class were executed at once. However
 * it's an {@link android.os.AsyncTask} the {@link #doInBackground(Object[])}
 * method's content are synchronized.
 * 
 * ATTENTION! Because it's an {@link android.os.AsyncTask} one instance of this
 * class can only execute once!
 * 
 * @author Tamás Hollósi
 */
public class RestExecuteTask extends AsyncTask<String, Void, Object> {

	/** HTTP method types. */
	public enum HttpMethod {
		GET, POST, DELETE, PUT
	}

	/** Index of the valid result in the onPostExecute()'s parameter */
	private static final int RESULT_IDX = 0;
	/** Index of the exception in the onPostExecute()'s parameter */
	private static final int EXCEPTION_IDX = 1;

	/** The HTTP method which will execute by the HTTP client. */
	private HttpRequestBase requestMethod;

	/**
	 * The request object which from create the JSON body of the http request.
	 * (Can be null)
	 */
	private Object request;
	/** The class what will cast the http response's JSON body. (Can be null) */
	private TypeReference<?> responseClazz;
	/**
	 * The callback what trough will inform the caller about the result of the
	 * call. (Can be null)
	 */
	private OnRestExecutorFinished callback;
	/** The http method type of the request. */
	private HttpMethod httpMethod;
	/** The rest base which starts the task. */
	private RestBase restBase;

	/**
	 * Constructor of the execute task. An instance of this class can execute
	 * ONLY ONCE!
	 * 
	 * @param restBase
	 *            The rest base which starts this task.
	 * @param method
	 *            The HTTP method of the request.
	 * @param request
	 *            The request which contains the HTTP request's body. (Can be
	 *            null)
	 * @param responseClazz
	 *            The type of the response class. (Can be null)
	 * @param callback
	 *            The callback what trough will inform the caller about the
	 *            result of the call. (Can be null)
	 */
	public RestExecuteTask(RestBase restBase, HttpMethod method,
			Object request, TypeReference<?> responseClazz,
			OnRestExecutorFinished callback) {

		this.request = request;
		this.responseClazz = responseClazz;
		this.callback = callback;
		this.httpMethod = method;
		this.restBase = restBase;
	}

	@Override
	protected Object doInBackground(String... url) {
		if (this.isCancelled()) {
			System.err.println("This request was canceled: " + url[0]);
			return null;
		}
		// the result of the request contains valid data and exception
		Object[] result = new Object[2];

		// create HTTP post method
		String restUrl = url[0];
		requestMethod = createHttpRequestMethod(httpMethod, restUrl);

		// passes the request to a string builder/entity
		if (request != null
				&& requestMethod instanceof HttpEntityEnclosingRequestBase) {

			StringEntity se = null;
			try {
				String json = JsonMapper.getMapper()
						.writeValueAsString(request);
				
				se = new StringEntity(json, "UTF-8");
				
				((HttpEntityEnclosingRequestBase) requestMethod).setEntity(se);
			} catch (UnsupportedEncodingException e) {
				return returnWithException(result, e);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			
		}

		// execute request
		HttpResponse httpResponse;
		try {
			httpResponse = restBase.getHttpClient().execute(requestMethod);
		} catch (IOException e) {
			return returnWithException(result, e);
		}

		// check status
		int status = httpResponse.getStatusLine().getStatusCode();
		// get response entity
		HttpEntity resEntity = httpResponse.getEntity();
		if (status < 200 || status >= 300) {
			// get error info
			ErrorInfo errorInfo = null;
			if (resEntity != null) {
				try {
					errorInfo = JsonMapper.getMapper().readValue(resEntity.getContent(), ErrorInfo.class);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			
			// if it's a login required
			if (status == 401) {
				// return with a Login required exception
				return returnWithException(result, new LoginRequiredException(errorInfo));

			} else { // any other status
				String exMessage = "Http Status: " + status + " URL: " + url[0];
				
				if (errorInfo != null)
					exMessage += "\n" + errorInfo.message;

				// return with a Http Exception containing status and LIRE
				// error description
				return returnWithException(result, new HttpExceptionWithInfo(status, errorInfo, exMessage));
			}
		}
		
		if (resEntity == null)
			return returnWithResult(result, null);

		// create response DAO
		if (responseClazz == null)
			return returnWithResult(result, null);

		Object response = null;
		try {
			response = JsonMapper.getMapper()
					.readValue(resEntity.getContent(), responseClazz);
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return returnWithResult(result, response);
	}

	/**
	 * This method handles that returns when the execute finishing with
	 * exception. It will return a valid result set witch can be handled by
	 * {@link #onPostExecute(Object)} and send trough callback.
	 * 
	 * @param resultSet
	 *            The currently using result set which will be added the
	 *            exception.
	 * @param e
	 *            The exception which occurred.
	 * 
	 * @return The result set with updated exception.
	 */
	private Object[] returnWithException(Object[] resultSet, Exception e) {
		resultSet[EXCEPTION_IDX] = e;

		// try-catches final part
		doBackgroundFinalPart();

		return resultSet;
	}

	/**
	 * This method handles that returns when the result was valid. The null is
	 * also a valid option. It will return a valid result set witch can be
	 * handled by {@link #onPostExecute(Object)} and send trough callback.
	 * 
	 * @param resultSet
	 *            The currently using result set which will be added the
	 *            exception.
	 * @param result
	 *            The valid result which will set into the set.
	 * 
	 * @return The result set with updated result part.
	 */
	private Object[] returnWithResult(Object[] resultSet, Object result) {
		resultSet[RESULT_IDX] = result;

		// try-catches final part
		doBackgroundFinalPart();

		return resultSet;
	}

	/**
	 * This method contains all calls which HAVE TO do before finish the
	 * request. It's likely the try-catches final part.
	 */
	private void doBackgroundFinalPart() {
		if (requestMethod != null)
			requestMethod.abort();
	}

	/**
	 * HTTP method creator. Create a the wanted method and sets the header with
	 * {@link RestBase#setHeader(HttpRequestBase)} method.
	 * 
	 * @param method
	 *            The wanted HTTP method.
	 * @param url
	 *            The full URL of the request which for this request will use.
	 * 
	 * @return New HTTP method request object.
	 */
	private HttpRequestBase createHttpRequestMethod(HttpMethod method,
			String url) {
		HttpRequestBase request = null;
		switch (method) {
		case GET:
			request = new HttpGet(url);
			break;
		case POST:
			request = new HttpPost(url);
			break;
		case DELETE:
			request = new HttpDelete(url);
			break;
		case PUT:
			request = new HttpPut(url);
			break;
		}

		restBase.setHeader(request);

		return request;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (this.isCancelled()) {
			System.err
					.println("This request was canceled! onPostExecute won't return!");
			return;
		}

		// if there isn't any callback do nothing
		if (callback == null)
			return;

		// if the result is null call onFinish with null
		if (result == null) {
			callback.onFinish(this, null);
			return;
		}

		// cast the result to result set
		Object[] res = (Object[]) result;

		// if the result contains Exception
		if (res[EXCEPTION_IDX] != null)
			callback.onException(this, (Exception) res[EXCEPTION_IDX]);
		else
			// if the result doesn't contains exception finished with a result
			// which can be null also
			callback.onFinish(this, res[RESULT_IDX]);

	}

}
