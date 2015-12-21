package hu.artklikk.android.loiti.ui.fragment.main;

import hu.artklikk.android.loiti.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_main_far_loggedin)
public class MainFarLoggedInFragment extends MainBaseFragment {
	
	@Click(R.id.fragment_main_far_loggedin_my_profile)
	void profileClicked() {
		startProfile();
	}
	
	@Click(R.id.fragment_main_far_loggedin_my_gifts)
	void giftsClicked() {
		showDialogForInside();
	}
	
	@Click(R.id.fragment_main_far_loggedin_chefs_secrets)
	void chefsSecretsClicked() {
		startChefsSecrets();
	}
	
	@Click(R.id.fragment_main_far_loggedin_daily_menu)
	void dailyMenuClicked() {
		startDailyMenu();
	}
	
	@Click(R.id.fragment_main_far_loggedin_weekly_menu)
	void weeklyMenuClicked() {
		startWeeklyMenu();
	}
	
	@Click(R.id.fragment_main_far_loggedin_a_la_carte)
	void alacarteClicked() {
		startAlacarte();
	}
	
}
