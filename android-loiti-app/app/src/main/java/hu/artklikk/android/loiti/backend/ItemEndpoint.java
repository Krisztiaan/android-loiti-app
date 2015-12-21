package hu.artklikk.android.loiti.backend;

import hu.artklikk.android.loiti.backend.dto.Item;
import hu.artklikk.android.loiti.backend.dto.ItemCategory;
import hu.artklikk.android.loiti.backend.rest.RestFactory;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

public class ItemEndpoint {
	
	private static final String URL_ITEM = "item";
	private static final String URL_ITEM_CATEGORIES = "item/category";
	
	public static void getItems(final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(URL_ITEM, new TypeReference<List<Item>>() {}, callback);
	}
	
	public static void getCategories(final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(URL_ITEM_CATEGORIES, new TypeReference<List<ItemCategory>>() {}, callback);
	}

}
