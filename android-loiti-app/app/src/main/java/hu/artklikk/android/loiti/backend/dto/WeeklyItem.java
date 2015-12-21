package hu.artklikk.android.loiti.backend.dto;

import hu.artklikk.android.loiti.backend.dto.embeded.DailySubItemList;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeeklyItem {
	@JsonProperty("weeklyItemId")
	@JsonInclude(Include.NON_NULL)
	public Long id;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Item item;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date startsAt;
	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	public Date endsAt;
	@JsonProperty
	@JsonInclude(Include.NON_EMPTY)
	public Set<DailySubItemList> days;
}
