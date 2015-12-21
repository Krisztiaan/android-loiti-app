package hu.artklikk.android.loiti.ui.activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.VenueEndpoint;
import hu.artklikk.android.loiti.backend.dto.Venue;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ContactActivity extends MenuActivity {
	
	private TextView description;
	private TextView name;
	private TextView addressFirst;
	private TextView addressSecond;
	private TextView email;
	private TextView phone;
	
	private MapFragment mapFragment;
	private static final String TAG_MAP = "TAG_MAP";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		description = (TextView) findViewById(R.id.activity_contact_venue_description);
		name = (TextView) findViewById(R.id.activity_contact_venue_name);
		addressFirst = (TextView) findViewById(R.id.activity_contact_venue_address_first_line);
		addressSecond = (TextView) findViewById(R.id.activity_contact_venue_address_second_line);
		email = (TextView) findViewById(R.id.activity_contact_venue_email);
		phone = (TextView) findViewById(R.id.activity_contact_venue_phone);
		
		if ((mapFragment = (MapFragment) getFragmentManager().findFragmentByTag(TAG_MAP)) == null) {
			mapFragment = new MapFragment();
			getFragmentManager().beginTransaction().add(R.id.activity_contact_map_container, mapFragment, TAG_MAP)
					.commit();
		}
		
		VenueEndpoint.getVenue(LoitiApplication.getVenueId(), new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				Venue response = (Venue) result;
				description.setText(response.description);
				name.setText(response.name);
				String firstAddressLine = new StringBuilder(response.address.getCity()).append(", ")
						.append(response.address.getStreet()).append(" ").append(response.address.getStreetNumber())
						.toString();
				addressFirst.setText(firstAddressLine);
				String secondAddressLine = new StringBuilder(response.address.getPostalCode()).append(" ")
						.append(response.address.getCountry()).toString();
				addressSecond.setText(secondAddressLine);
				String emailLine = response.contact.getEmail().isEmpty() ? "" : response.contact.getEmail().get(0);
				email.setText(emailLine);
				String phoneLine = response.contact.getPhone().isEmpty() ? "" : response.contact.getPhone().get(0);
				phone.setText(phoneLine);
				
				GoogleMap map = mapFragment.getMap();
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(response.location.latitude,
						response.location.longitude), 15f));
				map.addMarker(new MarkerOptions().position(new LatLng(response.location.latitude,
						response.location.longitude)));
				
			}
			
			@Override
			public void onException(Exception e) {
				
			}
		});
		
	}

	@Override
	public void onResume() {
		super.onResume();
		GoogleMap map = mapFragment.getMap();
		if(map != null) {
			map.getUiSettings().setAllGesturesEnabled(false);
			map.getUiSettings().setZoomControlsEnabled(false);
		}
	}
}
