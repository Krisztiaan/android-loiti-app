package hu.artklikk.android.loiti.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Badge implements Comparable<Badge> {
	@JsonProperty("badgeId")
	public Long id;

	@JsonProperty
	public String title;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Integer fromVisits;

	@Override
	public int compareTo(Badge another) {
		if(fromVisits == null) {
			if(another.fromVisits == null) {
				return 0;
			}
			return -1;
		}
		return fromVisits.compareTo(another.fromVisits);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromVisits == null) ? 0 : fromVisits.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Badge))
			return false;
		Badge other = (Badge) obj;
		if (fromVisits == null) {
			if (other.fromVisits != null)
				return false;
		}
		else if (!fromVisits.equals(other.fromVisits))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		return true;
	}
	
}
