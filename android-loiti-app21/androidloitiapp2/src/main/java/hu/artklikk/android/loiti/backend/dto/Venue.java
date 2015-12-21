package hu.artklikk.android.loiti.backend.dto;

import hu.artklikk.android.loiti.backend.dto.embeded.Address;
import hu.artklikk.android.loiti.backend.dto.embeded.Contact;
import hu.artklikk.android.loiti.backend.dto.embeded.Location;
import hu.artklikk.android.loiti.backend.dto.embeded.OrderType;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Venue {
	@JsonProperty("venueId")
	public Long id;
	
	@JsonProperty
	public String name;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String storeId;
	
	@JsonProperty
	public Location location;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Address address;
	
	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<OrderType> orderTypes;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Contact contact;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String featuredItemsLine;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String description;
}
