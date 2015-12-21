package hu.artklikk.android.loiti.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class VenueTable {
	@JsonProperty("tableId")
	@JsonInclude(Include.NON_NULL)
	public Long id;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String name;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Integer designation;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String code;

	@JsonProperty
	public Long venueId;
}
