package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.FeedbackEndpoint;
import hu.artklikk.android.loiti.backend.IntentEndpoint;
import hu.artklikk.android.loiti.backend.dto.VenueRating;
import hu.artklikk.android.loiti.backend.dto.enums.PaymentMethod;
import hu.artklikk.android.loiti.backend.dto.intent.PurchaseIntent;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.ui.core.BackPressAware;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.PaymentCheckboxFragment;
import hu.artklikk.android.loiti.ui.fragment.PaymentCheckboxFragment.CheckboxFragmentListener;
import hu.artklikk.android.loiti.ui.fragment.PaymentReviewFragment;
import hu.artklikk.android.loiti.ui.fragment.PaymentReviewFragment.ReviewFragmentListener;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

public class PaymentActivity extends MenuActivity implements
		CheckboxFragmentListener, ReviewFragmentListener {

	private static final String KEY_NO_BILL_SELECTED = "KEY_NO_BILL_SELECTED";
	private static final String KEY_BILL_SELECTED = "KEY_BILL_SELECTED";
	private static final String KEY_PAYMENT_TYPE = "KEY_PAYMENT_TYPE";

	private View noBillItem;
	private View billItem;
	private View cashItem;
	private View cardItem;

	private PaymentMethod paymentType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		final View root = findViewById(R.id.activity_payment_root);
		noBillItem = findViewById(R.id.activity_payment_item_no_bill);
		billItem = findViewById(R.id.activity_payment_item_bill);
		cashItem = findViewById(R.id.activity_payment_item_cash);
		cardItem = findViewById(R.id.activity_payment_item_card);

		if (savedInstanceState != null) {
			noBillItem.setSelected(savedInstanceState
					.getBoolean(KEY_NO_BILL_SELECTED));
			billItem.setSelected(savedInstanceState
					.getBoolean(KEY_BILL_SELECTED));
			paymentType = (PaymentMethod) savedInstanceState
					.getSerializable(KEY_PAYMENT_TYPE);
		}

		noBillItem.setOnClickListener(selectionListener);
		billItem.setOnClickListener(selectionListener);

		cashItem.setOnClickListener(paymentListener);
		cardItem.setOnClickListener(paymentListener);

		if (root != null) {

			root.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@SuppressWarnings("deprecation")
						@SuppressLint("NewApi")
						@Override
						public void onGlobalLayout() {
							int height = root.getMeasuredHeight();
							int itemHeight = height / 4;
							noBillItem
									.setLayoutParams(new LinearLayout.LayoutParams(
											LinearLayout.LayoutParams.MATCH_PARENT,
											itemHeight));
							billItem.setLayoutParams(new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									itemHeight));
							cashItem.setLayoutParams(new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									itemHeight));
							cardItem.setLayoutParams(new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									itemHeight));
							if (Build.VERSION.SDK_INT < 16) {
								root.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								root.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}
						}

					});

		}

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean(KEY_NO_BILL_SELECTED,
				noBillItem.isSelected());
		savedInstanceState.putBoolean(KEY_BILL_SELECTED, billItem.isSelected());
		savedInstanceState.putSerializable(KEY_PAYMENT_TYPE, paymentType);
	}

	private OnClickListener selectionListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			noBillItem.setSelected(noBillItem == v);
			billItem.setSelected(billItem == v);
		}
	};

	private OnClickListener paymentListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == cashItem) {
				paymentType = PaymentMethod.CASH;
			} else if (v == cardItem) {
				paymentType = PaymentMethod.CARD;
			} else {
				throw new IllegalStateException(
						"Something is seriously fucked up in PaymentActivity!");
			}

			if (!noBillItem.isSelected() && !billItem.isSelected()) {
				showCheckboxDialog();
			} else {
				if (billItem.isSelected()) {
					startActivity(new Intent(PaymentActivity.this,
							BillActivity.class).putExtra(
							BillActivity.KEY_PAYMENT_TYPE, paymentType));
				} else {
					payment(paymentType, new RestCallFinishListener() {

						@Override
						public void onFinish(Object result) {
							showReviewDialog();
						}

						@Override
						public void onException(Exception e) {
							e.printStackTrace();
						}
					});
				}
			}
		}
	};

	private void payment(PaymentMethod paymentType,
			RestCallFinishListener callback) {
		PurchaseIntent purchaseIntent = new PurchaseIntent();
		purchaseIntent.paymentMethod = paymentType;
		purchaseIntent.tableId = LoitiApplication.getLastTableId();
		purchaseIntent.time = new Date();
		purchaseIntent.userId = LoitiApplication.getUserId();
		purchaseIntent.venueId = LoitiApplication.getVenueId();

		IntentEndpoint.payment(purchaseIntent, callback);
	}

	private void showCheckboxDialog() {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.activity_payment_fragment_container,
						PaymentCheckboxFragment.create(),
						PaymentCheckboxFragment.TAG).addToBackStack(null)
				.commit();
	}

	private void showReviewDialog() {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.activity_payment_fragment_container,
						PaymentReviewFragment.create(),
						PaymentReviewFragment.TAG).addToBackStack(null)
				.commit();
	}

	@Override
	public void onCheckboxAgreed(Fragment caller, boolean isChecked) {

		getSupportFragmentManager().popBackStack();

		if (isChecked) {
			billItem.setSelected(true);
			startActivity(new Intent(PaymentActivity.this, BillActivity.class)
					.putExtra(BillActivity.KEY_PAYMENT_TYPE, paymentType));

		} else {
			noBillItem.setSelected(true);
			payment(paymentType, new RestCallFinishListener() {

				@Override
				public void onFinish(Object result) {
					showReviewDialog();
				}

				@Override
				public void onException(Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	@Override
	public void onCheckboxCancelled(Fragment caller) {
		getSupportFragmentManager().popBackStack();
	}

	@Override
	public void onReviewAgreed(final Fragment caller, int score) {
		VenueRating rating = new VenueRating();
		rating.score = score;
		rating.time = new Date();
		rating.userId = LoitiApplication.getUserId();
		rating.venueId = LoitiApplication.getVenueId();
		rating.venueTableId = LoitiApplication.getLastTableId();
		if (rating.venueTableId == null) {
			getSupportFragmentManager().popBackStack();
			finish();
		} else {
			FeedbackEndpoint.rate(rating, new RestCallFinishListener() {

				@Override
				public void onFinish(Object result) {
					getSupportFragmentManager().popBackStack();
					finish();
				}

				@Override
				public void onException(Exception e) {
					getSupportFragmentManager().popBackStack();
					finish();
				}
			});
		}
	}

	@Override
	public void onReviewCancelled(Fragment caller) {
		getSupportFragmentManager().popBackStack();
		finish();
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			super.onBackPressed();
		} else {
			Fragment fragment = getSupportFragmentManager().findFragmentById(
					R.id.activity_payment_fragment_container);
			if (fragment instanceof BackPressAware) {
				((BackPressAware) fragment).onBackPressed();
			} else {
				super.onBackPressed();
			}
		}

	}

}
