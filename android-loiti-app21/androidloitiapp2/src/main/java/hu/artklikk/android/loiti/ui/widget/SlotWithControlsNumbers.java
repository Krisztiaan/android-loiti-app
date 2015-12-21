package hu.artklikk.android.loiti.ui.widget;

import hu.artklikk.android.loiti.R;
import android.content.Context;
import android.util.AttributeSet;

public class SlotWithControlsNumbers extends SlotWithControls {
	
	public SlotWithControlsNumbers(Context context) {
		super(context);
	}
	
	public SlotWithControlsNumbers(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SlotWithControlsNumbers(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected int getSlotResId() {
		return R.layout.item_slot_with_controls_numbers;
	}
	
}
