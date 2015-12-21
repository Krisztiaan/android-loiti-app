package hu.artklikk.android.loiti.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorInfo {
	@JsonProperty
	public int status;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public final String message;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String method;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public final String uri;
	@JsonProperty
	public final Error error;
	
	@JsonCreator
	public ErrorInfo(@JsonProperty("status") int status, @JsonProperty("message") String message,
			@JsonProperty("method") String method, @JsonProperty("uri") String uri, @JsonProperty("error") Error error) {
		this.status = status;
		this.message = message;
		this.method = method;
		this.uri = uri;
		this.error = error;
	}
	
	public static class Error {
		@JsonProperty
		public final String type;
		@JsonProperty
		@JsonInclude(Include.NON_NULL)
		public final String message;
		
		@JsonCreator
		public Error(@JsonProperty("type") String type, @JsonProperty("message") String message) {
			this.type = type;
			this.message = message;
		}
	}
}
