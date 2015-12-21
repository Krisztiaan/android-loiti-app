package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.dto.ItemCategory;
import hu.artklikk.android.loiti.ui.adapter.AlacarteAdapter;
import hu.artklikk.android.loiti.ui.adapter.AlacarteAdapter.CategoryViewHolder.CategoryClickListener;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.AlacarteCategoryCacheFragment;
import hu.artklikk.android.loiti.ui.fragment.AlacarteCategoryCacheFragment.CategoryListCallback;
import hu.artklikk.android.loiti.ui.fragment.AlacarteCategoryFragment;
import hu.artklikk.android.loiti.ui.fragment.ItemCacheFragment;
import hu.artklikk.android.loiti.ui.fragment.ItemCacheFragment.ItemListCallback;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

@EActivity(R.layout.activity_alacarte)
public class AlacarteActivity extends MenuActivity implements CategoryListCallback, ItemListCallback,
		CategoryClickListener {
	
	@ViewById(R.id.activity_alacarte_list)
	RecyclerView list;
	
	private AlacarteCategoryCacheFragment categoryCache;
	private ItemCacheFragment itemCache;
	
	private static final String KEY_LAST_CLICKED = "KEY_LAST_CLICKED";
	private ItemCategory lastClickedCategory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(KEY_LAST_CLICKED)) {
				lastClickedCategory = savedInstanceState.getParcelable(KEY_LAST_CLICKED);
			}
		}
		if ((categoryCache = (AlacarteCategoryCacheFragment) getSupportFragmentManager().findFragmentByTag(
				AlacarteCategoryCacheFragment.TAG)) == null) {
			categoryCache = new AlacarteCategoryCacheFragment();
			getSupportFragmentManager().beginTransaction().add(categoryCache, AlacarteCategoryCacheFragment.TAG)
					.commit();
		}
		if ((itemCache = (ItemCacheFragment) getSupportFragmentManager().findFragmentByTag(ItemCacheFragment.TAG)) == null) {
			itemCache = new ItemCacheFragment();
			getSupportFragmentManager().beginTransaction().add(itemCache, ItemCacheFragment.TAG).commit();
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelable(KEY_LAST_CLICKED, lastClickedCategory);
	}
	
	@AfterViews
	void setupList() {
		if (categoryCache.isListReady()) {
			list.setAdapter(new AlacarteAdapter(categoryCache.getList(), this));
		}
		else if (categoryCache.isListCacheReady()) {
			list.setAdapter(new AlacarteAdapter(categoryCache.getListCache(), this));
		}
	}
	
	@Override
	public void onCategoryListReady(List<ItemCategory> categoryList) {
		if (!categoryList.equals(categoryCache.getListCache())) {
			list.setAdapter(new AlacarteAdapter(categoryCache.getList(), this));
		}
	}
	
	@Override
	public void onCategoryCacheListReady(List<ItemCategory> categoryList) {
		list.setAdapter(new AlacarteAdapter(categoryCache.getListCache(), this));
	}
	
	@Override
	public void onCategoryClick(ItemCategory category) {
		if (itemCache.isListReady() || itemCache.isListCacheReady()) {
			addItemListFragment(category);
		}
		else {
			lastClickedCategory = category;
		}
	}
	
	@Override
	public void onItemListReady() {
		checkLastClickedCategory();
	}
	
	@Override
	public void onItemCacheListReady() {
		checkLastClickedCategory();
	}
	
	private void checkLastClickedCategory() {
		if (lastClickedCategory != null) {
			addItemListFragment(lastClickedCategory);
			lastClickedCategory = null;
		}
	}
	
	private void addItemListFragment(ItemCategory category) {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.activity_alacarte_root,
						AlacarteCategoryFragment.newInstance(itemCache.getList(category.id), category))
				.addToBackStack(null).commit();
	}
	
}
