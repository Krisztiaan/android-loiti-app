package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.LoitiApplication;
import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.FeedbackEndpoint;
import hu.artklikk.android.loiti.backend.dto.FeedbackPost;
import hu.artklikk.android.loiti.backend.dto.enums.FeedbackPostType;
import hu.artklikk.android.loiti.backend.rest.core.RestCallFinishListener;
import hu.artklikk.android.loiti.ui.adapter.NothingSelectedSpinnerAdapter;
import hu.artklikk.android.loiti.ui.core.MenuActivity;

import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class VisitorsBookActivity extends MenuActivity {
	
	private Spinner spinner;
	private EditText input;
	private View send;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visitors_book);
		spinner = (Spinner) findViewById(R.id.activity_visitors_book_spinner);
		input = (EditText) findViewById(R.id.activity_visitors_book_input);
		send = findViewById(R.id.activity_visitors_book_send_container);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.visitors_book_spinner_items,
				R.layout.item_visitors_book_spinner);
		adapter.setDropDownViewResource(R.layout.item_visitors_book_spinner);
		spinner.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.item_visitors_book_spinner_prompt, this));
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (spinner.getSelectedItemPosition() == 0) {
					showSimpleDialog(R.string.popup_visitors_book_category_required);
					return;
				}
				if (input.getText().toString().trim().isEmpty()) {
					showSimpleDialog(R.string.popup_visitors_book_input_required);
					return;
				}
				FeedbackPost feedback = new FeedbackPost();
				feedback.postText = input.getText().toString().trim();
				feedback.postType = FeedbackPostType.fromIndex(spinner.getSelectedItemPosition());
				feedback.time = new Date();
				feedback.userId = LoitiApplication.getUserId();
				feedback.venueId = LoitiApplication.getVenueId();
				FeedbackEndpoint.feedback(feedback, new RestCallFinishListener() {
					
					@Override
					public void onFinish(Object result) {
						startActivity(MainActivity.createGoBackIntent(VisitorsBookActivity.this));
					}
					
					@Override
					public void onException(Exception e) {
						e.printStackTrace();
						showSimpleDialog(R.string.popup_visitors_book_fail);
						return;
					}
				});
			}
		});
		
	}
	
}
