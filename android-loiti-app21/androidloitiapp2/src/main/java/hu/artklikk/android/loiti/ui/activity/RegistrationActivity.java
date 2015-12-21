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
import hu.artklikk.android.loiti.ui.fragment.RegistrationPhotoUploaderFragment;
import hu.artklikk.android.loiti.ui.fragment.RegistrationPhotoUploaderFragment.PhotoCallback;
import hu.artklikk.android.loiti.ui.widget.SlotMachine;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLogoutListener;

@EActivity(R.layout.activity_registration)
public class RegistrationActivity extends MenuActivity implements PhotoCallback {
	
	@ViewById(R.id.activity_registration_name)
	EditText name;
	
	@ViewById(R.id.activity_registration_email)
	EditText email;
	
	@ViewById(R.id.activity_registration_slotmachine)
	SlotMachine slotMachine;
	
	@ViewById(R.id.activity_registration_register_progress)
	View registerProgressBar;
	
	private RegistrationPhotoUploaderFragment photoWorkerFragment;
	
	private static final String KEY_FB_LOGIN = "KEY_FB_LOGIN";
	private static final String KEY_FB_UPLOAD = "KEY_FB_UPLOAD";
	
	private boolean isFBLoginCompleted;
	private boolean isFBUploadCompleted;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			isFBLoginCompleted = savedInstanceState.getBoolean(KEY_FB_LOGIN);
			isFBUploadCompleted = savedInstanceState.getBoolean(KEY_FB_UPLOAD);
		}
		RegistrationPhotoUploaderFragment fragment = (RegistrationPhotoUploaderFragment) getSupportFragmentManager()
				.findFragmentByTag(RegistrationPhotoUploaderFragment.TAG);
		if (fragment == null) {
			fragment = new RegistrationPhotoUploaderFragment();
			getSupportFragmentManager().beginTransaction().add(fragment, RegistrationPhotoUploaderFragment.TAG)
					.commit();
		}
		photoWorkerFragment = fragment;
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean(KEY_FB_LOGIN, isFBLoginCompleted);
		savedInstanceState.putBoolean(KEY_FB_UPLOAD, isFBUploadCompleted);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		FBHelper.onActivityResult(this, requestCode, resultCode, data);
	}
	
	@Click(R.id.activity_registration_register)
	void registerClicked() {
		final String passwordHexa = slotMachine.getPassword();
		
		if (isAllDataValid() && slotMachine.isMoved()) {
			registerProgressBar.setVisibility(View.VISIBLE);
			UserEndpoint.registerWithSlotMachine(name.getText().toString(), passwordHexa, email.getText().toString(),
					new RestCallFinishListener() {
						
						@Override
						public void onFinish(Object result) {
							final User response = (User) result;
							
							LoginRequest toSave = new LoginRequest();
							
							UserIdentity identity = new UserIdentity(AuthenticationType.PASSWORD);
							identity.setCredential(passwordHexa);
							List<UserIdentity> identityList = new ArrayList<UserIdentity>(1);
							identityList.add(identity);
							
							toSave.email = response.email;
							toSave.identity = identityList;
							toSave.name = response.name;
							toSave.userId = response.id;
							
							LoitiApplication.setLoginData(toSave);
							
							performLogin(new RestCallFinishListener() {
								
								@Override
								public void onFinish(Object result) {
									registerProgressBar.setVisibility(View.GONE);
									startActivity(new Intent(RegistrationActivity.this, ProfileActivity_.class));
								}
								
								@Override
								public void onException(Exception e) {
									e.printStackTrace();
									registerProgressBar.setVisibility(View.GONE);
								}
							});
							
						}
						
						@Override
						public void onException(Exception e) {
							e.printStackTrace();
							registerProgressBar.setVisibility(View.GONE);
							showSimpleDialog(R.string.popup_registration_email_already_registered);
						}
					});
		}
		else {
			if (!hasName()) {
				showSimpleDialog(R.string.popup_registration_name_required);
				return;
			}
			if (!hasEmail()) {
				showSimpleDialog(R.string.popup_registration_email_required);
				return;
			}
			if (!isEmailAddressFormatValid(email.getText().toString())) {
				showSimpleDialog(R.string.popup_registration_email_invalid_format);
				return;
			}
			if (!slotMachine.isMoved()) {
				showSimpleDialog(R.string.popup_registration_password_required);
				return;
			}
		}
	}
	
	@Click(R.id.activity_registration_facebook_login)
	void facebookLoginClicked() {
		registerProgressBar.setVisibility(View.VISIBLE);
		if (!FBHelper.isLogin(RegistrationActivity.this)) {
			FBHelper.login(RegistrationActivity.this, loginCallback);
		}
		else {
			FBHelper.logout(RegistrationActivity.this, logoutCallback);
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
			FBHelper.login(RegistrationActivity.this, loginCallback);
		}
	};
	
	private OnLoginCallback loginCallback = new OnLoginCallback() {
		
		@Override
		public void onLogin(final String token, final String userId, final Profile profile) {
			UserEndpoint.registerWithFacebook(token, userId, profile, new RestCallFinishListener() {
				
				@Override
				public void onFinish(Object result) {
					final User response = (User) result;
					
					LoginRequest toSave = new LoginRequest();
					
					UserIdentity identity = new UserIdentity(AuthenticationType.FACEBOOK);
					identity.setCredential(token);
					identity.setIdentityId(userId);
					List<UserIdentity> identityList = new ArrayList<UserIdentity>(1);
					identityList.add(identity);
					
					toSave.email = response.email;
					toSave.identity = identityList;
					toSave.name = response.name;
					toSave.userId = response.id;
					
					LoitiApplication.setLoginData(toSave);
					
					performLogin(new RestCallFinishListener() {
						
						@Override
						public void onFinish(Object result) {
							isFBLoginCompleted = true;
							if (isFBUploadCompleted) {
								registerProgressBar.setVisibility(View.GONE);
								startActivity(new Intent(RegistrationActivity.this, ProfileActivity_.class));
							}
						}
						
						@Override
						public void onException(Exception e) {
							e.printStackTrace();
							registerProgressBar.setVisibility(View.GONE);
						}
					});
					
					photoWorkerFragment.startDownloadUpload(profile.getPicture(), response.id);
				}
				
				@Override
				public void onException(Exception e) {
					e.printStackTrace();
					registerProgressBar.setVisibility(View.GONE);
					showSimpleDialog(R.string.popup_registration_email_already_registered);
				}
			});
		}
		
		@Override
		public void onFail(String reason) {
			registerProgressBar.setVisibility(View.GONE);
		}
	};
	
	private boolean isAllDataValid() {
		return hasName() && hasEmail() && isEmailAddressFormatValid(email.getText().toString());
	}
	
	private boolean hasName() {
		return !isEmpty(name);
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
	
	@Override
	public void onDownloadCompleted() {
	}
	
	@Override
	public void onUploadCompleted() {
		isFBUploadCompleted = true;
		if (isFBLoginCompleted) {
			registerProgressBar.setVisibility(View.GONE);
			startActivity(new Intent(RegistrationActivity.this, ProfileActivity_.class));
		}
	}
	
	@Override
	public void onDownloadFailed(Exception e) {
		e.printStackTrace();
	}
	
	@Override
	public void onUploadFailed(Exception e) {
		e.printStackTrace();
	}
	
}
