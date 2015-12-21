package hu.artklikk.android.loiti.backend.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag implements Parcelable {
	
	public Tag(){}
	
	public Tag(Parcel in) {
		name = in.readString();
		id = in.readLong();
		category = in.readParcelable(TagCategory.class.getClassLoader());
	}
	
	@JsonProperty
	public String name;

	@JsonProperty("tagId")
	public Long id;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public TagCategory category;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeLong(id);
		dest.writeParcelable(category, 0);
	}
	
	public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
		public Tag createFromParcel(Parcel in) {
			return new Tag(in);
		}
		
		public Tag[] newArray(int size) {
			return new Tag[size];
		}
	};
}
