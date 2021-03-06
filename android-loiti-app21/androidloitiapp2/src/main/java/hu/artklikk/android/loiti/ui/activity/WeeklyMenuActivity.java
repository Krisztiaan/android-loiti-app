package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.VenueEndpoint.WeeklyItemsContainer;
import hu.artklikk.android.loiti.ui.adapter.WeeklyAdapter;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.WeeklyCacheFragment;
import hu.artklikk.android.loiti.ui.fragment.WeeklyCacheFragment.WeeklyCacheCallback;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class WeeklyMenuActivity extends MenuActivity implements WeeklyCacheCallback {
	
	private RecyclerView list;
	private TextView intervalText;
	
	private WeeklyCacheFragment cache;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weekly_menu);
		list = (RecyclerView) findViewById(R.id.activity_weekly_menu_list);
		intervalText = (TextView) findViewById(R.id.activity_weekly_menu_interval);
		
		if ((cache = (WeeklyCacheFragment) getSupportFragmentManager().findFragmentByTag(WeeklyCacheFragment.TAG)) == null) {
			cache = new WeeklyCacheFragment();
			getSupportFragmentManager().beginTransaction().add(cache, WeeklyCacheFragment.TAG).commit();
		}
		
		if(cache.isReady()) {
			setViews(cache.getContainer());
		}
		else if(cache.isCacheReady()) {
			setViews(cache.getContainerCache());
		}
	}
	
	@Override
	public void onReady(WeeklyItemsContainer result) {
		if (!result.equals(cache.getContainerCache())) {
			setViews(result);
		}
	}
	
	@Override
	public void onCacheReady(WeeklyItemsContainer result) {
		setViews(result);
	}
	
	private void setViews(WeeklyItemsContainer container) {
		list.setAdapter(new WeeklyAdapter(container, getResources().getStringArray(R.array.weekly_menu_day_names)));
		intervalText.setText(container.getInterval());
	}
}
