package hu.artklikk.android.loiti.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WeekDay {
	MON("monday"),
	TUE("tuesday"),
	WED("wednesday"),
	THU("thursday"),
	FRI("friday"),
	SAT("saturday"),
	SUN("sunday");

	private final String fullName;

	WeekDay(String fullName) {
		this.fullName = fullName;
	}

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}

	@JsonCreator
	public static WeekDay fromJson(String string) {
		return valueOf(string.toUpperCase());
	}

	public String getFullName() {
		return fullName;
	}
}
