package hu.artklikk.android.loiti.backend.dto;

import hu.artklikk.android.loiti.backend.dto.embeded.UserIdentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistrationRequest {
	@JsonProperty
	public User user;
	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<UserIdentity> identity;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String deviceId;
}
