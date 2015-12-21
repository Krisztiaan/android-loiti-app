package hu.artklikk.android.loiti.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class QrCodeFunction extends QrCode {
	private Type type;
	
	@JsonCreator
	public QrCodeFunction(@JsonProperty("type") Type type, @JsonProperty("code") String code) {
		super(code);
		this.type = type;
	}
	
	@JsonProperty
	public Type getType() {
		return type;
	}
	
	public enum Type {
		LOGIN,
		TABLE;
		
		@JsonValue
		public String toJson() {
			return name().toLowerCase();
		}
		
		@JsonCreator
		public static Type fromJson(String string) {
			return valueOf(string.toUpperCase());
		}
	}
}
