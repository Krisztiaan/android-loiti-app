package hu.artklikk.android.loiti.location;

import hu.artklikk.android.loiti.backend.dto.Venue;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;

/**
 * GeoFence initialization and using helper with lifecycle methods. Create
 * instance in onCreate() and don't forget to call onPause() and onResume() to
 * handle connection. To handle connection error override the
 * onActivityResult(...) method in your activity or fragment, and call this
 * handler's onActivityResult(...) method. If you need information about
 * location client's connection and disconnection set a connection listener.
 * 
 * @author garik
 */
public class FenceHandler {
	
	public static final int FENCE_REQUEST_CODE = 9532498;
	public static final String ACTION_FENCE_BROADCAST = "hu.artklikk.android.loiti.location.FENCE_ACTIVATED";
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 2193861;
	
	private LocationClient locationClient;
	private PendingIntent fenceIntent;
	private Activity activity;
	private ConnectionCallbacks outerConnectionCallback;
	
	public FenceHandler(Activity activity) {
		this.activity = activity;
	}
	
	public void onResume() {
		connect();
	}
	
	public void onPause() {
		if (locationClient != null) {
			removeFences();
			locationClient.disconnect();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (CONNECTION_FAILURE_RESOLUTION_REQUEST == requestCode && Activity.RESULT_OK == resultCode) {
			connect();
		}
	}
	
	private void connect() {
		if (locationClient == null)
			locationClient = new LocationClient(activity, connectionCallback, connectionFailed);
		
		if (!locationClient.isConnected() || !locationClient.isConnecting())
			locationClient.connect();
	}
	
	private ConnectionCallbacks connectionCallback = new ConnectionCallbacks() {
		
		@Override
		public void onConnected(Bundle dataBundle) {
			// locationClient.setMockMode(true);
			if (outerConnectionCallback != null)
				outerConnectionCallback.onConnected(dataBundle);
		}
		
		@Override
		public void onDisconnected() {
			locationClient = null;
			if (outerConnectionCallback != null)
				outerConnectionCallback.onDisconnected();
		}
		
	};
	
	private OnConnectionFailedListener connectionFailed = new OnConnectionFailedListener() {
		
		@Override
		public void onConnectionFailed(ConnectionResult result) {
			/*
			 * If the error has a resolution, start a Google Play services
			 * activity to resolve it.
			 */
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
				}
				catch (SendIntentException e) {
					// Log the error
					e.printStackTrace();
				}
				// If no resolution is available, display an error dialog
			}
			else {
				// Get the error code
				int errorCode = result.getErrorCode();
				// Get the error dialog from Google Play services
				Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activity,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				// If Google Play services can provide an error dialog
				if (errorDialog != null) {
					errorDialog.show();
				}
			}
			
		}
	};
	
	/**
	 * Set geofences by the given venues' locations.
	 * 
	 * @param venues
	 */
	public void addLocationsToFence(List<Venue> venues) {
		if (locationClient == null || !locationClient.isConnected()) {
			return;
		}
		if (venues == null || venues.size() == 0) {
			return;
		}
		
		List<Geofence> fences = new ArrayList<Geofence>(venues.size());
		for (Venue venue : venues) {
			fences.add(createGeofenceFromVenue(venue));
		}
		
		Intent bcIntent = new Intent(ACTION_FENCE_BROADCAST);
		fenceIntent = PendingIntent.getBroadcast(activity, FENCE_REQUEST_CODE, bcIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		locationClient.addGeofences(fences, fenceIntent, addGeofencesListener);
	}
	
	/**
	 * Generate fence objects from venue. It creates two fence one for beeing
	 * far and one for beeing in the venue. The in-fence has 50m radius! The
	 * far-fence's radius is depend on the venues's radius parameter. Fence ids
	 * build up like: <FAR|IN>_<random long>_<venueID> The prefix depend on is
	 * this a far-fence or in-fence. The random is because the GeoFence system
	 * can't contain same id-s so it's reduce the chance for duplication. The
	 * end of the id is the original ID of the venue.
	 * 
	 * @param venue
	 *            Venue.
	 * @return Two geofence the inner and the far fence around the veune.
	 */
	private Geofence createGeofenceFromVenue(Venue venue) {
		return new Geofence.Builder()
				.setCircularRegion(venue.location.latitude, venue.location.longitude, venue.location.radius)
				.setRequestId(String.valueOf(venue.id))
				.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
				.setExpirationDuration(Geofence.NEVER_EXPIRE).setNotificationResponsiveness(15000).build();
		
	}
	
	/**
	 * Remove all geofences added before.
	 */
	public void removeFences() {
		if (locationClient.isConnected() && fenceIntent != null)
			locationClient.removeGeofences(fenceIntent, removeFenceListener);
	}
	
	private OnRemoveGeofencesResultListener removeFenceListener = new OnRemoveGeofencesResultListener() {
		@Override
		public void onRemoveGeofencesByRequestIdsResult(int arg0, String[] arg1) {
		}
		
		@Override
		public void onRemoveGeofencesByPendingIntentResult(int arg0, PendingIntent arg1) {
		}
	};
	
	private OnAddGeofencesResultListener addGeofencesListener = new OnAddGeofencesResultListener() {
		
		@Override
		public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
			// success
			if (LocationStatusCodes.SUCCESS == statusCode) {
				
			}
			else { // failed
					// TODO ilyenkor mi van?
			}
		}
	};
	
	public void setConnectionCallback(ConnectionCallbacks connectionCallback) {
		this.outerConnectionCallback = connectionCallback;
	}
}
