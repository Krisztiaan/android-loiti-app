package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.backend.VenueEndpoint;
import hu.artklikk.android.loiti.backend.dto.Item;
import hu.artklikk.android.loiti.backend.dto.ItemCategory;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

public class FeaturedCacheFragment extends Fragment {
	
	public static final String TAG = FeaturedCacheFragment.class.getSimpleName();
	private static final String KEY_FEATURED_CATEGORY_CACHE = "KEY_FEATURED_CATEGORY_CACHE";
	private static final String KEY_FEATURED_ITEM_CACHE = "KEY_FEATURED_ITEM_CACHE";
	
	public interface FeaturedListCallback {
		public void onCategoryListReady(List<ItemCategory> categoryList);
		
		public void onCategoryCacheListReady(List<ItemCategory> categoryList);
	}
	
	private FeaturedListCallback callback;
	
	private List<ItemCategory> categoryList;
	private List<ItemCategory> categoryCacheList;
	private Map<Long, List<Item>> itemListByCategory;
	private Map<Long, List<Item>> itemListCacheByCategory;
	
	private boolean isCompleted = false;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (FeaturedListCallback) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if (prefs.contains(KEY_FEATURED_ITEM_CACHE)) {
			String cacheString = prefs.getString(KEY_FEATURED_ITEM_CACHE, null);
			if (cacheString != null) {
				Map<Long, List<Item>> itemListCacheByCategory = null;
				try {
					itemListCacheByCategory = new Gson().fromJson(cacheString, new TypeToken<Map<Long, List<Item>>>() {}.getType());
				}
				catch (JsonSyntaxException e) {
					itemListCacheByCategory = null;
				}
				catch (JsonParseException e) {
					itemListCacheByCategory = null;
				}
				if (itemListCacheByCategory != null && !itemListCacheByCategory.isEmpty()) {
					this.itemListCacheByCategory = itemListCacheByCategory;
				}
			}
		}
		
		if (prefs.contains(KEY_FEATURED_CATEGORY_CACHE)) {
			String cacheString = prefs.getString(KEY_FEATURED_CATEGORY_CACHE, null);
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
		
		categoryList = new ArrayList<ItemCategory>();
		itemListByCategory = new HashMap<Long, List<Item>>();
		
		VenueEndpoint.getFeaturedItems(VenueEndpoint.MENZA_VENUE_ID, new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				List<Item> response = (List<Item>) result;
				for (Item item : response) {
					if (item.category != null && !categoryList.contains(item.category)) {
						categoryList.add(item.category);
					}
					List<Item> itemList = itemListByCategory.get(item.category.id);
					if (itemList == null) {
						itemList = new ArrayList<Item>();
						itemListByCategory.put(item.category.id, itemList);
					}
					itemList.add(item);
				}
				isCompleted = true;
				if (callback != null) {
					callback.onCategoryListReady(categoryList);
				}
				
				Activity acivity = getActivity();
				if (acivity != null) {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
					prefs.edit()
							.putString(KEY_FEATURED_ITEM_CACHE,
									new Gson().toJson(itemListByCategory, new TypeToken<Map<Long, List<Item>>>() {}.getType()))
							.putString(KEY_FEATURED_CATEGORY_CACHE,
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
		return isCompleted;
	}
	
	public boolean isListCacheReady() {
		return categoryCacheList != null && !categoryCacheList.isEmpty();
	}
	
	public List<ItemCategory> getCategoryList() {
		return categoryList;
	}
	
	public List<ItemCategory> getCategoryCacheList() {
		return categoryCacheList;
	}
	
	public List<Item> getItemList(long id) {
		return isCompleted ? itemListByCategory.get(id) : ((itemListCacheByCategory != null && !itemListCacheByCategory
				.isEmpty()) ? itemListCacheByCategory.get(id) : null);
	}
	
}
