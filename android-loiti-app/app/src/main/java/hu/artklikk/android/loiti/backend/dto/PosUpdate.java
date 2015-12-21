
package hu.artklikk.android.loiti.backend.dto;

import hu.artklikk.android.loiti.backend.dto.intent.Intent;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PosUpdate {
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long venueId;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public List<Intent> intents;
}
