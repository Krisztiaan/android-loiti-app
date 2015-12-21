package hu.artklikk.android.loiti.backend;

import hu.artklikk.android.loiti.backend.dto.VenueTable;
import hu.artklikk.android.loiti.backend.dto.intent.PurchaseIntent;
import hu.artklikk.android.loiti.backend.dto.intent.VenueArrivalIntent;
import hu.artklikk.android.loiti.backend.dto.intent.VenueLeaveIntent;
import hu.artklikk.android.loiti.backend.dto.intent.VenueTableSeatIntent;
import hu.artklikk.android.loiti.backend.rest.RestFactory;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;

import com.fasterxml.jackson.core.type.TypeReference;

public class IntentEndpoint {
	
	private static final String URL_ARRIVAL = "intent/arrival";
	private static final String URL_LEAVE = "intent/leave";
	private static final String URL_TAKE_SEAT = "intent/tableseat";
	private static final String URL_PAYMENT = "intent/purchase";
	
	public static void arrival(final VenueArrivalIntent intent, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPost(URL_ARRIVAL, intent, null, callback);
	}
	
	public static void leave(final VenueLeaveIntent intent, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPost(URL_LEAVE, intent, null, callback);
	}
	
	public static void takeSeat(final VenueTableSeatIntent intent, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPost(URL_TAKE_SEAT, intent, new TypeReference<VenueTable>() {}, callback);
	}
	
	public static void payment(final PurchaseIntent intent, final RestCallFinishListener callback) {
		RestFactory.getStatefullRest().doPost(URL_PAYMENT, intent, null, callback);
	}
	
}
