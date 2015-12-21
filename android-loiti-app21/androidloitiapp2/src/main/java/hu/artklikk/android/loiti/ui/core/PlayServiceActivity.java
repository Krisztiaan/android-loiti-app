package hu.artklikk.android.loiti.ui.core;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PlayServiceActivity extends FragmentActivity {

	/** Request code to Google Play Services check. **/
	private static final int GOOGLE_PLAY_SERVICES_CHECK_REQUEST_CODE = 42;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// if the app is launching now
		if (isAppLaunching()) 
			checkPlayService();
		
	}
	
	protected boolean hasPlayService() {
		return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
	}
	
	private boolean checkPlayService() {
		final int googlePlayStatus;
		if ((googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) != ConnectionResult.SUCCESS) {
			// something is wrong with Google Play Services
			if (GooglePlayServicesUtil.isUserRecoverableError(googlePlayStatus)) {
				// user can do something with it, pop up a dialog
				// startApp comes from a thread, dialog needs UI thread
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, PlayServiceActivity.this,
								GOOGLE_PLAY_SERVICES_CHECK_REQUEST_CODE, new OnCancelListener() {
									
									@Override
									public void onCancel(DialogInterface dialog) {
										// user was dumb and cancelled the
										// dialog, quit here
										finish();
									}
								}).show();
						
					}
				});
			}
			else {
				// user can do nothing with it
				finish();
			}
			
			return false;
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// handles the play service recover result
		if (requestCode == GOOGLE_PLAY_SERVICES_CHECK_REQUEST_CODE) {
			// user returned from play services recovery
			if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
				// user did not fixed the error, quit for now
				finish();
				
			}
			else {
				// if the error was fixed 
				onPlayServiceErrorFixed();
			}
		}
	}
	
	/**
	 * True if this is an launching.
	 * 
	 * @return True if the Intent of the activity contains {@link Intent#ACTION_MAIN}.
	 */
	protected boolean isAppLaunching() {
		// if the app is launching now
		Intent mainIntent = getIntent();
		String action = mainIntent != null ? mainIntent.getAction() : null;
		return action != null && action.equals(Intent.ACTION_MAIN); 
	}
	
	/** 
	 * This method should override to detect if the play service has error and it fixed.
	 * You should call super!
	 */
	protected void onPlayServiceErrorFixed() {}
}
