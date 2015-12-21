package hu.artklikk.android.loiti.ui.fragment;

import com.koushikdutta.ion.Ion;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.fragment.GiftsDataFragment.GiftDescriptor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GiftsBasicFragment extends Fragment {
	
	public static final String TAG = GiftsBasicFragment.class.getSimpleName();
	
	public static GiftsBasicFragment create(GiftDescriptor descriptor) {
		GiftsBasicFragment toReturn = new GiftsBasicFragment();
		Bundle bundle = descriptor.toBundle();
		toReturn.setArguments(bundle);
		return toReturn;
	}
	
	public static GiftsBasicFragment create() {
		return new GiftsBasicFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		GiftDescriptor descriptor = null;
		
		if (getArguments() != null) {
			descriptor = new GiftDescriptor(getArguments());
		}
		
		View root = inflater.inflate(R.layout.fragment_gifts_basic, container, false);
		
		if (descriptor != null) {
			((TextView) root.findViewById(R.id.fragment_gift_basic_header)).setText(descriptor.getBadgeName());
			((TextView) root.findViewById(R.id.fragment_gift_basic_message)).setText(descriptor.getActionText());
			((TextView) root.findViewById(R.id.fragment_gift_basic_visit_count)).setText(descriptor.getVisitCount());
			Ion.with((ImageView) root.findViewById(R.id.fragment_gift_basic_thumbnail)).load(descriptor.getThumbnail());
		}
		else {
			((TextView) root.findViewById(R.id.fragment_gift_basic_visit_count)).setText("0");
		}
		
		return root;
	}
	
}
