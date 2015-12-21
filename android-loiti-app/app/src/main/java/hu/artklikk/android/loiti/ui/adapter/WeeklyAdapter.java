package hu.artklikk.android.loiti.ui.adapter;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.VenueEndpoint.WeeklyItemsContainer;
import hu.artklikk.android.loiti.backend.dto.SubItem;
import hu.artklikk.android.loiti.ui.adapter.WeeklyAdapter.TextViewHolder;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeeklyAdapter extends Adapter<TextViewHolder> {
	
	public static class TextViewHolder extends ViewHolder {
		
		TextView name;
		
		public TextViewHolder(View itemView, int viewType) {
			super(itemView);
			switch (viewType) {
			case 0:
				name = (TextView) itemView.findViewById(R.id.item_weekly_header_text);
				break;
			case 1:
				name = (TextView) itemView.findViewById(R.id.item_weekly_course_notlast);
				break;
				
			case 2:
				name = (TextView) itemView.findViewById(R.id.item_weekly_course_last);
				break;
			default:
				break;
			}
		}
	}
	
	private static final class ItemWrapper {
		
		private static final int TYPE_HEADER = 0;
		private static final int TYPE_COURSE_NOTLAST = 1;
		private static final int TYPE_COURSE_LAST = 2;
		
		private int type;
		private String value;
		
		public ItemWrapper(int type, String value) {
			this.type = type;
			this.value = value;
		}
		
	}
	
	private List<ItemWrapper> items;
	
	public WeeklyAdapter(WeeklyItemsContainer itemsContainer, String[] weekdayNames) {
		
		items = new ArrayList<ItemWrapper>();
		
		List<List<SubItem>> subItemList = itemsContainer.getWeeklyItems();
		
		for (int i = 0; i < subItemList.size(); i++) {
			
			List<SubItem> dailyList = subItemList.get(i);
			
			if(dailyList != null && !dailyList.isEmpty()) {
				items.add(new ItemWrapper(ItemWrapper.TYPE_HEADER, weekdayNames[i]));
				for (int j = 0; j < dailyList.size()-1; j++) {
					items.add(new ItemWrapper(ItemWrapper.TYPE_COURSE_NOTLAST, dailyList.get(j).name));
				}
				items.add(new ItemWrapper(ItemWrapper.TYPE_COURSE_LAST, dailyList.get(dailyList.size()-1).name));
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
		holder.name.setText(items.get(position).value);
	}
	
	@Override
	public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		
		switch (viewType) {
		case 0:
			return new TextViewHolder(inflater.inflate(R.layout.item_weekly_header, parent, false), viewType);
		case 1:
			return new TextViewHolder(inflater.inflate(R.layout.item_weekly_course_notlast, parent, false), viewType);
		case 2:
			return new TextViewHolder(inflater.inflate(R.layout.item_weekly_course_last, parent, false), viewType);
		default:
			return null;
		}
	}
	
}
