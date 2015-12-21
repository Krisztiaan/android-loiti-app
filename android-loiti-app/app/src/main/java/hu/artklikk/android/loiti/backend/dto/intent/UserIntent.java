package hu.artklikk.android.loiti.backend.dto.intent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class UserIntent extends Intent {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long userId;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String deviceId;

	protected UserIntent() {
		super();
	}

	protected UserIntent(IntentType type) {
		super(type);
	}

	@JsonIgnore
	public boolean isUserAnonymous() {
		if(userId == null) return true;
		else if(userId == 0L) return true;
		else return false;
	}
}
