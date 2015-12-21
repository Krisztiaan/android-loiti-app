package hu.artklikk.android.loiti.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Wifi {
	@JsonProperty("wifiId")
	public Long id;

	@JsonProperty
	public Long venueId;

	@JsonProperty
	public String name;

	@JsonProperty
	public String mac;
}
