package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.UserEndpoint;
import hu.artklikk.android.loiti.backend.dto.LoginRequest;
import hu.artklikk.android.loiti.backend.dto.User;
import hu.artklikk.android.loiti.backend.dto.embeded.UserIdentity;
import hu.artklikk.android.loiti.backend.dto.enums.AuthenticationType;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.social.FBHelper;
import hu.artklikk.android.loiti.social.FBHelper.OnLoginCallback;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.widget.SlotMachine;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;

import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLogoutListener;

@EActivity(R.layout.activity_login)
public class LoginActivity extends MenuActivity {
	
	@ViewById(R.id.activity_login_email)
	EditText email;
	
	@ViewById(R.id.activity_login_slotmachine)
	SlotMachine slotMachine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		FBHelper.onActivityResult(this, requestCode, resultCode, data);
	}
	
	@Click(R.id.activity_login_login)
	void loginClicked() {
		final String passwordHexa = slotMachine.getPassword();
		
		if (isAllDataValid() && slotMachine.isMoved()) {
			
			final LoginRequest toSave = new LoginRequest();
			
			UserIdentity identity = new UserIdentity(AuthenticationType.PASSWORD);
			identity.setCredential(passwordHexa);
			List<UserIdentity> identityList = new ArrayList<UserIdentity>(1);
			identityList.add(identity);
			
			toSave.email = email.getText().toString();
			toSave.identity = identityList;
			
			UserEndpoint.login(toSave, new RestCallFinishListener() {
				
				@Override
				public void onFinish(Object result) {
					User response = (User) result;
					toSave.name = response.name;
					toSave.userId = response.id;
					LoitiApplication.setLoginData(toSave);
					performLogin(null);
					startActivity(new Intent(LoginActivity.this, ProfileActivity_.class));
				}
				
				@Override
				public void onException(Exception e) {
					e.printStackTrace();
					LoitiApplication.setLoginData(null);
					showSimpleDialog(R.string.popup_login_invalid_credentials);
				}
			});
		}
		else {
			if (!hasEmail()) {
				showSimpleDialog(R.string.popup_login_invalid_credentials);
				return;
			}
			if (!isEmailAddressFormatValid(email.getText().toString())) {
				showSimpleDialog(R.string.popup_login_invalid_credentials);
				return;
			}
			if (!slotMachine.isMoved()) {
				showSimpleDialog(R.string.popup_login_invalid_credentials);
				return;
			}
		}
	}
	
	@Click(R.id.activity_login_facebook_login)
	void facebookLoginClicked() {
		if (!FBHelper.isLogin(LoginActivity.this)) {
			FBHelper.login(LoginActivity.this, loginCallback);
		}
		else {
			FBHelper.logout(LoginActivity.this, logoutCallback);
		}
		
	}
	
	private OnLogoutListener logoutCallback = new OnLogoutListener() {
		
		@Override
		public void onFail(String reason) {
			
		}
		
		@Override
		public void onException(Throwable throwable) {
			
		}
		
		@Override
		public void onThinking() {
			
		}
		
		@Override
		public void onLogout() {
			FBHelper.login(LoginActivity.this, loginCallback);
		}
	};
	
	private OnLoginCallback loginCallback = new OnLoginCallback() {
		
		@Override
		public void onLogin(final String token, final String userId, final Profile profile) {
			
			final LoginRequest toSave = new LoginRequest();
			
			UserIdentity identity = new UserIdentity(AuthenticationType.FACEBOOK);
			identity.setCredential(token);
			identity.setIdentityId(userId);
			List<UserIdentity> identityList = new ArrayList<UserIdentity>(1);
			identityList.add(identity);
			
			toSave.email = profile.getEmail();
			toSave.identity = identityList;
			
			UserEndpoint.login(toSave, new RestCallFinishListener() {
				
				@Override
				public void onFinish(Object result) {
					User response = (User) result;
					toSave.name = response.name;
					toSave.userId = response.id;
					LoitiApplication.setLoginData(toSave);
					performLogin(null);
					startActivity(new Intent(LoginActivity.this, ProfileActivity_.class));
				}
				
				@Override
				public void onException(Exception e) {
					LoitiApplication.setLoginData(null);
					showSimpleDialog(R.string.popup_registration_email_already_registered);
				}
			});
			
		}
		
		@Override
		public void onFail(String reason) {
		}
	};
	
	private boolean isAllDataValid() {
		return hasEmail() && isEmailAddressFormatValid(email.getText().toString());
	}
	
	private boolean hasEmail() {
		return !isEmpty(email);
	}
	
	private boolean isEmailAddressFormatValid(CharSequence email) {
		if (email == null) {
			return false;
		}
		return Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
	private boolean isEmpty(final EditText editText) {
		return editText.getText().toString().trim().length() == 0;
	}
	
}
