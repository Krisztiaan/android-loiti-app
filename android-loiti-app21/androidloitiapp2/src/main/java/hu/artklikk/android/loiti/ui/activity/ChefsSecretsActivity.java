package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.ChefsSecretsWebFragment;
import android.os.Bundle;

public class ChefsSecretsActivity extends MenuActivity {
	
	private ChefsSecretsWebFragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chefs_secrets);
		if ((fragment = (ChefsSecretsWebFragment) getSupportFragmentManager().findFragmentByTag(
				ChefsSecretsWebFragment.TAG)) == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.activity_chefs_secrets_content_container, fragment = new ChefsSecretsWebFragment(),
							ChefsSecretsWebFragment.TAG).commit();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (fragment.canGoBack()) {
			fragment.goBack();
		}
		else {
			super.onBackPressed();
		}
	}
	
}
