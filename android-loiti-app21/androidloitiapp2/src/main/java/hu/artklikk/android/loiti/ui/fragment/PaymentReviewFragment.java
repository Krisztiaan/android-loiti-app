package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.core.BackPressAware;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

public class PaymentReviewFragment extends Fragment implements BackPressAware {

	public interface ReviewFragmentListener {
		public void onReviewAgreed(Fragment caller, int score);

		public void onReviewCancelled(Fragment caller);
	}

	public static final String TAG = PaymentReviewFragment.class
			.getSimpleName();

	private static final String KEY_DISPLAYED_CHILD = "KEY_DISPLAYED_CHILD";

	private View outerArea;
	private View okButton;
	private View cancelButton;
	private ViewFlipper slot;
	private View leftArrow;
	private View rightArrow;

	public static PaymentReviewFragment create() {
		PaymentReviewFragment toReturn = new PaymentReviewFragment();
		Bundle bundle = new Bundle();
		toReturn.setArguments(bundle);
		return toReturn;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.dialog_review, container, false);
		outerArea = root.findViewById(R.id.dialog_review_out_area);
		okButton = root.findViewById(R.id.dialog_review_ok_button);
		cancelButton = root.findViewById(R.id.dialog_review_cancel_button);
		slot = (ViewFlipper) root.findViewById(R.id.dialog_review_content_slot);
		leftArrow = root.findViewById(R.id.dialog_review_content_left_arrow);
		rightArrow = root.findViewById(R.id.dialog_review_content_right_arrow);

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(KEY_DISPLAYED_CHILD)) {
			slot.setDisplayedChild(savedInstanceState
					.getInt(KEY_DISPLAYED_CHILD));
		} else {
			slot.setDisplayedChild(slot.getChildCount() - 1);
		}

		outerArea.setOnClickListener(cancelClickListener);
		cancelButton.setOnClickListener(cancelClickListener);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActivity() instanceof ReviewFragmentListener) {
					((ReviewFragmentListener) getActivity())
							.onReviewAgreed(PaymentReviewFragment.this, slot.getDisplayedChild() + 1);
				}
			}
		});
		leftArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slot.showPrevious();
			}
		});
		rightArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slot.showNext();
			}
		});
		return root;
	}

	private OnClickListener cancelClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sendCancel();
		}
	};
	
	private void sendCancel() {
		if (getActivity() instanceof ReviewFragmentListener) {
			((ReviewFragmentListener) getActivity()).onReviewCancelled(PaymentReviewFragment.this);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_DISPLAYED_CHILD, slot.getDisplayedChild());
	}

	@Override
	public void onBackPressed() {
		sendCancel();
	}

}
