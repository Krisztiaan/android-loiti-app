package hu.artklikk.android.loiti.ui.activity;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.adapter.TermsConditionsAdapter;
import hu.artklikk.android.loiti.ui.adapter.TermsConditionsAdapter.TermsConditionsItem;
import hu.artklikk.android.loiti.ui.core.MenuActivity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class TermsConditionsActivity extends MenuActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_terms_conditions);
		
		RecyclerView list = (RecyclerView) findViewById(R.id.activity_terms_conditions_list);
		
		String[] headers = getResources().getStringArray(R.array.terms_conditions_headers);
		String[] descriptions = getResources().getStringArray(R.array.terms_conditions_descriptions);
		
		int min = headers.length < descriptions.length ? headers.length : descriptions.length;
		
		List<TermsConditionsItem> termsList = new ArrayList<TermsConditionsItem>(min);
		
		for (int i = 0; i < min; i++) {
			termsList.add(new TermsConditionsItem(headers[i], descriptions[i]));
		}
		
		list.setAdapter(new TermsConditionsAdapter(termsList));
	}
	
}
