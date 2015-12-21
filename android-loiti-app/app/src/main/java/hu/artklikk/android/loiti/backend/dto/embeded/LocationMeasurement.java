package hu.artklikk.android.loiti.backend.dto.embeded;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationMeasurement extends Location {
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Float accuracy;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date time;

	public LocationMeasurement(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public LocationMeasurement(Double latitude, Double longitude, Float accuracy) {
		this(latitude, longitude);
		this.accuracy = accuracy;
	}

	public LocationMeasurement(Double latitude, Double longitude, Float accuracy, Date time) {
		this(latitude, longitude, accuracy);
		this.time = time;
	}
}
