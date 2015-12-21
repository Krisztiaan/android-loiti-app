package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.IntentEndpoint;
import hu.artklikk.android.loiti.backend.dto.QrCodeFunction;
import hu.artklikk.android.loiti.backend.dto.VenueTable;
import hu.artklikk.android.loiti.backend.dto.intent.VenueTableSeatIntent;
import hu.artklikk.android.loiti.backend.rest.core.JsonMapper;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.ui.core.CameraPreview;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.SimpleDialogFragment;

import java.io.IOException;
import java.util.Date;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SuppressWarnings("deprecation")
public class QrReaderActivity extends MenuActivity {
	
	private static final String TAG_SUCCESS = "TAG_SUCCESS";
	private static final String TAG_FAIL = "TAG_FAIL";
	private static final String KEY_IS_PREVIEWING = "KEY_IS_PREVIEWING";
	
	private ImageScanner scanner;
	
	private ViewGroup previewContainer;
	
	private CameraPreview preview;
	
	private boolean isPreviewStopped = false;
	
	static {
		System.loadLibrary("iconv");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_qr_reader);
		previewContainer = (ViewGroup) findViewById(R.id.activity_qr_reader_preview_container);
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);
		if(savedInstanceState != null) {
			if(savedInstanceState.containsKey(KEY_IS_PREVIEWING)) {
				isPreviewStopped = savedInstanceState.getBoolean(KEY_IS_PREVIEWING);
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		preview = new CameraPreview(this, previewCallback, !isPreviewStopped);
		previewContainer.addView(preview);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		previewContainer.removeAllViews();
		preview = null;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean(KEY_IS_PREVIEWING, isPreviewStopped);
	}

	@Override
	protected void onStop() {
		
		super.onStop();
	}
	
	PreviewCallback previewCallback = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();
			
			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);
			
			int result = scanner.scanImage(barcode);
			
			if (result != 0) {
				preview.stopPreview();
				isPreviewStopped = true;
				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					QrCodeFunction qrData = null;
					try {
						qrData = JsonMapper.getMapper().readValue(sym.getData(), QrCodeFunction.class);
					}
					catch (JsonParseException e) {
						e.printStackTrace();
						qrFailed();
					}
					catch (JsonMappingException e) {
						e.printStackTrace();
						qrFailed();
					}
					catch (IOException e) {
						e.printStackTrace();
						qrFailed();
					}
					if (qrData != null) {
						
						if (qrData.getType() != null && qrData.getCode() != null && !qrData.getCode().isEmpty()) {
							
							VenueTableSeatIntent intent = new VenueTableSeatIntent();
							intent.tableCode = qrData.getCode();
							intent.time = new Date();
							intent.userId = LoitiApplication.getUserId();
							intent.venueId = LoitiApplication.getVenueId();
							
							IntentEndpoint.takeSeat(intent, new RestCallFinishListener() {
								
								@Override
								public void onFinish(Object result) {
									VenueTable response = (VenueTable) result;
									LoitiApplication.saveLastTableId(response.id);
									qrSucceeded(getResources().getString(R.string.popup_qr_reader_welcome,
											response.designation));
								}
								
								@Override
								public void onException(Exception e) {
									e.printStackTrace();
									qrFailed();
								}
							});
						}
						else {
							qrFailed();
						}
					}
					
				}
				
			}
		}
	};
	
	private void qrSucceeded(String message) {
		Fragment success = getSupportFragmentManager().findFragmentByTag(TAG_SUCCESS);
		if (success == null) {
			showDialog(SimpleDialogFragment.create(message), TAG_SUCCESS);
		}
	}
	
	private void qrFailed() {
		Fragment fail = getSupportFragmentManager().findFragmentByTag(TAG_FAIL);
		if (fail == null) {
			showDialog(SimpleDialogFragment.create(R.string.popup_qr_reader_failed), TAG_FAIL);
		}
	}
	
	@Override
	public void onAgree(SimpleDialogFragment caller) {
		super.onAgree(caller);
		if (TAG_SUCCESS.equals(caller.getTag())) {
			startActivity(MainActivity.createGoBackIntent(QrReaderActivity.this));
		}
		else if (TAG_FAIL.equals(caller.getTag())) {
			if (preview != null) {
				preview.startPreview();
				isPreviewStopped = false;
			}
		}
	}
	
	@Override
	public void onCancel(SimpleDialogFragment caller) {
		super.onCancel(caller);
		if (TAG_SUCCESS.equals(caller.getTag())) {
			startActivity(MainActivity.createGoBackIntent(QrReaderActivity.this));
		}
		else if (TAG_FAIL.equals(caller.getTag())) {
			if (preview != null) {
				preview.startPreview();
				isPreviewStopped = false;
			}
		}
	}
	
}
