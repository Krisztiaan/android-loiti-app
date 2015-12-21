package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.GiftsBasicFragment;
import hu.artklikk.android.loiti.ui.fragment.GiftsDataFragment;
import hu.artklikk.android.loiti.ui.fragment.GiftsDataFragment.GiftDescriptor;
import hu.artklikk.android.loiti.ui.fragment.GiftsDataFragment.GiftDescriptorReadyListener;
import hu.artklikk.android.loiti.ui.fragment.GiftsTopLevelFragment;
import hu.artklikk.android.loiti.ui.fragment.SimpleDialogFragment;
import android.os.Bundle;

public class GiftActivity extends MenuActivity implements GiftDescriptorReadyListener {
	
	private static final String TAG_NOT_VISITED = "TAG_NOT_VISITED";
	
	private GiftsDataFragment dataFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_gifts);
		
		if ((dataFragment = (GiftsDataFragment) getSupportFragmentManager().findFragmentByTag(GiftsDataFragment.TAG)) == null) {
			dataFragment = new GiftsDataFragment();
			getSupportFragmentManager().beginTransaction().add(dataFragment, GiftsDataFragment.TAG).commit();
		}
		
	}
	
	@Override
	public void onGiftDescriptorReady() {
		GiftDescriptor descriptor = dataFragment.getGiftDescriptor(isInside());
		if (descriptor.isTopLevel()) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.activity_gifts_root,
							GiftsTopLevelFragment.create(dataFragment.getGiftDescriptor(isInside())),
							GiftsTopLevelFragment.TAG).commit();
		}
		else {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.activity_gifts_root,
							GiftsBasicFragment.create(dataFragment.getGiftDescriptor(isInside())),
							GiftsBasicFragment.TAG).commit();
			
		}
	}
	
	@Override
	public void onUserNotVisitedReady() {
		getSupportFragmentManager().beginTransaction()
				.add(R.id.activity_gifts_root, GiftsBasicFragment.create(), GiftsBasicFragment.TAG).commit();
		showDialog(SimpleDialogFragment.create(R.string.popup_gifts_not_visited), TAG_NOT_VISITED);
	}
	
	@Override
	public void onAgree(SimpleDialogFragment caller) {
		super.onAgree(caller);
		if (TAG_NOT_VISITED.equals(caller.getTag())) {
			startActivity(MainActivity.createGoBackIntent(this));
		}
	}
	
	@Override
	public void onCancel(SimpleDialogFragment caller) {
		// not calling super - prevent closing
	}
	
}
