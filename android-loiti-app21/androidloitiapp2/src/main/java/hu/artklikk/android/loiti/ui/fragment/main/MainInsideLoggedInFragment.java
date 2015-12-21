package hu.artklikk.android.loiti.ui.fragment.main;

import hu.artklikk.android.loiti.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_main_inside_loggedin)
public class MainInsideLoggedInFragment extends MainBaseFragment {
	
	@Click(R.id.fragment_main_inside_loggedin_payment)
	void paymentClicked() {
		startPayment();
	}
	
	@Click(R.id.fragment_main_inside_loggedin_qr_reader)
	void qrReaderClicked() {
		startQrReader();
	}
	
	@Click(R.id.fragment_main_inside_loggedin_my_profile)
	void profileClicked() {
		startProfile();
	}
	
	@Click(R.id.fragment_main_inside_loggedin_my_gifts)
	void giftsClicked() {
		startGifts();
	}
	
	@Click(R.id.fragment_main_inside_loggedin_chefs_secrets)
	void chefsSecretsClicked() {
		startChefsSecrets();
	}
	
	@Click(R.id.fragment_main_inside_loggedin_daily_menu)
	void dailyMenuClicked() {
		startDailyMenu();
	}
	
	@Click(R.id.fragment_main_inside_loggedin_weekly_menu)
	void weeklyMenuClicked() {
		startWeeklyMenu();
	}
	
	@Click(R.id.fragment_main_inside_loggedin_a_la_carte)
	void alacarteClicked() {
		startAlacarte();
	}
	
}
