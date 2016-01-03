package hu.artklikk.android.loiti.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.Locale;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.VenueEndpoint;
import hu.artklikk.android.loiti.backend.dto.Item;
import hu.artklikk.android.loiti.backend.dto.ItemAvailability;
import hu.artklikk.android.loiti.ui.adapter.AlacarteCategoryAdapter.ItemViewHolder;

public class AlacarteCategoryAdapter extends Adapter<ItemViewHolder> {
	
	public static class ItemViewHolder extends ViewHolder {
		
		TextView name;
		TextView description;
		TextView price;
		ImageView image;
		
		public ItemViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.item_alacarte_sub_name);
			description = (TextView) itemView.findViewById(R.id.item_alacarte_sub_description);
			price = (TextView) itemView.findViewById(R.id.item_alacarte_sub_price);
			image = (ImageView) itemView.findViewById(R.id.item_alacarte_sub_image);
		}
		
	}
	
	private List<Item> itemList;
	
	private RecyclerView.LayoutParams params;
	
	public AlacarteCategoryAdapter(List<Item> itemList) {
		this.itemList = itemList;
	}
	
	@Override
	public int getItemCount() {
		return itemList.size();
	}
	
	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		holder.name.setText(itemList.get(position).name.toUpperCase(Locale.ENGLISH));
		Ion.with(holder.image).load(itemList.get(position).imgProfile);
		if(itemList.get(position).tags != null && !itemList.get(position).tags.isEmpty()) {
			StringBuilder desc = new StringBuilder(itemList.get(position).tags.get(0).name);
			for (int i = 1; i < itemList.get(position).tags.size(); i++) {
				desc.append(", ").append(itemList.get(position).tags.get(i).name);
			}
			holder.description.setText(desc.toString());
		}
		if(itemList.get(position).venues != null && !itemList.get(position).venues.isEmpty()) {
			for (ItemAvailability itemAvailability : itemList.get(position).venues) {
				if(VenueEndpoint.MENZA_VENUE_ID == itemAvailability.venueId.longValue()) {
					holder.price.setText(
							((float)((int)itemAvailability.price.floatValue())) == itemAvailability.price.floatValue()
							?(int)itemAvailability.price.floatValue() + " " + itemAvailability.currency
									:itemAvailability.price + " " + itemAvailability.currency);
				}
			}
		}
	}
	
	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (params == null) {
			params = createLayoutParams(parent);
		}
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alacarte_sub, parent, false);
		view.setLayoutParams(new RecyclerView.LayoutParams(params));
		return new ItemViewHolder(view);
	}
	
	private RecyclerView.LayoutParams createLayoutParams(ViewGroup parent) {
		return new RecyclerView.LayoutParams(parent.getMeasuredWidth(), parent.getMeasuredHeight());
	}
	
}
