package hu.artklikk.android.loiti.ui.core;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LTextView extends TextView {
	
	public LTextView(Context context) {
		super(context);
	}
	
	public LTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public LTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	public void setTypeface(Typeface tf, int style) {
		switch (style) {
			case 0:
				setTypeface(FontContainer.tf_normal);
				break;
			case 1:
				setTypeface(FontContainer.tf_bold);
				break;
			case 2:
				setTypeface(FontContainer.tf_italic);
				break;
			default:
				setTypeface(FontContainer.tf_normal);
		}
	}
	
}
