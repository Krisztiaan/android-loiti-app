package hu.artklikk.android.loiti.backend.dto.intent;

import hu.artklikk.android.loiti.backend.dto.embeded.OrderType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VenueOrderIntent extends UserAtVenueIntent {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long tableId;

	@JsonProperty
	public OrderType orderType;

	@JsonProperty("promotionItemId")
	@JsonInclude(Include.NON_NULL)
	public Long promotionItemId;

	@JsonInclude(Include.NON_NULL)
	public Integer quantity;

	public VenueOrderIntent() {
		super(IntentType.ORDER);
	}
}
