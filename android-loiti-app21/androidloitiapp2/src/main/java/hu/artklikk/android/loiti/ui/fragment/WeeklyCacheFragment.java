package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.backend.VenueEndpoint;
import hu.artklikk.android.loiti.backend.VenueEndpoint.WeeklyItemsContainer;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class WeeklyCacheFragment extends Fragment {
	
	public static final String TAG = WeeklyCacheFragment.class.getSimpleName();
	private static final String KEY_WEEKLY_CACHE = "KEY_WEEKLY_CACHE";
	
	public interface WeeklyCacheCallback {
		public void onReady(WeeklyItemsContainer result);
		
		public void onCacheReady(WeeklyItemsContainer result);
	}
	
	private WeeklyCacheCallback callback;
	
	private WeeklyItemsContainer container;
	private WeeklyItemsContainer containerCache;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (WeeklyCacheCallback) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if (prefs.contains(KEY_WEEKLY_CACHE)) {
			String cacheString = prefs.getString(KEY_WEEKLY_CACHE, null);
			if (cacheString != null) {
				WeeklyItemsContainer containerCache = null;
				try {
					containerCache = new Gson().fromJson(cacheString, WeeklyItemsContainer.class);
				}
				catch (JsonSyntaxException e) {
					containerCache = null;
				}
				catch (JsonParseException e) {
					containerCache = null;
				}
				if (containerCache != null) {
					this.containerCache = containerCache;
					if (callback != null) {
						callback.onCacheReady(containerCache);
					}
				}
			}
		}
		
		VenueEndpoint.getWeeklyItems(VenueEndpoint.MENZA_VENUE_ID, new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				container = (WeeklyItemsContainer) result;
				if (callback != null) {
					callback.onReady(container);
				}
				Activity acivity = getActivity();
				if (acivity != null) {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
					prefs.edit().putString(KEY_WEEKLY_CACHE, new Gson().toJson(container)).apply();
				}
			}
			
			@Override
			public void onException(Exception e) {
				e.printStackTrace();
			}
		});
		
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		callback = null;
	}
	
	public boolean isReady() {
		return container != null;
	}
	
	public boolean isCacheReady() {
		return containerCache != null;
	}
	
	public WeeklyItemsContainer getContainer() {
		return container;
	}
	
	public WeeklyItemsContainer getContainerCache() {
		return containerCache;
	}
	
}
