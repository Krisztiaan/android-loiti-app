package hu.artklikk.android.loiti.backend.dto;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemCategory implements Parcelable {
	
	public ItemCategory(){}
	
	public ItemCategory(Parcel in) {
		name = in.readString();
		parentCategory = in.readParcelable(ItemCategory.class.getClassLoader());
		id = in.readLong();
		tags = in.readArrayList(Tag.class.getClassLoader());
		imgProfile = in.readString();
	}
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String name;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public ItemCategory parentCategory;

	@JsonProperty("itemCategoryId")
	public Long id;

	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public List<Tag> tags;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String imgProfile;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeParcelable(parentCategory, 0);
		dest.writeLong(id);
		dest.writeList(tags);
		dest.writeString(imgProfile);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((imgProfile == null) ? 0 : imgProfile.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((parentCategory == null) ? 0 : parentCategory.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemCategory other = (ItemCategory) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imgProfile == null) {
			if (other.imgProfile != null)
				return false;
		} else if (!imgProfile.equals(other.imgProfile))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentCategory == null) {
			if (other.parentCategory != null)
				return false;
		} else if (!parentCategory.equals(other.parentCategory))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}
	
	
}
