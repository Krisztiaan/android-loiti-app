package hu.artklikk.android.loiti.ui.core;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.backend.UserEndpoint;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import android.os.Bundle;

public abstract class AutoLoginActivity extends PlayServiceActivity {
	
	private static boolean isLastLoginSuccessful = false;
	private static boolean isCurrentLoginPending = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (LoitiApplication.hasLoginData() && !isCurrentLoginPending && !isLastLoginSuccessful) {
			performLogin(getLoginCallback());
		}
	}
	
	protected void performLogin(final RestCallFinishListener callback) {
		if (LoitiApplication.hasLoginData()) {
			RestCallFinishListener loginCallback = new RestCallFinishListener() {
				
				@Override
				public void onFinish(Object result) {
					isLastLoginSuccessful = true;
					isCurrentLoginPending = false;
					if(getLoginCallback() != null) {
						getLoginCallback().onFinish(result);
					}
					if (callback != null) {
						callback.onFinish(result);
					}
				}
				
				@Override
				public void onException(Exception e) {
					isLastLoginSuccessful = false;
					isCurrentLoginPending = false;
					if(getLoginCallback() != null) {
						getLoginCallback().onException(e);
					}
					if (callback != null) {
						callback.onException(e);
					}
				}
			};
			isCurrentLoginPending = true;
			UserEndpoint.login(LoitiApplication.getLoginData(), loginCallback);
		}
		else {
			if (callback != null) {
				callback.onException(new NullPointerException("Login data is not present!"));
			}
		}
	}
	
	protected abstract RestCallFinishListener getLoginCallback();
	
	protected boolean isLastLoginSuccessful() {
		return isLastLoginSuccessful;
	}
	
	protected boolean isCurrentLoginPending() {
		return isCurrentLoginPending;
	}
}
