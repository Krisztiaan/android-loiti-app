package hu.artklikk.android.loiti.backend.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemAvailability implements Parcelable {
	
	public ItemAvailability(){}
	
	public ItemAvailability(Parcel in) {
		venueId = in.readLong();
		price = in.readFloat();
		currency = in.readString();
		alacarte = in.readInt() == 0?true:false;
		featured = in.readInt() == 0?true:false;
	}
	
	@JsonProperty
	public Long venueId;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Float price;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public String currency;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Boolean alacarte;
	
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Boolean featured;
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(venueId);
		dest.writeFloat(price);
		dest.writeString(currency);
		dest.writeInt(alacarte ? 0 : -1);
		dest.writeInt(featured ? 0 : -1);
	}
	
	public static final Parcelable.Creator<ItemAvailability> CREATOR = new Parcelable.Creator<ItemAvailability>() {
		public ItemAvailability createFromParcel(Parcel in) {
			return new ItemAvailability(in);
		}
		
		public ItemAvailability[] newArray(int size) {
			return new ItemAvailability[size];
		}
	};
}
