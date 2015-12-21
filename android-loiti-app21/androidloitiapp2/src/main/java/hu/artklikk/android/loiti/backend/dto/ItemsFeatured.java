package hu.artklikk.android.loiti.backend.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemsFeatured {
	@JsonProperty("itemsFeaturedId")
	@JsonInclude(Include.NON_NULL)
	public Long id;

	@JsonProperty
	public List<Item> items;

	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<Venue> venues;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date validFrom;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date validUntil;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String title;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Boolean active;
}
