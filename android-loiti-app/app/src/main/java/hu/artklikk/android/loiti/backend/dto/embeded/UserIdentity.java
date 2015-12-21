package hu.artklikk.android.loiti.backend.dto.embeded;

import hu.artklikk.android.loiti.backend.dto.enums.AuthenticationType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserIdentity {
	@JsonProperty
	private AuthenticationType type;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	private String identityId;
	@JsonProperty
	private String credential;

	protected UserIdentity() {
	}

	public UserIdentity(AuthenticationType type) {
		this.type = type;
	}

	public AuthenticationType getType() {
		return type;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(final String credential) {
		this.credential = credential;
	}
}
