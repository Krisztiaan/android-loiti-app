package hu.artklikk.android.loiti.ui.core;

import hu.artklikk.android.loiti.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public abstract class MenuActivity extends BackPressFragmentAwareActivity {
	
	protected View menuIcon;
	private ViewGroup content;
	protected ViewGroup drawerContent;
	private DrawerLayout drawerLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.setContentView(R.layout.activity_menu);
		
		menuIcon = findViewById(R.id.menu_icon);
		menuIcon.setOnClickListener(menuIconClickListener);
		
		content = (ViewGroup) findViewById(R.id.content_frame);
		drawerContent = (ViewGroup) findViewById(R.id.drawer);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerListener(drawerListener);
		drawerLayout.setScrimColor(getResources().getColor(R.color.shell_white_60));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (drawerLayout.isDrawerOpen(Gravity.END)) {
			menuIcon.post(new Runnable() {
				@Override
				public void run() {
					menuIcon.setTranslationX(-(drawerLayout.getMeasuredWidth() - menuIcon.getMeasuredWidth()));
				}
			});
		}
	}
	
	public void setContentView(int layoutResID) {
		content.removeAllViews();
		getLayoutInflater().inflate(layoutResID, content, true);
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		content.removeAllViews();
		content.addView(view, params);
	}
	
	@Override
	public void setContentView(View view) {
		setContentView(view, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	@Override
	public void replaceMenuFragment(Fragment fragment, String tag) {
		getSupportFragmentManager().beginTransaction().replace(R.id.drawer, fragment, tag).commit();
	}
	
	public void setDrawerEnabled(boolean isEnabled) {
		if (isEnabled) {
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			menuIcon.setVisibility(View.VISIBLE);
		}
		else {
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			menuIcon.setVisibility(View.GONE);
		}
	}
	
	private DrawerListener drawerListener = new DrawerListener() {
		
		@Override
		public void onDrawerStateChanged(int state) {
		}
		
		@Override
		public void onDrawerSlide(View drawer, float offset) {
			float moveFactor = (drawerContent.getWidth() * offset);
			menuIcon.setTranslationX(-moveFactor);
		}
		
		@Override
		public void onDrawerOpened(View drawer) {
		}
		
		@Override
		public void onDrawerClosed(View drawer) {
		}
	};
	
	private OnClickListener menuIconClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (drawerLayout.isDrawerOpen(Gravity.END)) {
				drawerLayout.closeDrawer(Gravity.END);
			}
			else {
				drawerLayout.openDrawer(Gravity.END);
			}
		}
	};
	
	public void closeDrawer() {
		drawerLayout.closeDrawer(Gravity.END);
	}
	
	@Override
	public int getFragmentContainerViewId() {
		return R.id.content_frame;
	}
	
}
