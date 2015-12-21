package hu.artklikk.android.loiti.ui.adapter;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.adapter.TermsConditionsAdapter.TermsViewHolder;

import java.util.List;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TermsConditionsAdapter extends Adapter<TermsViewHolder> {
	
	public static class TermsViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
		
		TextView header;
		TextView description;
		
		public TermsViewHolder(View itemView) {
			super(itemView);
			header = (TextView) itemView.findViewById(R.id.item_terms_conditions_header);
			description = (TextView) itemView.findViewById(R.id.item_terms_conditions_description);
		}
		
	}
	
	public static class TermsConditionsItem {
		private String header;
		private String description;
		public TermsConditionsItem(String header, String description) {
			this.header = header;
			this.description = description;
		}
		public String getHeader() {
			return header;
		}
		public String getDescription() {
			return description;
		}
		
	}
	
	private List<TermsConditionsItem> termsList;
	
	public TermsConditionsAdapter(List<TermsConditionsItem> termsList) {
		this.termsList = termsList;
	}
	
	@Override
	public int getItemCount() {
		return termsList.size();
	}
	
	@Override
	public void onBindViewHolder(TermsViewHolder holder, int position) {
		holder.header.setText(termsList.get(position).getHeader());
		holder.description.setText(termsList.get(position).getDescription());
	}
	
	@Override
	public TermsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new TermsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_terms_conditions, parent, false));
	}
	
}
