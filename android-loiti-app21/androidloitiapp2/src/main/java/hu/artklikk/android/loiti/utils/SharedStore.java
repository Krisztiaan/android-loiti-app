package hu.artklikk.android.loiti.utils;

import hu.artklikk.android.loiti.backend.rest.core.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public abstract class SharedStore<T> {

	/** Id container preferences name. */
	private static final String PREF_KEY_IDS = "CACHE_IDS";

	private SharedPreferences preferences;

	public SharedStore(Context ctx) {
		preferences = ctx.getSharedPreferences(getPreferencesName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * This method have to give the shared preferences file name. It have to be
	 * constant to always use the same preferences. It used in the constructor
	 * to connect to preferences.
	 * 
	 * @return Name of the preferences what this cache use.
	 */
	protected abstract String getPreferencesName();

	/**
	 * This class have to give the template type's class type. Like: return
	 * String.class;
	 * 
	 * @return Type of the template type.
	 */
	protected abstract Class<T> getDataClass();

	/**
	 * Add an element to the cache. If it's already in the cache it will
	 * override.
	 * 
	 * @param id
	 *            Id of the item.
	 * @param data
	 *            The data wanted to store.
	 */
	public void add(String id, T data) {
		String json = null;
		try {
			json = JsonMapper.getMapper().writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (json == null || json.equals(""))
			return;

		// get all id
		Set<String> ids = preferences.getStringSet(PREF_KEY_IDS, null);
		// means store is empty
		if (ids == null)
			ids = new HashSet<String>();

		// add the id to the list
		ids.add(id);
		Editor edit = preferences.edit();
		// refresh the id list
		edit.putStringSet(PREF_KEY_IDS, ids);
		// save the object in JSON
		edit.putString(id, json);
		// commit in
		edit.commit();
	}

	/**
	 * Gives an item by the given ID.
	 * 
	 * @param id
	 *            The id of the required item.
	 * 
	 * @return An item or null if it can't found.
	 */
	public T getItem(String id) {
		// get all id
		Set<String> ids = preferences.getStringSet(PREF_KEY_IDS, null);

		// check contains
		if (ids == null || !ids.contains(id))
			return null;

		// get the item as JSON
		String itemJson = preferences.getString(id, null);

		if (itemJson == null || itemJson.equals(""))
			return null;

		T data = null;
		try {
			data = JsonMapper.getMapper().readValue(itemJson, getDataClass());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Removes an item from the store.
	 * 
	 * @param id The item's id.
	 * 
	 * @return The removed item or null if there wasn't any item whit the given id.
	 */
	public T remove(String id) {
		// get all id
		Set<String> ids = preferences.getStringSet(PREF_KEY_IDS, null);

		// check contains
		if (!ids.contains(id))
			return null;
		
		// get the item
		T item = getItem(id);
		Editor edit = preferences.edit();
		// remove id from id list
		ids.remove(id);
		// update ids
		edit.putStringSet(PREF_KEY_IDS, ids);
		// remove item
		edit.remove(id);
		// commit changes
		edit.commit();
		
		return item;
	}

	/**
	 * @return All item's id.
	 */
	public String[] getAllId() {
		// get all id
		Set<String> ids = preferences.getStringSet(PREF_KEY_IDS, null);
		if (ids == null)
			return new String[0];

		return ids.toArray(new String[ids.size()]);
	}

	/**
	 * @return All item in the store.
	 */
	public List<T> getAllItem() {
		String[] ids = getAllId();
		if (ids == null)
			return null;

		List<T> items = new ArrayList<T>();
		for (String id : ids) {
			items.add(getItem(id));
		}

		return items;
	}

	/**
	 * Clear all item from store.
	 */
	public void clear() {
		String[] ids = getAllId();
		if(ids == null)
			return;
		
		for(String id : ids) {
			remove(id);
		}
	}
}
