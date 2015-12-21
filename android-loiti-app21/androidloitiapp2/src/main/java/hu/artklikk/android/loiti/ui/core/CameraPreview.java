package hu.artklikk.android.loiti.ui.core;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	
	private SurfaceHolder holder;
	private Camera camera;
	private PreviewCallback previewCallback;
	private Handler autoFocusHandler;
	private int lastReportedWidth;
	private int lastReportedHeight;
	private int cameraId = -1;
	private int cameraOrientation;
	private boolean isPreviewing = false;
	private boolean autoStartPreview = true;
	
	public CameraPreview(Context context, PreviewCallback previewCallback, boolean autoStartPreview) {
		super(context);
		autoFocusHandler = new Handler();
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.previewCallback = previewCallback;
		this.autoStartPreview = autoStartPreview;
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		preparePreview();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopPreview();
		holder.removeCallback(this);
		releaseCamera();
	}
	
	private void preparePreview() {
		try {
			camera = openCamera();
			camera.setPreviewDisplay(holder);
		}
		catch (Exception exception) {
			releaseCamera();
		}
	}
	
	private Camera openCamera() {
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraOrientation = cameraInfo.orientation;
				return Camera.open(cameraId);
			}
		}
		return null;
	}
	
	public void stopPreview() {
		if (camera != null) {
			isPreviewing = false;
			camera.stopPreview();
		}
	}
	
	private void releaseCamera() {
		if (camera != null) {
			isPreviewing = false;
			camera.setPreviewCallback(null);
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		this.lastReportedWidth = w;
		this.lastReportedHeight = h;
		if (camera != null) {
			if (autoStartPreview) {
				startPreview();
			}
		}
	}
	
	public void startPreview() {
		Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		startPreview(camera, lastReportedWidth, lastReportedHeight, display);
	}
	
	private void startPreview(Camera camera, int previewWidth, int previewHeight, Display display) {
		int displayRotation = display.getRotation();
		Camera.Parameters parameters = camera.getParameters();
		List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
		Size optimalPreviewSize = getOptimalPreviewSize(supportedPreviewSizes, previewWidth, previewHeight,
				displayRotation, cameraOrientation);
		if (optimalPreviewSize != null) {
			int width = optimalPreviewSize.width;
			int height = optimalPreviewSize.height;
			int degrees = 0;
			switch (displayRotation) {
				case Surface.ROTATION_0:
					degrees = 0;
					break;
				case Surface.ROTATION_90:
					degrees = 90;
					break;
				case Surface.ROTATION_180:
					degrees = 180;
					break;
				case Surface.ROTATION_270:
					degrees = 270;
					break;
			}
			int result = (cameraOrientation - degrees + 360) % 360;
			camera.setDisplayOrientation(result);
			parameters.setPreviewSize(width, height);
			camera.setParameters(parameters);
			camera.setPreviewCallback(previewCallback);
			camera.startPreview();
			camera.autoFocus(autoFocusCallback);
			isPreviewing = true;
		}
	}
	
	private static Size getOptimalPreviewSize(List<Size> sizes, int w, int h, int deviceRotation, int cameraOrientation) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = getRatio(w, h, deviceRotation, cameraOrientation);
		if (sizes == null)
			return null;
		
		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		
		int targetHeight = h;
		
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
				continue;
			}
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}
	
	private static double getRatio(final int width, final int height, final int deviceRotation,
			final int cameraOrientation) {
		return isNeedToSwapDimensions(deviceRotation, cameraOrientation) ? ((double) height / (double) width)
				: ((double) width / (double) height);
	}
	
	private static boolean isNeedToSwapDimensions(int deviceRotation, int cameraOrientation) {
		return isDeviceRotationDimensionsNatural(deviceRotation) != isCameraInDeviceNaturalRotation(cameraOrientation);
	}
	
	private static boolean isDeviceRotationDimensionsNatural(int deviceRotation) {
		return deviceRotation == Surface.ROTATION_0 || deviceRotation == Surface.ROTATION_180;
	}
	
	private static boolean isCameraInDeviceNaturalRotation(int cameraOrientation) {
		return cameraOrientation == 0 || cameraOrientation == 180;
	}
	
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};
	
	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (isPreviewing)
				camera.autoFocus(autoFocusCallback);
		}
	};
	
}
