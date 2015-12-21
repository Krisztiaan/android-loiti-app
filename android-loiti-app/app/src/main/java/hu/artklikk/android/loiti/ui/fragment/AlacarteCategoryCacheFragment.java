package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.backend.ItemEndpoint;
import hu.artklikk.android.loiti.backend.dto.ItemCategory;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class AlacarteCategoryCacheFragment extends Fragment {
	
	public static final String TAG = AlacarteCategoryCacheFragment.class.getSimpleName();
	private static final String KEY_ALACARTE_CATEGORY_CACHE = "KEY_ALACARTE_CATEGORY_CACHE";
	
	public interface CategoryListCallback {
		public void onCategoryListReady(List<ItemCategory> categoryList);
		public void onCategoryCacheListReady(List<ItemCategory> categoryList);
	}
	
	private CategoryListCallback callback;
	
	private List<ItemCategory> categoryList;
	private List<ItemCategory> categoryCacheList;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (CategoryListCallback) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if (prefs.contains(KEY_ALACARTE_CATEGORY_CACHE)) {
			String cacheString = prefs.getString(KEY_ALACARTE_CATEGORY_CACHE, null);
			if (cacheString != null) {
				List<ItemCategory> categoryCacheList = null;
				try {
					categoryCacheList = new Gson().fromJson(cacheString, new TypeToken<List<ItemCategory>>() {}.getType());
				}
				catch (JsonSyntaxException e) {
					categoryCacheList = null;
				}
				catch (JsonParseException e) {
					categoryCacheList = null;
				}
				if (categoryCacheList != null && !categoryCacheList.isEmpty()) {
					this.categoryCacheList = categoryCacheList;
					if (callback != null) {
						callback.onCategoryCacheListReady(categoryCacheList);
					}
				}
			}
		}
		
		ItemEndpoint.getCategories(new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				categoryList = (List<ItemCategory>) result;
				if (callback != null) {
					callback.onCategoryListReady(categoryList);
				}
				Activity acivity = getActivity();
				if (acivity != null) {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
					prefs.edit()
							.putString(KEY_ALACARTE_CATEGORY_CACHE,
									new Gson().toJson(categoryList, new TypeToken<List<ItemCategory>>() {}.getType())).apply();
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
	
	public boolean isListReady() {
		return categoryList != null;
	}
	
	public boolean isListCacheReady() {
		return categoryCacheList != null && !categoryCacheList.isEmpty();
	}
	
	public List<ItemCategory> getList() {
		return categoryList;
	}
	
	public List<ItemCategory> getListCache() {
		return categoryCacheList;
	}
	
}
