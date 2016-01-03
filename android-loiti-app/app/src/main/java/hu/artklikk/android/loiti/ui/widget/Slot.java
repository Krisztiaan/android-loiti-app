package hu.artklikk.android.loiti.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import hu.artklikk.android.loiti.R;

public class Slot extends ViewFlipper implements OnGestureListener, OnTouchListener {
	
	public interface OnSlotMoveListener {
		public void onSlotMove();
	}
	
	private OnSlotMoveListener slotMoveListener;
	
	public void setOnSlotMoveListener(OnSlotMoveListener slotMoveListener) {
		this.slotMoveListener = slotMoveListener;
	}
	
	private static final int DEFAULT_DURATION_MILLISECS = 300;
	
	private GestureDetector gestureDetector;
	private int duration;
	private int count;
	private int dampening;
	private boolean isAnimating;
	private Handler handler;
	private View firstItem;
	
	private boolean isMoved = false;
	
	public Slot(Context context) {
		super(context);
		init();
	}
	
	public Slot(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.item_slot_numbers, this, true);
		firstItem = findViewById(R.id.item_slot_numbers_0);
		firstItem.setVisibility(View.INVISIBLE);
		gestureDetector = new GestureDetector(getContext(), this);
		isAnimating = false;
		count = 0;
		duration = 0;
		handler = new Handler();
		setOnTouchListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureDetector.onTouchEvent(me);
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		return true;
	}
	
	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish, float xVelocity, float yVelocity) {
		try {
			if (isAnimating) {
				return true;
			}
			isAnimating = true;
			count = (int) Math.abs(yVelocity) / 300;
			dampening = (int) 300 / count;
			duration = dampening;
			if (yVelocity > 0) {
				// down
				handler.postDelayed(down, duration);
			}
			else {
				// up
				handler.postDelayed(up, duration);
			}
		}
		catch (ArithmeticException e) {
			// swiped too slow doesn't register
			isAnimating = false;
		}
		return true;
	}
	
	private Runnable up = new Runnable() {
		
		@Override
		public void run() {
			isMoved = true;
			notifyListenerOnMove();
			--count;
			duration += dampening;
			Animation inFromBottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			inFromBottom.setInterpolator(new AccelerateInterpolator());
			inFromBottom.setDuration(duration);
			Animation outToTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f);
			outToTop.setInterpolator(new AccelerateInterpolator());
			outToTop.setDuration(duration);
			clearAnimation();
			setInAnimation(inFromBottom);
			setOutAnimation(outToTop);
			showNext();
			if (count < 1) {
				handler.postDelayed(reenableAnimations, duration);
			}
			else {
				handler.postDelayed(this, duration);
			}
		}
		
	};
	
	private Runnable down = new Runnable() {
		
		@Override
		public void run() {
			isMoved = true;
			notifyListenerOnMove();
			--count;
			duration += dampening;
			Animation outToBottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 1.0f);
			outToBottom.setInterpolator(new AccelerateInterpolator());
			outToBottom.setDuration(duration);
			Animation inFromTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			inFromTop.setInterpolator(new AccelerateInterpolator());
			inFromTop.setDuration(duration);
			clearAnimation();
			setInAnimation(inFromTop);
			setOutAnimation(outToBottom);
			showPrevious();
			if (count < 1) {
				handler.postDelayed(reenableAnimations, duration);
			}
			else {
				handler.postDelayed(this, duration);
			}
		}
		
	};
	
	private void notifyListenerOnMove() {
		if (slotMoveListener != null) {
			slotMoveListener.onSlotMove();
		}
	}
	
	private Runnable reenableAnimations = new Runnable() {
		@Override
		public void run() {
			isAnimating = false;
		}
	};
	
	public void stepUp() {
		if (isAnimating) {
			return;
		}
		prepareOneStep();
		up.run();
	}
	
	public void stepDown() {
		if (isAnimating) {
			return;
		}
		prepareOneStep();
		down.run();
	}
	
	private void prepareOneStep() {
		isAnimating = true;
		handler.removeCallbacks(down);
		handler.removeCallbacks(up);
		clearAnimation();
		count = 1;
		dampening = 0;
		duration = DEFAULT_DURATION_MILLISECS;
	}
	
	@Override
	public void onLongPress(MotionEvent arg0) {
	}
	
	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent arg0) {
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
	
	public boolean isAnimating() {
		return isAnimating;
	}
	
	public boolean isMoved() {
		return isMoved;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			requestDisallowParentInterceptTouchEvent(v, true);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
			requestDisallowParentInterceptTouchEvent(v, false);
		}
		return false;
	}
	
	private void requestDisallowParentInterceptTouchEvent(View v, boolean disallowIntercept) {
		while (v.getParent() != null && v.getParent() instanceof View) {
			if (v.getParent() instanceof ScrollView) {
				v.getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
			}
			v = (View) v.getParent();
		}
	}
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle state = new Bundle();
		state.putParcelable(SlotState.KEY_STATE, new SlotState(super.onSaveInstanceState(), getDisplayedChild(),
				isMoved));
		return state;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			SlotState customState = (SlotState) bundle.getParcelable(SlotState.KEY_STATE);
			setDisplayedChild(customState.getDisplayedViewIndex());
			isMoved = customState.isMoved();
			if (!isMoved) {
				firstItem.setVisibility(View.INVISIBLE);
			}
			super.onRestoreInstanceState(customState.getSuperState());
			return;
		}
		super.onRestoreInstanceState(BaseSavedState.EMPTY_STATE);
	}

	protected static class SlotState extends BaseSavedState implements Parcelable {



		protected static final String KEY_STATE = "KEY_STATE";

		private final int displayedViewIndex;
		private final boolean isMoved;

		public SlotState(Parcelable superState, int displayedViewIndex, boolean isMoved) {
			super(superState);
			this.displayedViewIndex = displayedViewIndex;
			this.isMoved = isMoved;
		}

		public int getDisplayedViewIndex() {
			return displayedViewIndex;
		}

		public boolean isMoved() {
			return isMoved;
		}


		protected SlotState(Parcel in) {
            super(in);
			displayedViewIndex = in.readInt();
			isMoved = in.readByte() != 0x00;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(displayedViewIndex);
			dest.writeByte((byte) (isMoved ? 0x01 : 0x00));
		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SlotState> CREATOR = new Parcelable.Creator<SlotState>() {
			@Override
			public SlotState createFromParcel(Parcel in) {
				return new SlotState(in);
			}

			@Override
			public SlotState[] newArray(int size) {
				return new SlotState[size];
			}
		};
	}
	
}
