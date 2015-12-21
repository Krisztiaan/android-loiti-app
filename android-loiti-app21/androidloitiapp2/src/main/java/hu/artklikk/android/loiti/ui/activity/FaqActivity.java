package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.FaqWebFragment;
import android.os.Bundle;

public class FaqActivity extends MenuActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		if ((getSupportFragmentManager().findFragmentByTag(FaqWebFragment.TAG)) == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.activity_faq_content_container, new FaqWebFragment(), FaqWebFragment.TAG).commit();
		}
	}
	
}
