package hu.artklikk.android.loiti.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserImage {
	@JsonProperty
	public byte[] base64;
}
