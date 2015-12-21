package hu.artklikk.android.loiti.ui.core;

import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.ui.fragment.menu.MenuFarLoggedInFragment;
import hu.artklikk.android.loiti.ui.fragment.menu.MenuFarLoggedOutFragment;
import hu.artklikk.android.loiti.ui.fragment.menu.MenuInsideLoggedInFragment;
import hu.artklikk.android.loiti.ui.fragment.menu.MenuInsideLoggedOutFragment;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class FragmentSwitcherActivity extends AutoLoginActivity {
	
	private static final String STATE_LAST_SCREEN = "LAST_SCREEN";
	
	private final Screen LOCK_SCREEN_TO = Screen.FAR_LOGGEDOUT;
	
	public enum Screen {
		FAR_LOGGEDIN,
		FAR_LOGGEDOUT,
		INSIDE_LOGGEDIN,
		INSIDE_LOGGEDOUT;
		
		public String getTag() {
			return this.name().toUpperCase(Locale.ENGLISH);
		}
		
		public String getMenuTag() {
			return "MENU_" + this.name().toUpperCase(Locale.ENGLISH);
		}
		
		public Screen getScreenIfLoginChanges() {
			switch (this) {
				case FAR_LOGGEDIN:
					return FAR_LOGGEDOUT;
				case FAR_LOGGEDOUT:
					return FAR_LOGGEDIN;
				case INSIDE_LOGGEDIN:
					return INSIDE_LOGGEDOUT;
				case INSIDE_LOGGEDOUT:
					return INSIDE_LOGGEDIN;
				default:
					return this;
			}
		}
		
		public Screen getScreenIfLoginSucceeds() {
			switch (this) {
				case FAR_LOGGEDIN:
					return FAR_LOGGEDIN;
				case FAR_LOGGEDOUT:
					return FAR_LOGGEDIN;
				case INSIDE_LOGGEDIN:
					return INSIDE_LOGGEDIN;
				case INSIDE_LOGGEDOUT:
					return INSIDE_LOGGEDIN;
				default:
					return this;
			}
		}
		
		public Screen getScreenIfLoginFails() {
			switch (this) {
				case FAR_LOGGEDIN:
					return FAR_LOGGEDOUT;
				case FAR_LOGGEDOUT:
					return FAR_LOGGEDOUT;
				case INSIDE_LOGGEDIN:
					return INSIDE_LOGGEDOUT;
				case INSIDE_LOGGEDOUT:
					return INSIDE_LOGGEDOUT;
				default:
					return this;
			}
		}
		
	}
	
	private static Screen currentScreen = Screen.FAR_LOGGEDOUT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			forceChangeScreenTo(currentScreen);
		}
		else {
			Screen savedState = (Screen) savedInstanceState.getSerializable(FragmentSwitcherActivity.STATE_LAST_SCREEN);
			if (savedState != null) {
				currentScreen = savedState;
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putSerializable(FragmentSwitcherActivity.STATE_LAST_SCREEN, currentScreen);
	}
	
	public void changeScreenTo(Screen screen) {
		// System.out.println("change to: " + screen + " from: " +
		// currentScreen);
		if (currentScreen == screen) {
			return;
		}
		// if(LOCK_SCREEN_TO != null) {
		// screen = LOCK_SCREEN_TO;
		// }
		forceChangeScreenTo(screen);
	}
	
	public void sendReplaceFragments() {
		forceChangeScreenTo(currentScreen);
	}
	
	private void forceChangeScreenTo(Screen screen) {
		Fragment toChange;
		if ((toChange = getSupportFragmentManager().findFragmentByTag(screen.getTag())) == null) {
			
			switch (screen) {
			
				case FAR_LOGGEDIN:
					toChange = new hu.artklikk.android.loiti.ui.fragment.main.MainFarLoggedInFragment_();
					break;
				case FAR_LOGGEDOUT:
					toChange = new hu.artklikk.android.loiti.ui.fragment.main.MainFarLoggedOutFragment_();
					break;
				case INSIDE_LOGGEDIN:
					toChange = new hu.artklikk.android.loiti.ui.fragment.main.MainInsideLoggedInFragment_();
					break;
				case INSIDE_LOGGEDOUT:
					toChange = new hu.artklikk.android.loiti.ui.fragment.main.MainInsideLoggedOutFragment_();
					break;
			}
		}
		replaceFragment(toChange, screen.getTag());
		
		Fragment toChangeMenu;
		if ((toChangeMenu = getSupportFragmentManager().findFragmentByTag(screen.getMenuTag())) == null) {
			
			switch (screen) {
			
				case FAR_LOGGEDIN:
					toChangeMenu = new MenuFarLoggedInFragment();
					break;
				case FAR_LOGGEDOUT:
					toChangeMenu = new MenuFarLoggedOutFragment();
					break;
				case INSIDE_LOGGEDIN:
					toChangeMenu = new MenuInsideLoggedInFragment();
					break;
				case INSIDE_LOGGEDOUT:
					toChangeMenu = new MenuInsideLoggedOutFragment();
					break;
			}
		}
		replaceMenuFragment(toChangeMenu, screen.getMenuTag());
		currentScreen = screen;
	}
	
	public void replaceFragment(Fragment fragment, String tag) {
	};
	
	public void replaceMenuFragment(Fragment fragment, String tag) {
	};
	
	protected Screen getCurrentScreen() {
		return currentScreen;
	}
	
	@Override
	protected RestCallFinishListener getLoginCallback() {
		return loginCallback;
	}
	
	private RestCallFinishListener loginCallback = new RestCallFinishListener() {
		
		@Override
		public void onFinish(Object result) {
			if (currentScreen != null) {
				changeScreenTo(currentScreen.getScreenIfLoginSucceeds());
			}
		}
		
		@Override
		public void onException(Exception e) {
			if (currentScreen != null) {
				changeScreenTo(currentScreen.getScreenIfLoginFails());
			}
		}
	};
	
}
