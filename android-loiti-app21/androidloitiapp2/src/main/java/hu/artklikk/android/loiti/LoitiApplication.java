package hu.artklikk.android.loiti;

import hu.artklikk.android.loiti.backend.VenueEndpoint;
import hu.artklikk.android.loiti.backend.dto.LoginRequest;
import hu.artklikk.android.loiti.backend.rest.core.RestBase;
import hu.artklikk.android.loiti.ui.activity.BillActivity.BillingData;
import hu.artklikk.android.loiti.ui.core.FontContainer;
import hu.artklikk.android.loiti.utils.LoginRequestStore;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class LoitiApplication extends Application {
	
	private static SharedPreferences preferences;
	private static LoginRequestStore loginStore;
	private static final String KEY_LOGIN = "KEY_LOGIN";
	private static final String KEY_LAST_TABLE_ID = "KEY_LOGIN";
	private static final Long CURRENT_VENUE_ID = VenueEndpoint.MENZA_VENUE_ID;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		FontContainer.init(getApplicationContext());
		
		preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		loginStore = new LoginRequestStore(this);
		
		Resources res = getResources();
		String languageHeaderName = res.getString(R.string.language_header_name);
		String languageHeaderValue = res.getString(R.string.language_header_value);
		RestBase.setLanguageHeader(languageHeaderName, languageHeaderValue);
	}
	
	public static void savePreferences(String key, Boolean value) {
		preferences.edit().putBoolean(key, value).commit();
	}
	
	public static Boolean getPreferences(String key, Boolean defValue) {
		return preferences.getBoolean(key, defValue);
	}
	
	public static void savePreferences(String key, Long value) {
		preferences.edit().putLong(key, value).commit();
	}
	
	public static Long getPreferences(String key, Long defValue) {
		return preferences.getLong(key, defValue);
	}
	
	public static Long getUserId() {
		LoginRequest loginRequest = getLoginData();
		if (loginRequest == null) {
			return null;
		}
		if (loginRequest.userId == null) {
			return null;
		}
		return loginRequest.userId;
	}
	
	public static Long getVenueId() {
		return CURRENT_VENUE_ID;
	}
	
	public static void setLoginData(LoginRequest loginData) {
		loginStore.add(KEY_LOGIN, loginData);
	}
	
	public static LoginRequest getLoginData() {
		return loginStore.getItem(KEY_LOGIN);
	}
	
	public static boolean hasLoginData() {
		return loginStore.getItem(KEY_LOGIN) != null;
	}
	
	public static void saveLastTableId(Long tableId) {
		if(tableId == null) {
			preferences.edit().remove(KEY_LAST_TABLE_ID).apply();
		}
		else {
			preferences.edit().putLong(KEY_LAST_TABLE_ID, tableId).apply();			
		}
	}
	
	public static Long getLastTableId() {
		long toReturn = preferences.getLong(KEY_LAST_TABLE_ID, -1L);
		return toReturn == -1L ? null : toReturn;
	}
	
	public static void saveBillingData(BillingData data) {
		preferences.edit() //
				.putString(BillingData.KEY_BILLDATA_NAME, data.getName()) //
				.putString(BillingData.KEY_BILLDATA_TAXNUMBER, data.getTaxNumber()) //
				.putString(BillingData.KEY_BILLDATA_POSTALCODE, data.getPostalCode()) //
				.putString(BillingData.KEY_BILLDATA_CITY, data.getCity()) //
				.putString(BillingData.KEY_BILLDATA_ADDRESS, data.getAddress()) //
				.apply();
	}
	
	public static BillingData getBillingData() {
		String name = preferences.getString(BillingData.KEY_BILLDATA_NAME, null);
		String taxNumber = preferences.getString(BillingData.KEY_BILLDATA_TAXNUMBER, null);
		String postalCode = preferences.getString(BillingData.KEY_BILLDATA_POSTALCODE, null);
		String city = preferences.getString(BillingData.KEY_BILLDATA_CITY, null);
		String address = preferences.getString(BillingData.KEY_BILLDATA_ADDRESS, null);
		
		if (name == null || taxNumber == null || postalCode == null || city == null || address == null) {
			return null;
		}
		return new BillingData(name, taxNumber, postalCode, city, address);
	}
}
