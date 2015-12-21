package hu.artklikk.android.loiti.ui.widget;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.widget.Slot.OnSlotMoveListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@EViewGroup(R.layout.item_slotmachine)
public class SlotMachine extends LinearLayout {
	
	public interface OnSlotMachineMoveListener {
		public void onSlotMachineMove();
	}
	
	private OnSlotMachineMoveListener slotMachineMoveListener;
	
	public void setOnSlotMachineMoveListener(OnSlotMachineMoveListener slotMachineMoveListener) {
		this.slotMachineMoveListener = slotMachineMoveListener;
	}
	
	@ViewById(R.id.item_slotmachine_slot0)
	SlotWithControls slot0;
	
	@ViewById(R.id.item_slotmachine_slot1)
	SlotWithControls slot1;
	
	@ViewById(R.id.item_slotmachine_slot2)
	SlotWithControls slot2;
	
	public SlotMachine(Context context) {
		super(context);
	}
	
	public SlotMachine(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SlotMachine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	{
		setOrientation(LinearLayout.HORIZONTAL);
	}
	
	@AfterViews
	void setupListener() {
		OnSlotMoveListener slotMoveListener = new OnSlotMoveListener() {
			@Override
			public void onSlotMove() {
				if (slotMachineMoveListener != null) {
					slotMachineMoveListener.onSlotMachineMove();
				}
			}
		};
		slot0.setOnSlotMoveListener(slotMoveListener);
		slot1.setOnSlotMoveListener(slotMoveListener);
		slot2.setOnSlotMoveListener(slotMoveListener);
	}
	
	public boolean isAnySlotAnimating() {
		return slot0.isAnimating() || slot1.isAnimating() || slot2.isAnimating();
	}
	
	public String getPassword() {
		if (isAnySlotAnimating()) {
			return null;
		}
		return new String() //
				.concat(Integer.toHexString(slot0.getDisplayedChild())) //
				.concat(Integer.toHexString(slot1.getDisplayedChild())) //
				.concat(Integer.toHexString(slot2.getDisplayedChild()));
	}
	
	public boolean isMoved() {
		return slot0.isMoved() && slot1.isMoved() && slot2.isMoved();
	}
	
}
