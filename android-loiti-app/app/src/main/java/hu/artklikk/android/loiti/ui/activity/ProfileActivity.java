package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.UserEndpoint;
import hu.artklikk.android.loiti.backend.dto.LoginRequest;
import hu.artklikk.android.loiti.backend.dto.User;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.ProfilePhotoFragment;
import hu.artklikk.android.loiti.ui.fragment.SimpleDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.ImageViewBitmapInfo;
import com.koushikdutta.ion.Ion;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends MenuActivity {
	
	private static final String TAG_SAVE_SUCCESSFUL = "TAG_SAVE_SUCCESSFUL";
	private static final String TAG_SAVE_FAILED = "TAG_SAVE_FAILED";
	
	private static final int CHOOSE_IMAGE = 71;
	private static final String IMAGE_MIME_TYPE = "image/*";
	private static final String FILE_TEMP_NAME = "loitiTemp.png";
	
	private Uri imageUri;
	
	private static final String KEY_IS_LOADED = "KEY_IS_LOADED";
	private boolean isLoaded;
	
	private static final String KEY_IS_UPLOAD_ENDED = "KEY_IS_UPLOAD_ENDED";
	private static final String KEY_IS_UPDATE_ENDED = "KEY_IS_UPDATE_ENDED";
	private boolean isUploadEnded = false;
	private boolean isUpdateEnded = false;
	
	@ViewById(R.id.activity_profile_image)
	ImageView thumbnail;
	@ViewById(R.id.activity_profile_name)
	EditText name;
	@ViewById(R.id.activity_profile_email)
	EditText email;
	@ViewById(R.id.activity_profile_phone)
	EditText phone;
	@ViewById(R.id.activity_profile_save_progress)
	View saveProgress;
	
	private ProfilePhotoFragment photoStoreFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			isLoaded = savedInstanceState.getBoolean(KEY_IS_LOADED);
			isUpdateEnded = savedInstanceState.getBoolean(KEY_IS_UPDATE_ENDED);
			isUploadEnded = savedInstanceState.getBoolean(KEY_IS_UPLOAD_ENDED);
		}
		ProfilePhotoFragment fragment = (ProfilePhotoFragment) getSupportFragmentManager().findFragmentByTag(
				ProfilePhotoFragment.TAG);
		if (fragment == null) {
			fragment = new ProfilePhotoFragment();
			getSupportFragmentManager().beginTransaction().add(fragment, ProfilePhotoFragment.TAG).commit();
		}
		photoStoreFragment = fragment;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_IS_LOADED, isLoaded);
		outState.putBoolean(KEY_IS_UPDATE_ENDED, isUpdateEnded);
		outState.putBoolean(KEY_IS_UPLOAD_ENDED, isUploadEnded);
	}
	
	@Click(R.id.activity_profile_new_photo)
	void newPhotoClicked() {
		final List<Intent> cameraIntentList = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		imageUri = Uri.fromFile(createImageTempFile());
		captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> cameraResolveInfoList = packageManager.queryIntentActivities(captureIntent, 0);
		for (ResolveInfo resolveInfo : cameraResolveInfoList) {
			final String packageName = resolveInfo.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
			intent.setPackage(packageName);
			cameraIntentList.add(intent);
		}
		
		final Intent galleryIntent = new Intent(Intent.ACTION_PICK).setType(IMAGE_MIME_TYPE);
		
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				getResources().getString(R.string.registration_picture_dialog_header));
		
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntentList.toArray(new Parcelable[] {}));
		
		startActivityForResult(chooserIntent, CHOOSE_IMAGE);
		
	}
	
	private File createImageTempFile() {
		
		File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		if (!picDir.exists()) {
			if (!picDir.mkdirs()) {
				throw new NullPointerException("Cannot create temp file directory!");
			}
		}
		
		File temp = new File(picDir.getAbsolutePath() + File.separatorChar + FILE_TEMP_NAME);
		if (temp.exists()) {
			if (!temp.delete()) {
				throw new NullPointerException("Temp file cannot be deleted!");
			}
		}
		
		try {
			if (!temp.createNewFile()) {
				throw new NullPointerException("Temp file cannot be created!");
				
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return temp;
	}
	
	private boolean deleteImageTempFile() {
		
		File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		if (!picDir.exists()) {
			if (!picDir.mkdirs()) {
				return false;
			}
		}
		
		File temp = new File(picDir.getAbsolutePath() + File.separatorChar + FILE_TEMP_NAME);
		if (temp.exists()) {
			return temp.delete();
		}
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CHOOSE_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				
				if (data != null && !android.provider.MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction())) {
					imageUri = data.getData();
				}
				
				Bitmap result = null;
				try {
					result = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
				}
				catch (FileNotFoundException e) {
					e.printStackTrace();
					return;
				}
				catch (IOException e) {
					e.printStackTrace();
					return;
				}
				
				int bitmapSize = result.getHeight() > result.getWidth() ? result.getWidth() : result.getHeight();
				photoStoreFragment.setNewPhoto(ThumbnailUtils.extractThumbnail(result, bitmapSize, bitmapSize,
						ThumbnailUtils.OPTIONS_RECYCLE_INPUT));
				deleteImageTempFile();
				thumbnail.setImageBitmap(photoStoreFragment.getPhoto());
			}
		}
	}
	
	@Click(R.id.activity_profile_save)
	void saveClicked() {
		if (LoitiApplication.getUserId() == null) {
			return;
		}
		
		if(!photoStoreFragment.hasPhoto()) {
			showSimpleDialog(R.string.popup_profile_photo_required);
			return;
		}
		if(name.getText().toString().trim().isEmpty()) {
			showSimpleDialog(R.string.popup_registration_name_required);
			return;
		}
		if(email.getText().toString().trim().isEmpty()) {
			showSimpleDialog(R.string.popup_registration_email_required);
			return;
		}
		saveProgress.setVisibility(View.VISIBLE);
		if (photoStoreFragment.hasNewPhoto()) {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			new AsyncTask<Bitmap, Void, Void>() {
				
				@Override
				protected Void doInBackground(Bitmap... params) {
					params[0].compress(Bitmap.CompressFormat.PNG, 100, baos);
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					UserEndpoint.uploadPhoto(LoitiApplication.getUserId(), baos.toByteArray(),
							new RestCallFinishListener() {
								
								@Override
								public void onFinish(Object result) {
									isUploadEnded = true;
									System.out.println("upload OK");
									if (isUpdateEnded) {
										saveProgress.setVisibility(View.GONE);
										showSaveSuccessful();
									}
								}
								
								@Override
								public void onException(Exception e) {
									System.out.println("upload NOT OK");
									e.printStackTrace();
									isUploadEnded = true;
									if (isUpdateEnded) {
										saveProgress.setVisibility(View.GONE);
										showSaveFailed();
									}
								}
							});
				}
			}.execute(photoStoreFragment.getPhoto());
			
		}
		
		User user = new User();
		user.id = LoitiApplication.getUserId();
		user.name = name.getText().toString();
		user.email = email.getText().toString();
		user.phone = phone.getText().toString();
		
		UserEndpoint.updateUser(user, new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				System.out.println("update OK");
				isUpdateEnded = true;
				if (!photoStoreFragment.hasNewPhoto() || photoStoreFragment.hasNewPhoto() && isUploadEnded) {
					saveProgress.setVisibility(View.GONE);
					showSaveSuccessful();
				}
			}
			
			@Override
			public void onException(Exception e) {
				System.out.println("update NOT OK");
				isUpdateEnded = true;
				e.printStackTrace();
				if (!photoStoreFragment.hasNewPhoto() || photoStoreFragment.hasNewPhoto() && isUploadEnded) {
					saveProgress.setVisibility(View.GONE);
					showSaveFailed();
				}
			}
		});
	}
	
	@Click(R.id.activity_profile_change_password)
	void changePasswordClicked() {
		LoginRequest request = null;
		if ((request = LoitiApplication.getLoginData()) == null) {
			return;
		}
		UserEndpoint.resetPassword(request, new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				System.out.println("reset successful");
				
			}
			
			@Override
			public void onException(Exception e) {
				System.out.println("reset failed");
				
			}
		});
	}
	
	@AfterViews
	void loadUserData() {
		if (!isLoaded) {
			if (LoitiApplication.getUserId() != null) {
				UserEndpoint.getUser(LoitiApplication.getUserId(), userDataCallback);
			}
		}
		if (photoStoreFragment.hasPhoto()) {
			thumbnail.setImageBitmap(photoStoreFragment.getPhoto());
		}
	}
	
	private RestCallFinishListener userDataCallback = new RestCallFinishListener() {
		@Override
		public void onFinish(Object result) {
			isLoaded = true;
			User response = (User) result;
			
			name.setText(response.name);
			email.setText(response.email);
			phone.setText(response.phone);
			
			if (response.imgProfile != null) {
				Ion.with(thumbnail).load(response.imgProfile).withBitmapInfo()
						.setCallback(new FutureCallback<ImageViewBitmapInfo>() {
							
							@Override
							public void onCompleted(Exception e, ImageViewBitmapInfo imageViewBitmapInfo) {
								if (e == null) {
									if (imageViewBitmapInfo.getException() == null) {
										if (imageViewBitmapInfo.getBitmapInfo() != null) {
											if (imageViewBitmapInfo.getBitmapInfo().bitmap != null) {
												photoStoreFragment.setDownloadedPhoto(imageViewBitmapInfo
														.getBitmapInfo().bitmap);
											}
											else {
												throw new NullPointerException("Bitmap is null!");
											}
										}
										else {
											throw new NullPointerException("BitmapInfo is null!");
										}
									}
									else {
										imageViewBitmapInfo.getException().printStackTrace();
									}
								}
								else {
									e.printStackTrace();
								}
							}
						});
			}
		}
		
		@Override
		public void onException(Exception e) {
			e.printStackTrace();
		}
	};

	@Override
	public void onAgree(SimpleDialogFragment caller) {
		super.onAgree(caller);
		if (TAG_SAVE_SUCCESSFUL.equals(caller.getTag()) || TAG_SAVE_FAILED.equals(caller.getTag())) {
			startActivity(MainActivity.createGoBackIntent(this));
		}
	}

	@Override
	public void onCancel(SimpleDialogFragment caller) {
		// not calling super - prevent closing
	}
	
	private void showSaveSuccessful() {
		showDialog(SimpleDialogFragment.create(R.string.popup_profile_save_successful), TAG_SAVE_SUCCESSFUL);
	}
	
	private void showSaveFailed() {
		showDialog(SimpleDialogFragment.create(R.string.popup_profile_save_failed), TAG_SAVE_FAILED);
	}
}
