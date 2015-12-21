package hu.artklikk.android.loiti.backend.dto;

import hu.artklikk.android.loiti.backend.dto.embeded.Address;
import hu.artklikk.android.loiti.backend.dto.embeded.BillingAddress;
import hu.artklikk.android.loiti.backend.dto.enums.AppRole;
import hu.artklikk.android.loiti.backend.dto.enums.UserGender;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class User {
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Long id;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String name;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String firstName;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String middleName;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String lastName;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String email;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public UserGender gender;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String locale;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String phone;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Address address;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public BillingAddress billingAddress;

	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<Badge> badges;

	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<Tag> tags;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String imgProfile;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public AppRole role;
}
