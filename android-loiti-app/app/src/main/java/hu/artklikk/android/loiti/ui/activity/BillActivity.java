package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.FeedbackEndpoint;
import hu.artklikk.android.loiti.backend.IntentEndpoint;
import hu.artklikk.android.loiti.backend.dto.VenueRating;
import hu.artklikk.android.loiti.backend.dto.embeded.BillingAddress;
import hu.artklikk.android.loiti.backend.dto.enums.PaymentMethod;
import hu.artklikk.android.loiti.backend.dto.intent.PurchaseIntent;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.ui.core.MenuActivity;
import hu.artklikk.android.loiti.ui.fragment.PaymentReviewFragment;
import hu.artklikk.android.loiti.ui.fragment.PaymentReviewFragment.ReviewFragmentListener;

import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class BillActivity extends MenuActivity implements ReviewFragmentListener {
	
	public static final String KEY_PAYMENT_TYPE = "KEY_PAYMENT_TYPE";
	
	private PaymentMethod paymentType;
	
	private View previousContainer;
	private TextView previousName;
	private View previousApply;
	private View previousDelete;
	
	private EditText name;
	private EditText taxNumber;
	private EditText postalCode;
	private EditText city;
	private EditText address;
	
	private View send;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);
		
		paymentType = (PaymentMethod) getIntent().getSerializableExtra(KEY_PAYMENT_TYPE);
		
		final BillingData billingData = LoitiApplication.getBillingData();
		
		previousContainer = findViewById(R.id.activity_bill_previous_container);
		previousName = (TextView) findViewById(R.id.activity_bill_previous_name);
		previousApply = findViewById(R.id.activity_bill_previous_apply);
		previousDelete = findViewById(R.id.activity_bill_previous_delete);
		
		name = (EditText) findViewById(R.id.activity_bill_name);
		taxNumber = (EditText) findViewById(R.id.activity_bill_tax_number);
		postalCode = (EditText) findViewById(R.id.activity_bill_postal_code);
		city = (EditText) findViewById(R.id.activity_bill_city);
		address = (EditText) findViewById(R.id.activity_bill_address);
		
		send = findViewById(R.id.activity_bill_send);
		
		if (billingData == null) {
			previousContainer.setVisibility(View.GONE);
		}
		else {
			previousName.setText(billingData.getName());
			previousApply.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					name.setText(billingData.getName());
					taxNumber.setText(billingData.getTaxNumber());
					postalCode.setText(billingData.getPostalCode());
					city.setText(billingData.getCity());
					address.setText(billingData.getAddress());
				}
			});
			previousDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					LoitiApplication.saveBillingData(new BillingData());
					previousContainer.setVisibility(View.GONE);
				}
			});
		}
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isEmpty(name) || isEmpty(taxNumber) || isEmpty(postalCode) || isEmpty(city) || isEmpty(address)) {
					showSimpleDialog(R.string.popup_bill_data_required);
				}
				else {
					if (LoitiApplication.getBillingData() == null) {
						
						BillingData newBillingData = new BillingData( //
								name.getText().toString().trim(), //
								taxNumber.getText().toString().trim(), //
								postalCode.getText().toString().trim(), //
								city.getText().toString().trim(), //
								address.getText().toString().trim());
						
						LoitiApplication.saveBillingData(newBillingData);
					}
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
		});
	}
	
	private boolean isEmpty(final EditText editText) {
		return editText.getText().toString().trim().isEmpty();
	}
	
	private void payment(PaymentMethod paymentType, RestCallFinishListener callback) {
		PurchaseIntent purchaseIntent = new PurchaseIntent();
		purchaseIntent.paymentMethod = paymentType;
		purchaseIntent.tableId = LoitiApplication.getLastTableId();
		purchaseIntent.time = new Date();
		purchaseIntent.userId = LoitiApplication.getUserId();
		purchaseIntent.venueId = LoitiApplication.getVenueId();
		
		BillingAddress address = new BillingAddress();
		address.setName(this.name.getText().toString().trim());
		address.setFloor(this.taxNumber.getText().toString().trim());
		address.setPostalCode(this.postalCode.getText().toString().trim());
		address.setCity(this.city.getText().toString().trim());
		address.setStreet(this.address.getText().toString().trim());
		
		purchaseIntent.billingAddress = address;
		
		IntentEndpoint.payment(purchaseIntent, callback);
	}
	
	private void showReviewDialog() {
		getSupportFragmentManager().beginTransaction()
				.add(R.id.activity_bill_root, PaymentReviewFragment.create(), PaymentReviewFragment.TAG)
				.addToBackStack(null).commit();
	}
	
	@Override
	public void onReviewAgreed(Fragment caller, int score) {
		VenueRating rating = new VenueRating();
		rating.score = score;
		rating.time = new Date();
		rating.userId = LoitiApplication.getUserId();
		rating.venueId = LoitiApplication.getVenueId();
		rating.venueTableId = LoitiApplication.getLastTableId();
		if (rating.venueTableId == null) {
			getSupportFragmentManager().popBackStack();
			startActivity(MainActivity.createGoBackIntent(BillActivity.this));
		}
		else {
			FeedbackEndpoint.rate(rating, new RestCallFinishListener() {
				
				@Override
				public void onFinish(Object result) {
					getSupportFragmentManager().popBackStack();
					startActivity(MainActivity.createGoBackIntent(BillActivity.this));
				}
				
				@Override
				public void onException(Exception e) {
					getSupportFragmentManager().popBackStack();
					startActivity(MainActivity.createGoBackIntent(BillActivity.this));
				}
			});
		}
	}
	
	@Override
	public void onReviewCancelled(Fragment caller) {
		getSupportFragmentManager().popBackStack();
		startActivity(MainActivity.createGoBackIntent(BillActivity.this));
	}
	
	public static class BillingData {
		
		public static final String KEY_BILLDATA_NAME = "KEY_BILLDATA_NAME";
		public static final String KEY_BILLDATA_TAXNUMBER = "KEY_BILLDATA_TAXNUMBER";
		public static final String KEY_BILLDATA_POSTALCODE = "KEY_BILLDATA_POSTALCODE";
		public static final String KEY_BILLDATA_CITY = "KEY_BILLDATA_CITY";
		public static final String KEY_BILLDATA_ADDRESS = "KEY_BILLDATA_ADDRESS";
		
		private String name;
		private String taxNumber;
		private String postalCode;
		private String city;
		private String address;
		
		public BillingData() {
		}
		
		public BillingData(String name, String taxNumber, String postalCode, String city, String address) {
			this.name = name;
			this.taxNumber = taxNumber;
			this.postalCode = postalCode;
			this.city = city;
			this.address = address;
		}
		
		public String getName() {
			return name;
		}
		
		public String getTaxNumber() {
			return taxNumber;
		}
		
		public String getPostalCode() {
			return postalCode;
		}
		
		public String getCity() {
			return city;
		}
		
		public String getAddress() {
			return address;
		}
		
	}
	
}
