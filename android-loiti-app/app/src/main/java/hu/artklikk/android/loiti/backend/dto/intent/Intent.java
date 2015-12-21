package hu.artklikk.android.loiti.backend.dto.intent;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class Intent implements Serializable, Comparable<Intent> {

	private static final long serialVersionUID = 1L;

	@JsonProperty("intentId")
	@JsonInclude(Include.NON_NULL)
	public Long id;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date time;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public final IntentType type;

	protected Intent() {
		this.type = null;
	}

	protected Intent(IntentType type) {
		this.type = type;
	}

	@Override
	public int compareTo(Intent otherIntent) {
		if(time == null) {
			if(otherIntent.time == null) return 0;
			else return -1;
		}
		else if(otherIntent.time == null) return 1;

		return this.time.compareTo(otherIntent.time);
	}
}
