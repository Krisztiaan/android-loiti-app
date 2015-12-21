package hu.artklikk.android.loiti.backend.dto.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FacebookProfile {
	@JsonProperty("id")
	public String facebookId;

	@JsonProperty
	public String name;

	@JsonProperty("first_name")
	public String firstName;

	@JsonProperty("last_name")
	public String lastName;

	@JsonProperty("middle_name")
	@JsonInclude(Include.NON_EMPTY)
	public String middleName;

	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public String email;

	public String gender;

	public String locale;
}
