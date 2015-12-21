package hu.artklikk.android.loiti.backend;

import hu.artklikk.android.loiti.backend.dto.FeedbackPost;
import hu.artklikk.android.loiti.backend.dto.VenueRating;
import hu.artklikk.android.loiti.backend.rest.RestFactory;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

public class FeedbackEndpoint {
	
	private static final String URL_RATING = "feedback/venue/rating";
	private static final String URL_FEEDBACK = "feedback/post";
	
	public static void rate(final VenueRating rating, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPost(URL_RATING, rating, null, callback);
	}
	
	public static void feedback(final FeedbackPost feedback, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPost(URL_FEEDBACK, feedback, null, callback);
	}
	
}
