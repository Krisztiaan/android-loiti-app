package hu.artklikk.android.loiti.backend.dto;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PosUpdateRequest {
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date fromTime;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date toTime;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long venueId;

	@JsonProperty("lastProcessed")
	@JsonInclude(Include.NON_EMPTY)
	public Set<Long> processedIntents;
}
