package hu.artklikk.android.loiti.ui.util;

import hu.artklikk.android.loiti.LoitiApplication;

public class PopupUtil {
	
	public enum PopupType {
		INSIDE_START;
	}
	
	public static boolean isDialogNeedsToShow(PopupType type) {
		return LoitiApplication.getPreferences(type.name(), true);
	}
	
	public static void setDialogNotToShow(PopupType type) {
		LoitiApplication.savePreferences(type.name(), false);
	}
	
}
