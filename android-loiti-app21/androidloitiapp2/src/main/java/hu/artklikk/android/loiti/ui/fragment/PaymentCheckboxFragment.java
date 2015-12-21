package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.core.BackPressAware;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class PaymentCheckboxFragment extends Fragment implements BackPressAware {

	public interface CheckboxFragmentListener {
		public void onCheckboxAgreed(Fragment caller, boolean isChecked);

		public void onCheckboxCancelled(Fragment caller);
	}

	public static final String TAG = PaymentCheckboxFragment.class
			.getSimpleName();

	private static final String KEY_IS_CHECKED = "KEY_IS_CHECKED";

	private ViewGroup outerArea;
	private TextView okButton;
	private View checkbox;

	public static PaymentCheckboxFragment create() {
		PaymentCheckboxFragment toReturn = new PaymentCheckboxFragment();
		Bundle bundle = new Bundle();
		toReturn.setArguments(bundle);
		return toReturn;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater
				.inflate(R.layout.dialog_checkbox, container, false);
		outerArea = (ViewGroup) root
				.findViewById(R.id.dialog_checkbox_out_area);
		okButton = (TextView) root.findViewById(R.id.dialog_checkbox_ok_button);
		checkbox = root.findViewById(R.id.dialog_checkbox_checkbox);

		outerArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCancel();
			}
		});
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActivity() instanceof CheckboxFragmentListener) {
					((CheckboxFragmentListener) getActivity())
							.onCheckboxAgreed(PaymentCheckboxFragment.this,
									checkbox.isSelected());
				}
			}
		});
		checkbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkbox.setSelected(!checkbox.isSelected());
			}
		});
		if (savedInstanceState != null) {
			checkbox.setSelected(savedInstanceState.getBoolean(KEY_IS_CHECKED));
		}
		return root;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_IS_CHECKED, checkbox.isSelected());
	}

	@Override
	public void onBackPressed() {
		sendCancel();
	}

	private void sendCancel() {
		if (getActivity() instanceof CheckboxFragmentListener) {
			((CheckboxFragmentListener) getActivity())
					.onCheckboxCancelled(this);
		}
	}

}
