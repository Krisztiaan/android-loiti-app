package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.MainVideoFragment;

import org.androidannotations.annotations.EActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

@EActivity(R.layout.activity_main)
public class MainActivity extends MenuActivity {
	
	private static final String KEY_CURRENT_SCREEN = "KEY_CURRENT_SCREEN";
	private Screen currentScreen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setDrawerEnabled(false);
		
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(KEY_CURRENT_SCREEN)) {
				Screen toRestore = (Screen) savedInstanceState.getSerializable(KEY_CURRENT_SCREEN);
				if (toRestore != null) {
					currentScreen = toRestore;
				}
			}
		}
		
		Fragment videoFragment = getSupportFragmentManager().findFragmentByTag(MainVideoFragment.TAG);
		if (videoFragment == null) {
			videoFragment = new MainVideoFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.activity_main_video_container, videoFragment, MainVideoFragment.TAG).commit();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putSerializable(KEY_CURRENT_SCREEN, currentScreen);
	}
	
	@Override
	public void replaceFragment(Fragment fragment, String tag) {
		if (!isCurrentLoginPending()) {
			getSupportFragmentManager().beginTransaction()
					.setCustomAnimations(R.anim.main_fragment_in, R.anim.main_fragment_out)
					.replace(R.id.fragment_container, fragment, tag).commit();
		}
	}
	
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		if (currentScreen != null && currentScreen != getCurrentScreen()) {
			sendReplaceFragments();
		}
	}
	
	@Override
	public void onPause() {
		currentScreen = getCurrentScreen();
		super.onPause();
	}
	
	public static Intent createGoBackIntent(Context context) {
		return new Intent(context, hu.artklikk.android.loiti.ui.activity.MainActivity_.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}
	
}
