package hu.artklikk.android.loiti.location;

import hu.artklikk.android.loiti.backend.dto.Venue;
import hu.artklikk.android.loiti.utils.SharedStore;
import android.content.Context;

public class VenueStore extends SharedStore<Venue>{

	private static final String PREFERENCE_NAME = "VENUE_STORE";
	
	public VenueStore(Context ctx) {
		super(ctx);
	}

	@Override
	protected String getPreferencesName() {
		return PREFERENCE_NAME;
	}

	@Override
	protected Class<Venue> getDataClass() {
		return Venue.class;
	}
	
	
	public void add(Venue venue) {
		if(venue == null)
			return;
		if(venue.id == null)
			throw new IllegalArgumentException("No ID!");
		
		super.add(String.valueOf(venue.id), venue);
	}
}
