package hu.artklikk.android.loiti.ui.adapter;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.VenueEndpoint;
import hu.artklikk.android.loiti.backend.dto.Item;
import hu.artklikk.android.loiti.backend.dto.ItemAvailability;
import hu.artklikk.android.loiti.backend.dto.ItemCategory;
import hu.artklikk.android.loiti.ui.adapter.FeaturedAdapter.TextViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeaturedAdapter extends Adapter<TextViewHolder> {
	
	public static class TextViewHolder extends ViewHolder {
		
		TextView name;
		TextView price;
		
		public TextViewHolder(View itemView, int viewType) {
			super(itemView);
			switch (viewType) {
			case 0:
				name = (TextView) itemView.findViewById(R.id.item_featured_header_text);
				break;
			case 1:
				name = (TextView) itemView.findViewById(R.id.item_featured_course_name);
				price = (TextView) itemView.findViewById(R.id.item_featured_course_price);
				break;
				
			default:
				break;
			}
		}
	}
	
	private static final class ItemWrapper {
		
		private static final int TYPE_HEADER = 0;
		private static final int TYPE_COURSE = 1;
		
		private int type;
		private String name;
		private String price;
		
		public ItemWrapper(int type, String name, String price) {
			this.type = type;
			this.name = name;
			this.price = price;
		}
		
	}
	
	private List<ItemWrapper> items;
	
	public FeaturedAdapter(List<Item> itemList) {
		
		items = new ArrayList<ItemWrapper>();
		
		Map<ItemCategory, List<Item>> itemMap = new HashMap<ItemCategory, List<Item>>();
		
		for (Item item : itemList) {
			List<Item> categoryList = itemMap.get(item.category);
			if(categoryList == null) {
				categoryList = new ArrayList<Item>();
				itemMap.put(item.category, categoryList);
			}
			categoryList.add(item);
		}
		
		Set<ItemCategory> keySet = itemMap.keySet();
		
		for (ItemCategory itemCategory : keySet) {

			List<Item> categoryList = itemMap.get(itemCategory);
			items.add(new ItemWrapper(ItemWrapper.TYPE_HEADER, itemCategory.name.toUpperCase(Locale.ENGLISH), null));
			for (Item item : categoryList) {
				String price = null;
				for (ItemAvailability itemAvailability : item.venues) {
					if(VenueEndpoint.MENZA_VENUE_ID == itemAvailability.venueId.longValue()) {
						price = 
								((float)((int)itemAvailability.price.floatValue())) == itemAvailability.price.floatValue()
								?(int)itemAvailability.price.floatValue() + " " + itemAvailability.currency
										:itemAvailability.price + " " + itemAvailability.currency;
					}
				}
				items.add(new ItemWrapper(ItemWrapper.TYPE_COURSE, item.name, price));
			}
		}
	}
	
	@Override
	public int getItemCount() {
		return items.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		return items.get(position).type;
	}

	@Override
	public void onBindViewHolder(TextViewHolder holder, int position) {
		holder.name.setText(items.get(position).name);			
		if(items.get(position).price != null) {
			holder.price.setText(items.get(position).price);			
		}
	}
	
	@Override
	public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		
		switch (viewType) {
		case 0:
			return new TextViewHolder(inflater.inflate(R.layout.item_featured_header, parent, false), viewType);
		case 1:
			return new TextViewHolder(inflater.inflate(R.layout.item_featured_course, parent, false), viewType);
		default:
			return null;
		}
	}
	
}
