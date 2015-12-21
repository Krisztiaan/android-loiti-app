package hu.artklikk.android.loiti.backend.dto;

import hu.artklikk.android.loiti.backend.dto.intent.PurchaseIntent;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Purchase extends PurchaseIntent {

	private static final long serialVersionUID = 1L;

	@JsonProperty("purchaseId")
	@JsonInclude(Include.NON_NULL)
	public Long id;

	@JsonProperty
	public Float value;

	@JsonProperty
	public String currency;

	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<Item> items;
}
