package hu.artklikk.android.loiti.backend.rest.core;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.koushikdutta.async.util.Charsets;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import hu.artklikk.android.loiti.backend.dto.ErrorInfo;

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
	private Request requestMethod;

	private Request.Builder requestBuilder;

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

		// execute request
		Response httpResponse;
		try {
            requestMethod = requestBuilder.build();
			httpResponse = restBase.getHttpClient().newCall(requestMethod).execute();
		} catch (IOException e) {
			return returnWithException(result, e);
		}

        // passes the request to a string builder/entity
        if (request != null) {

            String se = null;
            try {
                byte[] json = JsonMapper.getMapper()
                        .writeValueAsBytes(request);

                se = new String(json, Charsets.UTF_8);

                switch (httpMethod) {

                    case POST:
                     requestBuilder.post().setEntity(se);
                        break;

                }
            } catch (UnsupportedEncodingException e) {
                return returnWithException(result, e);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


        }

		// check status
		int status = httpResponse.code();
		// get response entity
		ResponseBody resEntity = httpResponse.body();
		if (status < 200 || status >= 300) {
			// get error info
			ErrorInfo errorInfo = null;
			if (resEntity != null) {
				try {
					errorInfo = JsonMapper.getMapper().readValue(resEntity.string(), ErrorInfo.class);
				} catch (IllegalStateException | IOException e) {
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
					.readValue(resEntity.string(), responseClazz);
			
		} catch (IllegalStateException | IOException e) {
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
		if (requestMethod != null) {
			// TODO: 12/22/2015 Need something?
		}
	}

	/**
	 * HTTP method creator. Create a the wanted method and sets the header with
	 * {@link RestBase#setHeader(com.squareup.okhttp.Request.Builder)} method.
	 * 
	 * @param method
	 *            The wanted HTTP method.
	 * @param url
	 *            The full URL of the request which for this request will use.
	 * 
	 * @return New HTTP method request object.
	 */
	private Request createHttpRequestMethod(HttpMethod method,
			String url) {
		requestBuilder = new Request.Builder();
		requestBuilder.url(url);

		requestBuilder.post(RequestBody.create(MediaType.parse()))

		restBase.setHeader(requestBuilder);

		return requestBuilder.build();
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
