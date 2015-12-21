package hu.artklikk.android.loiti.backend.dto;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item implements Parcelable {
	
	public Item(){}
	
	public Item(Parcel in) {
		id = in.readLong();
		name = in.readString();
		tags = in.readArrayList(Tag.class.getClassLoader());
		venues = in.readArrayList(ItemAvailability.class.getClassLoader());
		category = in.readParcelable(ItemCategory.class.getClassLoader());
		imgProfile = in.readString();
	}
	
	@JsonProperty("itemId")
	public Long id;
	
	@JsonProperty
	public String name;
	
	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<Tag> tags;
	
	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<ItemAvailability> venues;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public ItemCategory category;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String imgProfile;
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeList(tags);
		dest.writeList(venues);
		dest.writeParcelable(category, 0);
		dest.writeString(imgProfile);
	}
	
	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}
		
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};
	
}
