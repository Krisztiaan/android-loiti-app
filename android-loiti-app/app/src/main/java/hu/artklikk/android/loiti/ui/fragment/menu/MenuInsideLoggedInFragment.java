package hu.artklikk.android.loiti.ui.fragment.menu;

import hu.artklikk.android.loiti.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuInsideLoggedInFragment extends MenuBaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_menu, container, false);
		root.findViewById(R.id.fragment_menu_main).setOnClickListener(mainClickListener);
		root.findViewById(R.id.fragment_menu_payment).setOnClickListener(paymentClickListener);
		root.findViewById(R.id.fragment_menu_qr_reader).setOnClickListener(qrReaderClickListener);
		root.findViewById(R.id.fragment_menu_visitors_book).setOnClickListener(visitorsBookClickListener);
		root.findViewById(R.id.fragment_menu_profile).setOnClickListener(profileClickListener);
		root.findViewById(R.id.fragment_menu_gifts).setOnClickListener(giftsClickListener);
		root.findViewById(R.id.fragment_menu_daily_menu).setOnClickListener(dailyMenuClickListener);
		root.findViewById(R.id.fragment_menu_weekly_menu).setOnClickListener(weeklyMenuClickListener);
		root.findViewById(R.id.fragment_menu_alacarte).setOnClickListener(alacarteClickListener);
		root.findViewById(R.id.fragment_menu_chefs_secrets).setOnClickListener(chefsSecretsClickListener);
		root.findViewById(R.id.fragment_menu_faq).setOnClickListener(faqClickListener);
		root.findViewById(R.id.fragment_menu_contact).setOnClickListener(contactClickListener);
		root.findViewById(R.id.fragment_menu_terms_conditions).setOnClickListener(termsConditionsClickListener);		
		return root;
	}

}
