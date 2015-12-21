package hu.artklikk.android.loiti.backend;

import hu.artklikk.android.loiti.backend.dto.Item;
import hu.artklikk.android.loiti.backend.dto.SubItem;
import hu.artklikk.android.loiti.backend.dto.Venue;
import hu.artklikk.android.loiti.backend.dto.WeeklyItem;
import hu.artklikk.android.loiti.backend.dto.embeded.DailySubItemList;
import hu.artklikk.android.loiti.backend.rest.RestFactory;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.core.type.TypeReference;

public class VenueEndpoint {
	
	public static final long MENZA_VENUE_ID = 5454651147157504L;
	public static final long TEST_VENUE_ID = 5150480221077504L;
	
	private static final String URL_ALL_VENUES = "/venue";
	private static final String URL_VENUE = "/venue/%1$d";
	private static final String URL_WEEKLY_MENU = "/venue/%1$d/item/weekly";
	private static final String URL_FEATURED_MENU = "/venue/%1$d/item/featured";
	private static final String URL_ALACARTE_MENU = "/venue/%1$d/item/alacarte";
	
	public static void getVenues(RestCallFinishListener callback) {
		if (callback == null)
			return;
		
		RestFactory.getStatefullRest().doGet(URL_ALL_VENUES, new TypeReference<List<Venue>>() {}, callback);
	}
	
	public static void getVenue(final long venueId, RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(String.format(URL_VENUE, venueId), new TypeReference<Venue>() {}, callback);
	}
	
	public static void getWeeklyItems(final long venueId, final RestCallFinishListener callback) {
		RestCallFinishListener innerCallback = new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				List<WeeklyItem> response = (List<WeeklyItem>) result;
				callback.onFinish(new WeeklyItemsContainer(response));
			}
			
			@Override
			public void onException(Exception e) {
				callback.onException(e);
			}
		};
		RestFactory.getStatefullRest().doGet(String.format(URL_WEEKLY_MENU, venueId),
				new TypeReference<List<WeeklyItem>>() {}, innerCallback);
	}
	
	public static void getFeaturedItems(final long venueId, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(String.format(URL_FEATURED_MENU, venueId),
				new TypeReference<List<Item>>() {}, callback);
	}
	
	public static void getAlacarteItems(final long venueId, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(String.format(URL_ALACARTE_MENU, venueId),
				new TypeReference<List<Item>>() {}, callback);
	}
	
	public static class WeeklyItemsContainer {
		private List<List<SubItem>> weeklyMenu;
		private String interval;
		
		public WeeklyItemsContainer(List<WeeklyItem> in) {
			weeklyMenu = new ArrayList<List<SubItem>>();
			for (int i = 0; i < 7; i++) {
				weeklyMenu.add(new ArrayList<SubItem>());
			}
			for (WeeklyItem weeklyList : in) {
				for (DailySubItemList dailyList : weeklyList.days) {
					for (SubItem dailyItem : dailyList.getSubItems()) {
						weeklyMenu.get(dailyList.getDay().ordinal()).add(dailyItem);
					}
				}
			}
			SimpleDateFormat start = new SimpleDateFormat("yyyy.MM.dd.", Locale.ENGLISH);
			SimpleDateFormat end = new SimpleDateFormat("MM.dd.", Locale.ENGLISH);
			if (!in.isEmpty()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(in.get(0).startsAt);
				calendar.add(Calendar.DATE, 4);
				Date endDate = calendar.getTime();
				interval = start.format(in.get(0).startsAt) + " - " + end.format(endDate);
			}
			
		}
		
		public List<List<SubItem>> getWeeklyItems() {
			return weeklyMenu;
		}
		
		public String getInterval() {
			return interval;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((interval == null) ? 0 : interval.hashCode());
			result = prime * result + ((weeklyMenu == null) ? 0 : weeklyMenu.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WeeklyItemsContainer))
				return false;
			WeeklyItemsContainer other = (WeeklyItemsContainer) obj;
			if (interval == null) {
				if (other.interval != null)
					return false;
			}
			else if (!interval.equals(other.interval))
				return false;
			if (weeklyMenu == null) {
				if (other.weeklyMenu != null)
					return false;
			}
			else if (!weeklyMenu.equals(other.weeklyMenu))
				return false;
			return true;
		}
		
	}
}
