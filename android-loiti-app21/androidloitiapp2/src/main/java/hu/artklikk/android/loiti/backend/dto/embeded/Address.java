package hu.artklikk.android.loiti.backend.dto.embeded;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String city;
	protected String postalCode;
	protected String street;
	@JsonInclude(Include.NON_NULL)
	private String streetNumber;
	@JsonInclude(Include.NON_NULL)
	private String floor;
	@JsonInclude(Include.NON_NULL)
	protected String country;
	@JsonInclude(Include.NON_NULL)
	protected String comment;

	public Address() {
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if("".equals(country)) country = null;
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if("".equals(city)) city = null;
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		if("".equals(street)) street = null;
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		if("".equals(streetNumber)) streetNumber = null;
		this.streetNumber = streetNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		if("".equals(postalCode)) postalCode = null;
		this.postalCode = postalCode;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		if("".equals(floor)) floor = null;
		this.floor = floor;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		if("".equals(comment)) comment = null;
		this.comment = comment;
	}
}
