package hu.artklikk.android.loiti.ui.core;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

@SuppressWarnings("deprecation")
public class CameraWrapper {
	
	private Camera camera;
	private CameraInfo info;
	
	public CameraWrapper(final Camera camera, final CameraInfo info) {
		this.camera = camera;
		this.info = info;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public CameraInfo getInfo() {
		return info;
	}
	
}
