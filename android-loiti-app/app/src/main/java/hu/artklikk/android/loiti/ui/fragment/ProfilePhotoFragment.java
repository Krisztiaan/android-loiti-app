package hu.artklikk.android.loiti.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ProfilePhotoFragment extends Fragment {
	
	public static final String TAG = ProfilePhotoFragment.class.getSimpleName();
	
	private Bitmap photo;
	private boolean isNewPhoto = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	public void setDownloadedPhoto(Bitmap photo) {
		this.photo = photo;
		isNewPhoto = false;
	}
	
	public void setNewPhoto(Bitmap photo) {
		this.photo = photo;
		isNewPhoto = true;
	}
	
	public Bitmap getPhoto() {
		return photo;
	}
	
	public boolean hasPhoto() {
		return photo != null;
	}
	
	public boolean hasNewPhoto() {
		return hasPhoto() && isNewPhoto;
	}
}
