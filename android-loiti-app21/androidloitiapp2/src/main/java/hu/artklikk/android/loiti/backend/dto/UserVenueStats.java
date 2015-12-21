package hu.artklikk.android.loiti.backend.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserVenueStats {
	@JsonProperty("statsId")
	@JsonInclude(Include.NON_NULL)
	public Long id;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long userId;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long venueId;
	@JsonProperty
	public int visits;
	@JsonProperty
	public int orders;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date lastVisit;
}
