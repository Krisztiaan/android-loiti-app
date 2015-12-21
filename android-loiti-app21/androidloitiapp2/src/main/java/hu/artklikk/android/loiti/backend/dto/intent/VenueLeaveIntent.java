package hu.artklikk.android.loiti.backend.dto.intent;

public class VenueLeaveIntent extends UserAtVenueIntent {

	private static final long serialVersionUID = 1L;

	public VenueLeaveIntent() {
		super(IntentType.LEAVE);
	}
}
