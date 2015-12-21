package hu.artklikk.android.loiti.ui.core;

import android.content.Context;
import android.graphics.Typeface;

public class FontContainer {

	public static Typeface tf_normal;
	public static Typeface tf_bold;
	public static Typeface tf_italic;
	
	public static void init(Context context) {
		if(tf_normal == null) {
			tf_normal = Typeface.createFromAsset(context.getAssets(), "font/MBEmpire-Book.otf");
		}
		if(tf_bold == null) {
			tf_bold = Typeface.createFromAsset(context.getAssets(), "font/MBEmpire-Bold.otf");
		}
		if(tf_italic == null) {
			tf_italic = Typeface.createFromAsset(context.getAssets(), "font/MBEmpire-Light.otf");
		}
	}
}
