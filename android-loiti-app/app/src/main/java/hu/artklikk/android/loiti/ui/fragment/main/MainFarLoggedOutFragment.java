package hu.artklikk.android.loiti.ui.fragment.main;

import hu.artklikk.android.loiti.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_main_far_loggedout)
public class MainFarLoggedOutFragment extends MainBaseFragment {
	
	@Click(R.id.fragment_main_far_loggedout_register)
	void registerClicked() {
		startRegister();
	}
	
	@Click(R.id.fragment_main_far_loggedout_login)
	void loginClicked() {
		startLogin();
	}
	
	@Click(R.id.fragment_main_far_loggedout_my_profile)
	void profileClicked() {
		showDialogForRegister();
	}
	
	@Click(R.id.fragment_main_far_loggedout_my_gifts)
	void giftsClicked() {
		showDialogForRegister();
	}
	
	@Click(R.id.fragment_main_far_loggedout_daily_menu)
	void dailyMenuClicked() {
		startDailyMenu();
	}
	
	@Click(R.id.fragment_main_far_loggedout_weekly_menu)
	void weeklyMenuClicked() {
		startWeeklyMenu();
	}
	
	@Click(R.id.fragment_main_far_loggedout_a_la_carte)
	void alacarteClicked() {
		startAlacarte();
	}
	
}
