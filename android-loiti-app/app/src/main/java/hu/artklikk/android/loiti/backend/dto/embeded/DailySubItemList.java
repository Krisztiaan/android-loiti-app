package hu.artklikk.android.loiti.backend.dto.embeded;

import hu.artklikk.android.loiti.backend.dto.SubItem;
import hu.artklikk.android.loiti.backend.dto.WeekDay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailySubItemList implements Comparable<DailySubItemList>, Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	private WeekDay day;
	@JsonProperty
	private List<SubItem> subItems = new ArrayList<SubItem>();
	private Set<Long> likes;

	protected DailySubItemList() {
	}

	public DailySubItemList(WeekDay day) {
		this.day = day;
	}

	public DailySubItemList(WeekDay day, List<SubItem> subItems) {
		this(day);
		this.subItems = subItems;
	}

	public WeekDay getDay() {
		return day;
	}

	public void setDay(WeekDay day) {
		this.day = day;
	}

	public List<SubItem> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<SubItem> subItems) {
		this.subItems = subItems;
	}

	public Set<Long> getLikes() {
		return likes;
	}

	public void setLikes(final Set<Long> likes) {
		this.likes = likes;
	}

	public boolean addLike(final Long userId) {
		if(likes == null) likes = new HashSet<Long>();
		return likes.add(userId);
	}

	public boolean hasLiked(final Long userId) {
		if(likes == null) return false;
		return likes.contains(userId);
	}

	@Override
	public int compareTo(DailySubItemList otherDay) {
		if(day == null) return -1;

		return day.compareTo(otherDay.getDay());
	}
}
