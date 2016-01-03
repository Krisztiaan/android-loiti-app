package hu.artklikk.android.loiti.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;
import com.sromku.simple.fb.utils.PictureAttributes.PictureType;

import java.util.List;

import hu.artklikk.android.loiti.R;

public class FBHelper {
	
	private static Permission[] permissions = new Permission[] { Permission.PUBLIC_PROFILE, Permission.EMAIL };
	
	private static boolean configured = false;
	
	private static void setConfig(Context ctx) {
		SimpleFacebookConfiguration config = new SimpleFacebookConfiguration.Builder()
				.setAppId(ctx.getString(R.string.fb_app_id)).setNamespace(ctx.getPackageName())
				.setPermissions(permissions).build();
		
		SimpleFacebook.setConfiguration(config);
	}
	
	public static SimpleFacebook getFacebook(Activity activity) {
		if (!configured)
			setConfig(activity);
		
		return SimpleFacebook.getInstance(activity);
	}
	
	/**
	 * 
	 * @param activity
	 * @param callback
	 *            HAVE TO GIVE! Except it won't do anything!
	 */
	public static void login(Activity activity, final OnLoginCallback callback) {
		if (callback == null)
			return;
		
		final SimpleFacebook facebook = FBHelper.getFacebook(activity);
		facebook.login(new OnLoginListener() {

			@Override
			public void onLogin(final String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
				{
					PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
					pictureAttributes.setType(PictureType.LARGE);
					pictureAttributes.setHeight(512);
					pictureAttributes.setWidth(512);

					Properties prp = new Properties.Builder() //
							.add(Properties.EMAIL) //
							.add(Properties.FIRST_NAME) //
							.add(Properties.LAST_NAME) //
							.add(Properties.NAME) //
							.add(Properties.PICTURE, pictureAttributes) //
							.build();

					facebook.getProfile(prp, new OnProfileListener() {
						@Override
						public void onComplete(Profile profile) {
							callback.onLogin(accessToken, profile.getId(), profile);
						}

						@Override
						public void onException(Throwable throwable) {
							callback.onFail(throwable.getMessage());
						}

						@Override
						public void onFail(String reason) {
							callback.onFail(reason);
						}
					});

				}
			}

			@Override
			public void onCancel() {

			}

			@Override
			public void onFail(String reason) {
				callback.onFail(reason);
			}
			
			@Override
			public void onException(Throwable throwable) {
				callback.onFail(throwable.getMessage());
			}
		});
	}
	
	public static void logout(Activity activity, OnLogoutListener listener) {
		if (listener == null) {
			listener = new OnLogoutListener() {
				@Override
				public void onLogout() {
				}
			};
		}
		
		FBHelper.getFacebook(activity).logout(listener);
	}
	
	public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		FBHelper.getFacebook(activity).onActivityResult(requestCode, resultCode, data);
	}
	
	public static boolean isLogin(Activity activity) {
		return FBHelper.getFacebook(activity).isLogin();
	}
	
	public interface OnLoginCallback {
		public void onLogin(String token, String userId, Profile profile);
		
		public void onFail(String reason);
	}
}
