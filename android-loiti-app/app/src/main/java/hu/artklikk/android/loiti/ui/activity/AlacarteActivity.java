package hu.artklikk.android.loiti.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

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

@EActivity(R.layout.activity_alacarte)
public class AlacarteActivity extends MenuActivity implements CategoryListCallback, ItemListCallback,
        CategoryClickListener {

    private static final String KEY_LAST_CLICKED = "KEY_LAST_CLICKED";
    @ViewById(R.id.activity_alacarte_list)
    RecyclerView list;
    @ViewById(R.id.activity_alacarte_refresh)
    SwipeRefreshLayout refreshLayout;
    private AlacarteCategoryCacheFragment categoryCache;
    private ItemCacheFragment itemCache;
    private ItemCategory lastClickedCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_LAST_CLICKED)) {
                lastClickedCategory = savedInstanceState.getParcelable(KEY_LAST_CLICKED);
            }
        }
    }

    protected void initCache() {
        if (!refreshLayout.isRefreshing()) refreshLayout.setRefreshing(true);

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

        if (categoryCache.isListReady()) {
            if (list.getAdapter() != null)
                ((AlacarteAdapter) list.getAdapter()).replaceList(categoryCache.getList());
            else
                list.setAdapter(new AlacarteAdapter(categoryCache.getList(), this));
        } else if (categoryCache.isListCacheReady()) {
            if (list.getAdapter() != null)
                ((AlacarteAdapter) list.getAdapter()).replaceList(categoryCache.getListCache());
            else
                list.setAdapter(new AlacarteAdapter(categoryCache.getListCache(), this));
        }

        refreshLayout.setRefreshing(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(KEY_LAST_CLICKED, lastClickedCategory);
    }

    @AfterViews
    void setupList() {

        initCache();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSupportFragmentManager().beginTransaction().remove(categoryCache).commit();
                initCache();
            }
        });
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
    public void onCategoryClick(ItemCategory category, View... views) {
        if (itemCache.isListReady() || itemCache.isListCacheReady()) {
            addItemListFragment(category, views);
        } else {
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

    private void addItemListFragment(ItemCategory category, View... sharedElements) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .add(R.id.activity_alacarte_root,
                        AlacarteCategoryFragment.newInstance(itemCache.getList(category.id), category));
        if (sharedElements != null) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            for (View v :
                    sharedElements) {
                transaction
                        .addSharedElement(v, v.getTransitionName());
            }
        }
        transaction.addToBackStack(null).commit();
    }
}
