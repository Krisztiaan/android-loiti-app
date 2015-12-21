package hu.artklikk.android.loiti.ui.core;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.backend.IntentEndpoint;
import hu.artklikk.android.loiti.backend.VenueEndpoint;
import hu.artklikk.android.loiti.backend.dto.Venue;
import hu.artklikk.android.loiti.backend.dto.intent.VenueArrivalIntent;
import hu.artklikk.android.loiti.backend.dto.intent.VenueLeaveIntent;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.location.FenceHandler;
import hu.artklikk.android.loiti.location.VenueStore;

import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

public abstract class FenceAwareActivity extends FragmentSwitcherActivity {
	
	public enum FenceInfo {
		FAR,
		IN;
	}
	
	private static final String STATE_LAST_FENCE = "LAST_FENCE";
	
	private VenueStore venueStore;
	private FenceHandler fenceHandler;
	private Long lastVenueId;
	
	private static FenceInfo lastFence = FenceInfo.FAR;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		venueStore = new VenueStore(this);
		fenceHandler = new FenceHandler(this);
		fenceHandler.setConnectionCallback(connectionCallback);
		if (savedInstanceState != null) {
			FenceInfo savedState = (FenceInfo) savedInstanceState.getSerializable(STATE_LAST_FENCE);
			if (savedState != null) {
				lastFence = savedState;
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(fenceTriggerReceiver, new IntentFilter(FenceHandler.ACTION_FENCE_BROADCAST));
		fenceHandler.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(fenceTriggerReceiver);
		fenceHandler.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		fenceHandler.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putSerializable(STATE_LAST_FENCE, lastFence);
	}
	
	public FenceInfo getLastFenceInfo() {
		return lastFence;
	}
	
	public boolean isInside() {
		return lastFence == FenceInfo.IN;
	}
	
	private ConnectionCallbacks connectionCallback = new ConnectionCallbacks() {
		
		@Override
		public void onConnected(Bundle data) {
			getVenues();
		}
		
		@Override
		public void onDisconnected() {
		}
		
	};
	
	private void getVenues() {
		// TODO TEST
		venueStore.clear();
		
		// if the venue store empty download it
		if (venueStore.getAllId().length == 0) {
			VenueEndpoint.getVenues(new RestCallFinishListener() {
				
				@Override
				public void onFinish(Object result) {
					@SuppressWarnings("unchecked")
					List<Venue> list = (List<Venue>) result;
					for (Venue v : list)
						venueStore.add(v);
					
					fenceHandler.addLocationsToFence(list);
				}
				
				@Override
				public void onException(Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	private BroadcastReceiver fenceTriggerReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get the type of transition (entry or exit)
			int transitionType = LocationClient.getGeofenceTransition(intent);
			
			// get the triggered fences
			List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
			
			Long venueId = null;
			
			if (triggerList.size() == 0) {
				return;
			}
			
			for (Geofence geofence : triggerList) {
				venueId = Long.parseLong(geofence.getRequestId());
			}
			
			if (venueId == null || !venueId.equals(LoitiApplication.getVenueId())) {
				return;
			}
			else {
				lastVenueId = venueId;
			}
			
			if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER && lastFence == FenceInfo.FAR) {
				changeScreenTo(isLastLoginSuccessful() ? Screen.INSIDE_LOGGEDIN : Screen.INSIDE_LOGGEDOUT);
				lastFence = FenceInfo.IN;
				VenueArrivalIntent arrivalIntent = new VenueArrivalIntent();
				arrivalIntent.time = new Date();
				arrivalIntent.userId = LoitiApplication.getUserId();
				arrivalIntent.venueId = venueId;
				IntentEndpoint.arrival(arrivalIntent, null);
			}
			
			else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT && lastFence == FenceInfo.IN) {
				changeScreenTo(isLastLoginSuccessful() ? Screen.FAR_LOGGEDIN : Screen.FAR_LOGGEDOUT);
				lastFence = FenceInfo.FAR;
				LoitiApplication.saveLastTableId(null);
				VenueLeaveIntent leaveIntent = new VenueLeaveIntent();
				leaveIntent.time = new Date();
				leaveIntent.userId = LoitiApplication.getUserId();
				leaveIntent.venueId = venueId;
				IntentEndpoint.leave(leaveIntent, null);
			}
		}
		
	};
	
	protected Long getLastVenueId() {
		return lastVenueId;
	}
	
}
