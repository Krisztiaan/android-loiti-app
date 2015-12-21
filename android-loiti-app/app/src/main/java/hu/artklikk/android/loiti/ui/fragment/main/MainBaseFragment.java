package hu.artklikk.android.loiti.ui.fragment.main;

import hu.artklikk.android.loiti.ui.core.BackPressFragmentAwareActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;

public abstract class MainBaseFragment extends Fragment {
	
	void startRegister() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.RegistrationActivity_.class));
	}
	
	void startLogin() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.LoginActivity_.class));
	}
	
	void startProfile() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.ProfileActivity_.class));
	}
	
	void startGifts() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.GiftActivity.class));
	}
	
	void startDailyMenu() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.WeeklyMenuActivity.class));
	}
	
	void startWeeklyMenu() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.FeaturedMenuActivity.class));
	}
	
	void startAlacarte() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.AlacarteActivity_.class));
	}
	
	void startChefsSecrets() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.ChefsSecretsActivity.class));
	}
	
	void startPayment() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.PaymentActivity.class));
	}
	
	void startQrReader() {
		startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.QrReaderActivity.class));
	}
	
	void showDialogForRegister() {
		((BackPressFragmentAwareActivity) getActivity()).showDialogForRegister();
	}
	
	void showDialogForInside() {
		((BackPressFragmentAwareActivity) getActivity()).showDialogForInside();
	}
	
}
