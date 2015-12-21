package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.backend.UserEndpoint;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RegistrationPhotoUploaderFragment extends Fragment {
	
	public static final String TAG = "DOWN_UP_FRAGMENT";
	
	public interface PhotoCallback {
		public void onDownloadCompleted();
		
		public void onUploadCompleted();
		
		public void onDownloadFailed(Exception e);
		
		public void onUploadFailed(Exception e);
	}
	
	private PhotoCallback callback;
	private DownloadTask downloadTask;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (PhotoCallback) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		callback = null;
	}
	
	public void startDownloadUpload(String url, Long userId) {
		
		if (downloadTask != null) {
			if (downloadTask.getStatus() == Status.FINISHED) {
				downloadTask = new DownloadTask(this, url, userId);
				downloadTask.execute();
			}
			else {
				return;
			}
		}
		else {
			downloadTask = new DownloadTask(this, url, userId);
			downloadTask.execute();
		}
	}
	
	private void onDownloadComplete(Long userId, byte[] photoData) {
		UserEndpoint.uploadPhoto(userId, photoData, new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				onUploadComplete();
			}
			
			@Override
			public void onException(Exception e) {
				onUploadFailed(e);
			}
		});
	}
	
	private void onDownloadFailed(Exception e) {
		if (callback != null) {
			callback.onDownloadFailed(e);
		}
	}
	
	private void onUploadComplete() {
		if (callback != null) {
			callback.onUploadCompleted();
		}
	}
	
	private void onUploadFailed(Exception e) {
		if (callback != null) {
			callback.onUploadFailed(e);
		}
	}
	
	private static class DownloadTaskResult {
		
		DownloadTaskResult(Exception e) {
			this.e = e;
		}
		
		DownloadTaskResult(byte[] photoData) {
			this.photoData = photoData;
		}
		
		byte[] photoData;
		Exception e;
	}
	
	private class DownloadTask extends AsyncTask<Void, Void, DownloadTaskResult> {
		
		private RegistrationPhotoUploaderFragment fragment;
		private String url;
		private Long userId;
		
		DownloadTask(RegistrationPhotoUploaderFragment fragment, String url, Long userId) {
			this.fragment = fragment;
			this.url = url;
			this.userId = userId;
		}
		
		@Override
		protected DownloadTaskResult doInBackground(Void... params) {
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			try {
				byte[] chunk = new byte[4096];
				int bytesRead;
				InputStream stream = new URL(url).openStream();
				
				while ((bytesRead = stream.read(chunk)) > 0) {
					outputStream.write(chunk, 0, bytesRead);
				}
				stream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				return new DownloadTaskResult(e);
			}
			return new DownloadTaskResult(outputStream.toByteArray());
			
		}
		
		@Override
		protected void onPostExecute(DownloadTaskResult result) {
			if (result.e == null) {
				fragment.onDownloadComplete(userId, result.photoData);
			}
			else {
				fragment.onDownloadFailed(result.e);
			}
		}
	}
	
}
