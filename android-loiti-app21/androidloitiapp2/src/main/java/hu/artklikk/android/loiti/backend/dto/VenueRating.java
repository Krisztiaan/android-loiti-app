package hu.artklikk.android.loiti.backend.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VenueRating {
	@JsonProperty("venueRatingId")
	@JsonInclude(Include.NON_NULL)
	public Long id;

	@JsonProperty
	public int score;

	@JsonProperty
	public String comment;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date time;

	@JsonProperty
	public Long userId;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public User user;

	@JsonProperty
	public Long venueId;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long venueTableId;
}
