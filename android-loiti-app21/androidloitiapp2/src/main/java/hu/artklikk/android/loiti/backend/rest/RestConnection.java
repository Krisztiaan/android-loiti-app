package hu.artklikk.android.loiti.backend.rest;

import android.webkit.URLUtil;

public class RestConnection {

	/** Servers' address names. */
	private static String SERVER_ADDRESS = "http://husegp.appspot.com:80/";
	/** REST URLs. */
	private static String REST_URLS = "api/";
	
	/**
	 * Set the server address if the given string is a valid URL. Otherwise does nothing.
	 * 
	 * @param server The new server address.
	 */
	public static void setServer(String server) {
		if(server != null && URLUtil.isValidUrl(server))
			SERVER_ADDRESS = server;
	}
	
	/**
	 * Set a new REST sub URL if the given sub URL with the current server address gives
	 * a valid URL. Otherwise does nothing.
	 * 
	 * @param subUrl The new REST sub url.
	 */
	public static void setRestSubUrl(String subUrl) {
		if(subUrl != null && URLUtil.isValidUrl(SERVER_ADDRESS + subUrl))
			REST_URLS = subUrl;
	}
	
	/**
	 * Returns a rest requests' base URL.
	 * 
	 * @return The base URL of the rest calls finished with '/'.
	 */
	public static String getRestBaseURL() {
		return SERVER_ADDRESS + REST_URLS;
	}
	
}
