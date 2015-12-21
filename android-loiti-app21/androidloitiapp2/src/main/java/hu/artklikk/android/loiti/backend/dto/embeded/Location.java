package hu.artklikk.android.loiti.backend.dto.embeded;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
	@JsonProperty
	public Double latitude;
	@JsonProperty
	public Double longitude;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Float radius;

	public Location() {
	}

	public Location(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Location(Double latitude, Double longitude, Float radius) {
		this(latitude, longitude);
		this.radius = radius;
	}
}
