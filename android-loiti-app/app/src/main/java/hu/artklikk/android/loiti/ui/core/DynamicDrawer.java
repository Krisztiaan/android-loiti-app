package hu.artklikk.android.loiti.ui.core;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

public class DynamicDrawer extends RelativeLayout {

	public DynamicDrawer(Context context) {
		super(context);
	}
	
	public DynamicDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public DynamicDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int width = calcDrawerWantedWidth();
		int height = MeasureSpec.getSize(heightMeasureSpec);
	    setMeasuredDimension(width, height);
	    
	    super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
	    		MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}
	
	private int calcDrawerWantedWidth() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		return (int) (metrics.widthPixels - (metrics.density * 50));
	}

}
