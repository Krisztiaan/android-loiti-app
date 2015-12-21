package hu.artklikk.android.loiti.backend.dto.intent;

import hu.artklikk.android.loiti.backend.dto.embeded.BillingAddress;
import hu.artklikk.android.loiti.backend.dto.enums.PaymentMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PurchaseIntent extends UserAtVenueIntent {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long tableId;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public PaymentMethod paymentMethod;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public BillingAddress billingAddress;

	public PurchaseIntent() {
		super(IntentType.PURCHASE);
	}
}
