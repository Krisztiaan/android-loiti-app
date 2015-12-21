package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.dto.ItemCategory;
import hu.artklikk.android.loiti.ui.adapter.AlacarteAdapter;
import hu.artklikk.android.loiti.ui.adapter.AlacarteAdapter.CategoryViewHolder.CategoryClickListener;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.AlacarteCategoryFragment;
import hu.artklikk.android.loiti.ui.fragment.FeaturedCacheFragment;
import hu.artklikk.android.loiti.ui.fragment.FeaturedCacheFragment.FeaturedListCallback;

import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class FeaturedMenuActivity extends MenuActivity implements FeaturedListCallback, CategoryClickListener {
	
	private RecyclerView list;
	
	private FeaturedCacheFragment cache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_featured_menu);
		
		if ((cache = (FeaturedCacheFragment) getSupportFragmentManager().findFragmentByTag(FeaturedCacheFragment.TAG)) == null) {
			cache = new FeaturedCacheFragment();
			getSupportFragmentManager().beginTransaction().add(cache, FeaturedCacheFragment.TAG).commit();
		}
		
		list = (RecyclerView) findViewById(R.id.activity_featured_menu_list);
		if (cache.isListReady()) {
			list.setAdapter(new AlacarteAdapter(cache.getCategoryList(), this));
		}
		else if(cache.isListCacheReady()) {
			list.setAdapter(new AlacarteAdapter(cache.getCategoryList(), this));
		}
	}
	
	@Override
	public void onCategoryListReady(List<ItemCategory> categoryList) {
		if(!categoryList.equals(cache.getCategoryCacheList())) {
			list.setAdapter(new AlacarteAdapter(categoryList, this));			
		}
	}
	
	@Override
	public void onCategoryCacheListReady(List<ItemCategory> categoryList) {
		list.setAdapter(new AlacarteAdapter(categoryList, this));
	}
	
	@Override
	public void onCategoryClick(ItemCategory category) {
		if (cache.isListReady() || cache.isListCacheReady()) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.activity_featured_menu_root,
							AlacarteCategoryFragment.newInstance(cache.getItemList(category.id), category))
					.addToBackStack(null).commit();
		}
	}
	
}
