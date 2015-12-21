package hu.artklikk.android.loiti.backend.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FeedbackPostType {
	APP,
	VENUE,
	SERVICE,
	MEAL,
	CUSTOM;
	
	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}
	
	@JsonCreator
	public static FeedbackPostType fromJson(String string) {
		try {
			return valueOf(string.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			return CUSTOM;
		}
		catch (NullPointerException e) {
			return CUSTOM;
		}
	}
	
	public static FeedbackPostType fromIndex(int index) {
		if (index == 0) {
			throw new IllegalArgumentException("Spinner - no item index!");
		}
		if (index < 1 || index > 4) {
			throw new IndexOutOfBoundsException("Spinner - out of bounds index!");
		}
		switch (index) {
			case 1:
				return APP;
			case 2:
				return VENUE;
			case 3:
				return SERVICE;
			case 4:
				return MEAL;
			default:
				return CUSTOM;
		}
	}
}
