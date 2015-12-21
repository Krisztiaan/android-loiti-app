package hu.artklikk.android.loiti.backend;

import hu.artklikk.android.loiti.backend.dto.Badge;
import hu.artklikk.android.loiti.backend.dto.LoginRequest;
import hu.artklikk.android.loiti.backend.dto.Promotion;
import hu.artklikk.android.loiti.backend.dto.User;
import hu.artklikk.android.loiti.backend.dto.UserImage;
import hu.artklikk.android.loiti.backend.dto.UserRegistrationRequest;
import hu.artklikk.android.loiti.backend.dto.UserVenueStats;
import hu.artklikk.android.loiti.backend.dto.embeded.UserIdentity;
import hu.artklikk.android.loiti.backend.dto.enums.AuthenticationType;
import hu.artklikk.android.loiti.backend.rest.RestFactory;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sromku.simple.fb.entities.Profile;

public class UserEndpoint {
	
	private static final String URL_USER = "user";
	private static final String URL_LOGIN = "user/login";
	private static final String URL_USER_IMAGE_UPLOAD = "user/%1$d/image/profile";
	private static final String URL_RESET_PASSWORD = "user/reset/password";
	private static final String URL_NEXT_BADGE = "user/%1$d/badge/next/venue/%2$d";
	private static final String URL_PROMOTIONS = "user/%1$d/promotion";
	private static final String URL_STATS = "user/%1$d/stats/%2$d";
	
	public static void registerWithFacebook(String token, String userId, Profile profile,
			RestCallFinishListener listener) {
		UserIdentity identity = new UserIdentity(AuthenticationType.FACEBOOK);
		identity.setCredential(token);
		identity.setIdentityId(userId);
		User u = new User();
		u.name = profile.getName();
		u.email = profile.getEmail();
		u.firstName = profile.getFirstName();
		u.lastName = profile.getLastName();
		
		UserRegistrationRequest reg = new UserRegistrationRequest();
		List<UserIdentity> iList = new ArrayList<UserIdentity>(1);
		iList.add(identity);
		reg.identity = iList;
		reg.user = u;
		
		RestFactory.getStatefullRest().doPost(URL_USER, reg, new TypeReference<User>() {}, listener);
		
	}
	
	public static void registerWithSlotMachine(String name, String passwordHexa, String email, 	RestCallFinishListener callback) {
		UserIdentity identity = new UserIdentity(AuthenticationType.PASSWORD);
		identity.setCredential(passwordHexa);
		List<UserIdentity> identityList = new ArrayList<UserIdentity>(1);
		identityList.add(identity);
		
		User user = new User();
		user.name = name;
		user.email = email;
		
		UserRegistrationRequest request = new UserRegistrationRequest();
		request.user = user;
		request.identity = identityList;
		
		RestFactory.getStatefullRest().doPost(URL_USER, request, new TypeReference<User>() {}, callback);
		
	}
	
	public static void uploadPhoto(final Long userId, final byte[] base64, final RestCallFinishListener callback) {
		UserImage request = new UserImage();
		request.base64 = base64;
		RestFactory.getStatefullRest().doPost(String.format(URL_USER_IMAGE_UPLOAD, userId), request, null, callback);
	}
	
	public static void getUser(final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(URL_USER, new TypeReference<User>() {}, callback);
	}
	
	public static void getUser(final Long userId, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(URL_USER + "/" + userId, new TypeReference<User>() {}, callback);
	}
	
	public static void login(final LoginRequest request, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPost(URL_LOGIN, request, new TypeReference<User>() {}, callback);
	}
	
	public static void updateUser(final User user, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPut(URL_USER, user, new TypeReference<User>() {}, callback);
	}
	
	public static void resetPassword(final LoginRequest request, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPost(URL_RESET_PASSWORD, request, null, callback);
	}
	
	public static void getNextBadge(final Long userId, final Long venueId, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(String.format(URL_NEXT_BADGE, userId, venueId), new TypeReference<Badge>() {}, callback);
	}
	
	public static void getPromotions(final Long userId, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(String.format(URL_PROMOTIONS, userId), new TypeReference<List<Promotion>>() {}, callback);
	}
	
	public static void getStatsAtVenue(final Long userId, final Long venueId, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doGet(String.format(URL_STATS, userId, venueId), new TypeReference<UserVenueStats>() {}, callback);
	}
}
