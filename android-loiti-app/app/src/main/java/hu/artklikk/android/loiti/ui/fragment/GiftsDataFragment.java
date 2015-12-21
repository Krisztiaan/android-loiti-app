package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.backend.BadgeEndpoint;
import hu.artklikk.android.loiti.backend.UserEndpoint;
import hu.artklikk.android.loiti.backend.dto.Badge;
import hu.artklikk.android.loiti.backend.dto.Promotion;
import hu.artklikk.android.loiti.backend.dto.User;
import hu.artklikk.android.loiti.backend.dto.UserVenueStats;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class GiftsDataFragment extends Fragment {
	
	public static final String TAG = GiftsDataFragment.class.getSimpleName();
	
	public interface GiftDescriptorReadyListener {
		public void onGiftDescriptorReady();
		public void onUserNotVisitedReady();
	}
	
	private GiftDescriptorReadyListener callback;
	
	private GiftDescriptor giftDescriptor;
	
	private Set<Badge> userHighestBadgeSet;
	private boolean userBadgesFinished = false;
	private Set<Badge> highestBadgeSet;
	private boolean highestBadgesFinished = false;
	
	private String badgeName;
	private Boolean isTopLevel;
	private String thumbnail;
	private String actionInside;
	private String actionRemote;
	private String visitCount;
	private String description;
	
	private boolean badgeRelatedFinished = false;
	private boolean promoRelatedFinished = false;
	private boolean statRelatedFinished = false;
	private boolean isVisited = false;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (GiftDescriptorReadyListener) activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		callback = null;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		UserEndpoint.getUser(new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				User user = (User) result;
				List<Badge> userBadgeList = user.badges;
				if (userBadgeList != null && !userBadgeList.isEmpty()) {
					userHighestBadgeSet = getHighestBadges(userBadgeList);
					userBadgesFinished = true;
					if (highestBadgesFinished) {
						setBadgeRelated(highestBadgeSet, userHighestBadgeSet);
					}
				}
			}
			
			@Override
			public void onException(Exception e) {
				
			}
		});
		
		BadgeEndpoint.getBadges(new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				@SuppressWarnings("unchecked")
				List<Badge> badges = (List<Badge>) result;
				highestBadgeSet = getHighestBadges(badges);
				highestBadgesFinished = true;
				if (userBadgesFinished) {
					setBadgeRelated(highestBadgeSet, userHighestBadgeSet);
				}
			}
			
			@Override
			public void onException(Exception e) {
				e.printStackTrace();
			}
		});
		
		UserEndpoint.getPromotions(LoitiApplication.getUserId(), new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				@SuppressWarnings("unchecked")
				List<Promotion> response = (List<Promotion>) result;
				if(response != null && !response.isEmpty()) {
					Promotion promotion = response.get(0);
					thumbnail = promotion.imgProfile;
					actionInside = promotion.actionRequired;
					actionRemote = promotion.actionRequiredRemote;
					description = promotion.description;
					promoRelatedFinished = true;
					checkEverythingFinished();
				}
			}
			
			@Override
			public void onException(Exception e) {
				e.printStackTrace();
			}
		});
		
		UserEndpoint.getStatsAtVenue(LoitiApplication.getUserId(), LoitiApplication.getVenueId(), new RestCallFinishListener() {
			
			@Override
			public void onFinish(Object result) {
				UserVenueStats response = (UserVenueStats) result;
				visitCount = Integer.toString(response.visits);
				isVisited = response.visits > 0;
				statRelatedFinished = true;
				checkEverythingFinished();
			}
			
			@Override
			public void onException(Exception e) {
				
			}
		});
	}
	
	private static final Set<Badge> getHighestBadges(Collection<Badge> badges) {
		Badge maxBadge = Collections.max(badges);
		int max = maxBadge.fromVisits;
		HashSet<Badge> toReturn = new HashSet<Badge>();
		for (Badge badge : badges) {
			if (badge.fromVisits == max) {
				toReturn.add(badge);
			}
		}
		return toReturn;
	}
	
	private void setBadgeRelated(Set<Badge> highestBadges, Set<Badge> userHighestBadges) {
		isTopLevel = userHighestBadges.containsAll(highestBadges);
		badgeName = new String();
		for (Badge badge : userHighestBadges) {
			badgeName = badgeName.isEmpty() ? badgeName + badge.title : badgeName + " | " + badge.title;
		}
		badgeRelatedFinished = true;
		checkEverythingFinished();
	}
	
	private void checkEverythingFinished() {
		if(badgeRelatedFinished && promoRelatedFinished && statRelatedFinished) {
			giftDescriptor = new GiftDescriptor(badgeName, isTopLevel, thumbnail, actionInside, actionRemote, visitCount, description);
			if(callback != null) {
				callback.onGiftDescriptorReady();
			}
		}
		else if (statRelatedFinished && !isVisited) {
			if (callback != null) {
				callback.onUserNotVisitedReady();
			}
		} 
	}
	
	public boolean isGiftDescriptorReady() {
		return giftDescriptor != null;
	}
	
	public GiftDescriptor getGiftDescriptor(boolean isInside) {
		return new GiftDescriptor(giftDescriptor, isInside);
	}
	
	public static class GiftDescriptor {
		
		private static final String KEY_BADGE_NAME = "KEY_BADGE_NAME";
		private static final String KEY_IS_TOP_LEVEL = "KEY_IS_TOP_LEVEL";
		private static final String KEY_THUMBNAIL = "KEY_THUMBNAIL";
		private static final String KEY_ACTION_INSIDE = "KEY_ACTION_INSIDE";
		private static final String KEY_ACTION_REMOTE = "KEY_ACTION_REMOTE";
		private static final String KEY_VISIT_COUNT = "KEY_VISIT_COUNT";
		private static final String KEY_DESCRIPTION = "KEY_DESCRIPTION";
		private static final String KEY_IS_INSIDE = "KEY_IS_INSIDE";
		
		public GiftDescriptor(String badgeName, boolean isTopLevel, String thumbnail, String actionInside,
				String actionRemote, String visitCount, String description) {
			this.badgeName = badgeName;
			this.isTopLevel = isTopLevel;
			this.thumbnail = thumbnail;
			this.actionInside = actionInside;
			this.actionRemote = actionRemote;
			this.visitCount = visitCount;
			this.description = description;
		}
		
		public GiftDescriptor(GiftDescriptor descriptor, boolean isInside) {
			this.badgeName = descriptor.badgeName;
			this.isTopLevel = descriptor.isTopLevel;
			this.thumbnail = descriptor.thumbnail;
			this.actionInside = descriptor.actionInside;
			this.actionRemote = descriptor.actionRemote;
			this.visitCount = descriptor.visitCount;
			this.description = descriptor.description;
			this.isInside = isInside;
		}
		
		public GiftDescriptor(Bundle bundle) {
			this.badgeName = bundle.getString(KEY_BADGE_NAME);
			this.isTopLevel = bundle.getBoolean(KEY_IS_TOP_LEVEL);
			this.thumbnail = bundle.getString(KEY_THUMBNAIL);
			this.actionInside = bundle.getString(KEY_ACTION_INSIDE);
			this.actionRemote = bundle.getString(KEY_ACTION_REMOTE);
			this.visitCount = bundle.getString(KEY_VISIT_COUNT);
			this.description = bundle.getString(KEY_DESCRIPTION);
			this.isInside = bundle.getBoolean(KEY_IS_INSIDE);
		}
		
		private String badgeName;
		private boolean isTopLevel;
		private String thumbnail;
		private String actionInside;
		private String actionRemote;
		private String visitCount;
		private String description;
		
		private boolean isInside;
		
		public String getBadgeName() {
			return badgeName;
		}
		
		public boolean isTopLevel() {
			return isTopLevel;
		}
		
		public String getThumbnail() {
			return thumbnail;
		}
		
		public String getActionText() {
			return isInside ? actionInside : actionRemote;
		}
		
		public String getVisitCount() {
			return visitCount;
		}
		
		public String getDescription() {
			return description;
		}
		
		public Bundle toBundle() {
			Bundle bundle = new Bundle();
			bundle.putString(KEY_BADGE_NAME, badgeName);
			bundle.putBoolean(KEY_IS_TOP_LEVEL, isTopLevel);
			bundle.putString(KEY_THUMBNAIL, thumbnail);
			bundle.putString(KEY_ACTION_INSIDE, actionInside);
			bundle.putString(KEY_ACTION_REMOTE, actionRemote);
			bundle.putString(KEY_VISIT_COUNT, visitCount);
			bundle.putString(KEY_DESCRIPTION, description);
			bundle.putBoolean(KEY_IS_INSIDE, isInside);
			return bundle;
		}

		@Override
		public String toString() {
			return "GiftDescriptor [badgeName=" + badgeName + ", isTopLevel=" + isTopLevel + ", thumbnail=" + thumbnail
					+ ", actionInside=" + actionInside + ", actionRemote=" + actionRemote + ", visitCount="
					+ visitCount + ", description=" + description + ", isInside=" + isInside + "]";
		}
		
	}
	
}
