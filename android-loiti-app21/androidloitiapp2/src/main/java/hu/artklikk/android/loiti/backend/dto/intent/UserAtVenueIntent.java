package hu.artklikk.android.loiti.backend.dto.intent;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class UserAtVenueIntent extends UserIntent {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	public Long venueId;

	protected UserAtVenueIntent() {
		super();
	}

	protected UserAtVenueIntent(IntentType type) {
		super(type);
	}
}
