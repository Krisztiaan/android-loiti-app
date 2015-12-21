package hu.artklikk.android.loiti.backend.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TagCategory implements Parcelable {
	
	public TagCategory(){}
	
	public TagCategory(Parcel in) {
		name = in.readString();
		id = in.readLong();
	}
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String name;
	
	@JsonProperty("tagCategoryId")
	public Long id;
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeLong(id);
	}
	
	public static final Parcelable.Creator<TagCategory> CREATOR = new Parcelable.Creator<TagCategory>() {
		public TagCategory createFromParcel(Parcel in) {
			return new TagCategory(in);
		}
		
		public TagCategory[] newArray(int size) {
			return new TagCategory[size];
		}
	};
}
