package hu.artklikk.android.loiti.ui.fragment.main;

import hu.artklikk.android.loiti.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_main_inside_loggedout)
public class MainInsideLoggedOutFragment extends MainBaseFragment {
	
	@Click(R.id.fragment_main_inside_loggedout_register)
	void registerClicked() {
		startRegister();
	}
	
	@Click(R.id.fragment_main_inside_loggedout_login)
	void loginClicked() {
		startLogin();
	}
	
	@Click(R.id.fragment_main_inside_loggedout_payment)
	void paymentClicked() {
		showDialogForRegister();
	}
	
	@Click(R.id.fragment_main_inside_loggedout_qr_reader)
	void qrReaderClicked() {
		showDialogForRegister();
	}
	
	@Click(R.id.fragment_main_inside_loggedout_my_profile)
	void profileClicked() {
		showDialogForRegister();
	}
	
	@Click(R.id.fragment_main_inside_loggedout_my_gifts)
	void giftsClicked() {
		showDialogForRegister();
	}
	
	@Click(R.id.fragment_main_inside_loggedout_daily_menu)
	void dailyMenuClicked() {
		startDailyMenu();
	}
	
	@Click(R.id.fragment_main_inside_loggedout_weekly_menu)
	void weeklyMenuClicked() {
		startWeeklyMenu();
	}
	
	@Click(R.id.fragment_main_inside_loggedout_a_la_carte)
	void alacarteClicked() {
		startAlacarte();
	}
	
}
