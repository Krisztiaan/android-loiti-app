package hu.artklikk.android.loiti.location;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import hu.artklikk.android.loiti.backend.dto.Venue;

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

    private GoogleApiClient googleApiClient;
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
		if (googleApiClient != null) {
			removeFences();
            googleApiClient.disconnect();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (CONNECTION_FAILURE_RESOLUTION_REQUEST == requestCode && Activity.RESULT_OK == resultCode) {
			connect();
		}
	}
	
	private void connect() {
		if (googleApiClient == null)
            googleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(connectionCallback)
                    .addOnConnectionFailedListener(connectionFailed)
                    .build();
		
		if (!googleApiClient.isConnected() || !googleApiClient.isConnecting())
            googleApiClient.connect();
	}
	
	private ConnectionCallbacks connectionCallback = new ConnectionCallbacks() {
		
		@Override
		public void onConnected(Bundle dataBundle) {
			// locationClient.setMockMode(true);
			if (outerConnectionCallback != null)
				outerConnectionCallback.onConnected(dataBundle);
		}
		
		@Override
		public void onConnectionSuspended(int var1) {
			googleApiClient = null;
			if (outerConnectionCallback != null)
				outerConnectionCallback.onConnectionSuspended(var1);
		}
		
	};
	
	private GoogleApiClient.OnConnectionFailedListener connectionFailed = new OnConnectionFailedListener() {
		
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
		if (googleApiClient == null || !googleApiClient.isConnected()) {
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

        LocationServices.GeofencingApi.addGeofences(googleApiClient, fences, fenceIntent);
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
		if (googleApiClient.isConnected() && fenceIntent != null)
            LocationServices.GeofencingApi.removeGeofences(googleApiClient, fenceIntent);
	}
	
	public void setConnectionCallback(ConnectionCallbacks connectionCallback) {
		this.outerConnectionCallback = connectionCallback;
	}
}
