package hu.artklikk.android.loiti.backend.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
	CASH,
	CARD;

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}

	@JsonCreator
	public static PaymentMethod fromJson(String string) {
		return valueOf(string.toUpperCase());
	}
}
