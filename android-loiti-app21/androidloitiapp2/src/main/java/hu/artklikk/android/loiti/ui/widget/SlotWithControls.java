package hu.artklikk.android.loiti.ui.widget;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.widget.Slot.OnSlotMoveListener;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class SlotWithControls extends LinearLayout {
	
	private View upButton;
	private View downButton;
	private Slot slot;
	
	public SlotWithControls(Context context) {
		super(context);
	}
	
	public SlotWithControls(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SlotWithControls(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	{
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.item_slot_with_controls, this, true);
		upButton = findViewById(R.id.item_slot_with_controls_up_button);
		downButton = findViewById(R.id.item_slot_with_controls_down_button);
		ViewStub slotStub = (ViewStub) findViewById(R.id.item_slot_with_controls_slot_stub);
		slotStub.setLayoutResource(getSlotResId());
		slot = (Slot) slotStub.inflate();
		
		upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slot.stepUp();
			}
		});
		
		downButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slot.stepDown();
			}
		});
	}
	
	protected abstract int getSlotResId();
	
	public int getDisplayedChild() {
		return slot.getDisplayedChild();
	}
	
	public boolean isAnimating() {
		return slot.isAnimating();
	}
	
	public boolean isMoved() {
		return slot.isMoved();
	}
	
	public void setOnSlotMoveListener(OnSlotMoveListener slotMoveListener) {
		slot.setOnSlotMoveListener(slotMoveListener);
	}
	
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.childrenStates = new SparseArray();
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).saveHierarchyState(ss.childrenStates);
		}
		return ss;
	}
	
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).restoreHierarchyState(ss.childrenStates);
		}
	}
	
	@Override
	protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
		dispatchFreezeSelfOnly(container);
	}
	
	@Override
	protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
		dispatchThawSelfOnly(container);
	}
	
	static class SavedState extends BaseSavedState {
		SparseArray childrenStates;
		
		SavedState(Parcelable superState) {
			super(superState);
		}
		
		private SavedState(Parcel in, ClassLoader classLoader) {
			super(in);
			childrenStates = in.readSparseArray(classLoader);
		}
		
		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeSparseArray(childrenStates);
		}
		
		public static final ClassLoaderCreator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel source, ClassLoader loader) {
				return new SavedState(source, loader);
			}
			
			@Override
			public SavedState createFromParcel(Parcel source) {
				return createFromParcel(null);
			}
			
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
	
}
