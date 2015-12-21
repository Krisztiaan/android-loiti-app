package hu.artklikk.android.loiti.backend.dto.intent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VenueTableSeatIntent extends UserAtVenueIntent {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long tableId;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String tableCode;

	public VenueTableSeatIntent() {
		super(IntentType.TABLESEAT);
	}
}
