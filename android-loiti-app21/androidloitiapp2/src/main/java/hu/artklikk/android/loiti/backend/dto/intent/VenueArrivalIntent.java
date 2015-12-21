package hu.artklikk.android.loiti.backend.dto.intent;

public class VenueArrivalIntent extends UserAtVenueIntent {

	private static final long serialVersionUID = 1L;

	public VenueArrivalIntent() {
		super(IntentType.ARRIVAL);
	}
}
