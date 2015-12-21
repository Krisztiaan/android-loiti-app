package hu.artklikk.android.loiti.backend.dto;

import hu.artklikk.android.loiti.backend.dto.enums.FeedbackPostType;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FeedbackPost {
	@JsonProperty("feedbackPostId")
	@JsonInclude(Include.NON_NULL)
	public Long id;

	@JsonProperty
	public FeedbackPostType postType;

	@JsonProperty
	public String postText;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date time;

	@JsonProperty
	public Long userId;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public User user;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long venueId;
}
