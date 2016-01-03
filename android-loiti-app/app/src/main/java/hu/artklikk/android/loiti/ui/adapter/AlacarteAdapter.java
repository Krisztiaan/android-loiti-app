package hu.artklikk.android.loiti.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.Locale;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.dto.ItemCategory;
import hu.artklikk.android.loiti.ui.adapter.AlacarteAdapter.CategoryViewHolder;
import hu.artklikk.android.loiti.ui.adapter.AlacarteAdapter.CategoryViewHolder.CategoryClickListener;

public class AlacarteAdapter extends Adapter<CategoryViewHolder> {
	
	public static class CategoryViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements android.view.View.OnClickListener {
		
		public interface CategoryClickListener {
            void onCategoryClick(ItemCategory category, @Nullable View... views);
		}
		
		TextView name;
		ImageView thumbnail;
		CategoryClickListener listener;
		
		ItemCategory category;
		
		public CategoryViewHolder(View itemView, CategoryClickListener listener) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.item_alacarte_name);
			thumbnail = (ImageView) itemView.findViewById(R.id.item_alacarte_thumbnail);
			this.listener = listener;
			itemView.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v) {
			listener.onCategoryClick(category, name, thumbnail, itemView.findViewById(R.id.item_alacarte_stripes));
		}
		
		void setCategory(ItemCategory category) {
			this.category = category;
		}
		
	}
	
	private List<ItemCategory> categoryList;
	private CategoryClickListener listener;
	
	private RecyclerView.LayoutParams params;
	
	public AlacarteAdapter(List<ItemCategory> categoryList, CategoryClickListener listener) {
		this.categoryList = categoryList;
		this.listener = listener;
	}

	public void replaceList(List<ItemCategory> categoryList) {
		this.categoryList = categoryList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		return categoryList.size();
	}
	
	@Override
	public void onBindViewHolder(CategoryViewHolder holder, int position) {
		holder.name.setText(categoryList.get(position).name.toUpperCase(Locale.ENGLISH));
		Ion.with(holder.thumbnail).load(categoryList.get(position).imgProfile);
		holder.setCategory(categoryList.get(position));
	}
	
	@Override
	public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (params == null) {
			params = createLayoutParams(parent);
		}
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alacarte, parent, false);
		view.setLayoutParams(new RecyclerView.LayoutParams(params));
		return new CategoryViewHolder(view, listener);
	}
	
	private RecyclerView.LayoutParams createLayoutParams(ViewGroup parent) {
		int parentHeight = parent.getMeasuredHeight();
		int parentWidth = parent.getMeasuredWidth();
		
		int childHeight;
		int childWidth;
		
		if (parentHeight < parentWidth) {
			childWidth = (int) Math.ceil((float) parentWidth / 3f);
			childHeight = parentHeight;
		}
		else {
			childHeight = (int) Math.ceil((float) parentHeight / 3f);
			childWidth = parentWidth;
		}
		
		return new RecyclerView.LayoutParams(childWidth, childHeight);
	}
	
}
