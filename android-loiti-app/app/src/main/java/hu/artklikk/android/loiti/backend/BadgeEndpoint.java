package hu.artklikk.android.loiti.backend;

import hu.artklikk.android.loiti.backend.dto.Badge;
import hu.artklikk.android.loiti.backend.rest.RestFactory;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

public class BadgeEndpoint {
	
	private static final String URL_BADGE = "badge";
	
	public static void getBadges(final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(URL_BADGE, new TypeReference<List<Badge>>() {}, callback);
	}
	
}
