package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.backend.VenueEndpoint;
import hu.artklikk.android.loiti.backend.dto.Item;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ItemCacheFragment extends Fragment {
	
	public static final String TAG = ItemCacheFragment.class.getSimpleName();
	private static final String KEY_ALACARTE_ITEM_CACHE = "KEY_ALACARTE_ITEM_CACHE";
	
	public interface ItemListCallback {
		public void onItemListReady();
		public void onItemCacheListReady();
	}
	
	private ItemListCallback callback;
	
	private Map<Long, List<Item>> itemListByCategory;
	private Map<Long, List<Item>> itemListByCategoryCache;
	
	private boolean isCompleted = false;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (ItemListCallback) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if (prefs.contains(KEY_ALACARTE_ITEM_CACHE)) {
			String cacheString = prefs.getString(KEY_ALACARTE_ITEM_CACHE, null);
			if (cacheString != null) {
				Map<Long, List<Item>> itemListByCategoryCache = null;
				try {
					itemListByCategoryCache = new Gson().fromJson(cacheString,
							new TypeToken<Map<Long, List<Item>>>() {}.getType());
				}
				catch (JsonSyntaxException e) {
					itemListByCategoryCache = null;
				}
				catch (JsonParseException e) {
					itemListByCategoryCache = null;
				}
				if (itemListByCategoryCache != null && !itemListByCategoryCache.isEmpty()) {
					this.itemListByCategoryCache = itemListByCategoryCache;
					if(callback != null) {
						callback.onItemCacheListReady();
					}
				}
			}
		}
		itemListByCategory = new HashMap<Long, List<Item>>();
		
		VenueEndpoint.getAlacarteItems(VenueEndpoint.MENZA_VENUE_ID, new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				List<Item> allItemList = (List<Item>) result;
				
				for (Item item : allItemList) {
					List<Item> itemList = itemListByCategory.get(item.category.id);
					if (itemList == null) {
						itemList = new ArrayList<Item>();
						itemListByCategory.put(item.category.id, itemList);
					}
					itemList.add(item);
				}
				isCompleted = true;
				if (callback != null) {
					callback.onItemListReady();
				}
				
				Activity acivity = getActivity();
				if(acivity != null) {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
					prefs.edit().putString(KEY_ALACARTE_ITEM_CACHE,
							new Gson().toJson(itemListByCategory, new TypeToken<Map<Long, List<Item>>>() {}.getType())).apply();
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
		return itemListByCategoryCache != null && !itemListByCategoryCache.isEmpty();
	}
	
	public List<Item> getList(Long categoryId) {
		return isCompleted ? itemListByCategory.get(categoryId)
				: (itemListByCategoryCache != null ? itemListByCategoryCache.get(categoryId) : null);
	}
	
}
