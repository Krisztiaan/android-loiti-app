package hu.artklikk.android.loiti.backend.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthenticationType {
	PASSWORD,
	FACEBOOK,
	QRCODE,
	TOKEN;

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}

	@JsonCreator
	public static AuthenticationType fromJson(String string) {
		return valueOf(string.toUpperCase());
	}
}
