package hu.artklikk.android.loiti.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrCode {
	private String code;

	public QrCode(final String code) {
		this.code = code;
	}

	@JsonProperty
	public String getCode() {
		return code;
	}
}
