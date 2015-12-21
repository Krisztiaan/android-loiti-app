package hu.artklikk.android.loiti.ui.core;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.activity.MainActivity;
import hu.artklikk.android.loiti.ui.fragment.SimpleDialogFragment;
import hu.artklikk.android.loiti.ui.fragment.SimpleDialogFragment.DialogDescriptor;
import hu.artklikk.android.loiti.ui.fragment.SimpleDialogFragment.SimpleDialogListener;
import android.content.Intent;
import android.support.v4.app.Fragment;

public abstract class BackPressFragmentAwareActivity extends FenceAwareActivity implements SimpleDialogListener {
	
	private static final String TAG_REGISTER_DIALOG = "TAG_REGISTER_DIALOG";
	private static final String TAG_INSIDE_DIALOG = "TAG_INSIDE_DIALOG";
	
	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			super.onBackPressed();
		}
		else {
			Fragment fragment = getSupportFragmentManager().findFragmentById(getFragmentContainerViewId());
			if (fragment instanceof BackPressAware) {
				((BackPressAware) fragment).onBackPressed();
			}
			else {
				super.onBackPressed();
			}
		}
		
	}
	
	public abstract int getFragmentContainerViewId();
	
	@Override
	public void onAgree(SimpleDialogFragment caller) {
		getSupportFragmentManager().popBackStack();
		if(TAG_REGISTER_DIALOG.equals(caller.getTag())) {
			startActivity(new Intent(this, hu.artklikk.android.loiti.ui.activity.RegistrationActivity_.class));
		}
		if(TAG_INSIDE_DIALOG.equals(caller.getTag())) {
			startActivity(MainActivity.createGoBackIntent(this));
		}
	}
	
	@Override
	public void onCancel(SimpleDialogFragment caller) {
		getSupportFragmentManager().popBackStack();
	}
	
	public void showSimpleDialog(int messageId) {
		showDialog(SimpleDialogFragment.create(messageId));
	}
	
	public void showDialog(SimpleDialogFragment dialog) {
		getSupportFragmentManager().beginTransaction()
				.add(getFragmentContainerViewId(), dialog, SimpleDialogFragment.TAG).addToBackStack(null).commit();
		getSupportFragmentManager().executePendingTransactions();
	}
	
	public void showDialog(SimpleDialogFragment dialog, String tag) {
		getSupportFragmentManager().beginTransaction().add(getFragmentContainerViewId(), dialog, tag)
				.addToBackStack(null).commit();
		getSupportFragmentManager().executePendingTransactions();
	}
	
	public void showDialogForRegister() {
		showDialog(SimpleDialogFragment.create(new DialogDescriptor().setCancelVisible(true)
				.setMessageId(R.string.popup_register).setOkStringId(R.string.popup_register_agree_text)), TAG_REGISTER_DIALOG);
	}
	
	public void showDialogForInside() {
		showDialog(SimpleDialogFragment.create(R.string.popup_inside_only), TAG_INSIDE_DIALOG);
	}
}
