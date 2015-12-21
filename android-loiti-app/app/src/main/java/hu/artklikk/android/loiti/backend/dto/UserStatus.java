package hu.artklikk.android.loiti.backend.dto;

import hu.artklikk.android.loiti.backend.dto.User;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserStatus {
	@JsonProperty
	public User user;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String deviceId;
	@JsonInclude(Include.NON_NULL)
	public Long tableId;
	@JsonProperty
	public Date arrivedAt;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date tableRequestAt;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date tableSeatAt;

	protected UserStatus() {
	}

	public UserStatus(User user) {
		this.user = user;
	}

	public UserStatus(User user, String deviceId) {
		this(user);
		this.deviceId = deviceId;
	}

	@JsonProperty
	public boolean isUserUnknown() {
		return (user == null);
	}

	@JsonIgnore
	protected boolean isSameDevice(String otherDeviceId) {
		if(deviceId == null) {
			return (otherDeviceId == null);
		}
		else return deviceId.equals(otherDeviceId);
	}

	@Override
	public boolean equals(Object object) {
		if(!(object instanceof UserStatus)) return false;

		final UserStatus otherStatus = (UserStatus) object;
		if(isUserUnknown()) {
			return isSameDevice(otherStatus.deviceId);
		}
		else return user.id.equals(otherStatus.user.id);
	}

	@Override
	public int hashCode() {
		if(user != null) return user.id.hashCode();
		else if(deviceId != null) return deviceId.hashCode();
		else return super.hashCode();
	}
}