package hu.artklikk.android.loiti.ui.fragment.menu;

import hu.artklikk.android.loiti.ui.activity.FeaturedMenuActivity;
import hu.artklikk.android.loiti.ui.activity.GiftActivity;
import hu.artklikk.android.loiti.ui.activity.MainActivity;
import hu.artklikk.android.loiti.ui.activity.PaymentActivity;
import hu.artklikk.android.loiti.ui.activity.QrReaderActivity;
import hu.artklikk.android.loiti.ui.activity.VisitorsBookActivity;
import hu.artklikk.android.loiti.ui.activity.WeeklyMenuActivity;
import hu.artklikk.android.loiti.ui.core.BackPressFragmentAwareActivity;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class MenuBaseFragment extends Fragment {
	
	void startMain() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(MainActivity.createGoBackIntent(getActivity()));
		}
	}
	
	void startRegistration() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.RegistrationActivity_.class));
		}
	}
	
	void startLogin() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.LoginActivity_.class));
		}
	}
	
	void startProfile() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.ProfileActivity_.class));
		}
	}
	
	void startGifts() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), GiftActivity.class));
		}
	}
	
	void startDailyMenu() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), WeeklyMenuActivity.class));
		}
	}
	
	void startWeeklyMenu() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), FeaturedMenuActivity.class));
		}
	}
	
	void startAlacarte() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.AlacarteActivity_.class));
		}
	}
	
	void startChefsSecrets() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.ChefsSecretsActivity.class));
		}
	}
	
	void startFaq() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.FaqActivity.class));
		}
	}
	
	void startContact() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.ContactActivity.class));
		}
	}
	
	void startTermsConditions() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), hu.artklikk.android.loiti.ui.activity.TermsConditionsActivity.class));
		}
	}
	
	void startPayment() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), PaymentActivity.class));
		}
	}
	
	void startQrReader() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), QrReaderActivity.class));
		}
	}
	
	void startVisitorsBook() {
		closeDrawer();
		if (getActivity() != null) {
			startActivity(new Intent(getActivity(), VisitorsBookActivity.class));
		}
	}
	
	private void closeDrawer() {
		if (getActivity() != null && getActivity() instanceof MenuActivity) {
			((MenuActivity) getActivity()).closeDrawer();
		}
	}
	
	OnClickListener mainClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startMain();
		}
	};
	
	OnClickListener registrationClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startRegistration();
		}
	};
	
	OnClickListener loginClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startLogin();
		}
	};
	
	OnClickListener profileClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startProfile();
		}
	};
	
	OnClickListener giftsClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startGifts();
		}
	};
	
	OnClickListener dailyMenuClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startDailyMenu();
		}
	};
	
	OnClickListener weeklyMenuClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startWeeklyMenu();
		}
	};
	
	OnClickListener alacarteClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startAlacarte();
		}
	};
	
	OnClickListener chefsSecretsClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startChefsSecrets();
		}
	};
	
	OnClickListener faqClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startFaq();
		}
	};
	
	OnClickListener contactClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startContact();
		}
	};
	
	OnClickListener termsConditionsClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startTermsConditions();
		}
	};
	
	OnClickListener paymentClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startPayment();
		}
	};
	
	OnClickListener qrReaderClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startQrReader();
		}
	};
	
	OnClickListener visitorsBookClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startVisitorsBook();
		}
	};
	
	
	OnClickListener registerPopupClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			closeDrawer();
			showDialogForRegister();
		}
	};
	
	OnClickListener insidePopupClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			closeDrawer();
			showDialogForInside();
		}
	};
	
	private void showDialogForRegister() {
		((BackPressFragmentAwareActivity) getActivity()).showDialogForRegister();
	}
	
	private void showDialogForInside() {
		((BackPressFragmentAwareActivity) getActivity()).showDialogForInside();
	}
	
}
