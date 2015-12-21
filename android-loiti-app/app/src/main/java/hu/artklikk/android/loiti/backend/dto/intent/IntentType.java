package hu.artklikk.android.loiti.backend.dto.intent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IntentType {
	ARRIVAL,
	TABLEREQUEST,
	TABLESEAT,
	ORDER,
	PURCHASE,
	LEAVE,
	UNKNOWN;

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}

	@JsonCreator
	public static IntentType fromJson(String string) {
		return valueOf(string.toUpperCase());
	}
}
