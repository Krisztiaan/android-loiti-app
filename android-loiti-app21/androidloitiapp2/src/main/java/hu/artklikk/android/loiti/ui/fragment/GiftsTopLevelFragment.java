package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.fragment.GiftsDataFragment.GiftDescriptor;

import com.koushikdutta.ion.Ion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GiftsTopLevelFragment extends Fragment {
	public static final String TAG = GiftsTopLevelFragment.class.getSimpleName();
	
	public static GiftsTopLevelFragment create(GiftDescriptor descriptor) {
		GiftsTopLevelFragment toReturn = new GiftsTopLevelFragment();
		Bundle bundle = descriptor.toBundle();
		toReturn.setArguments(bundle);
		return toReturn;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		GiftDescriptor descriptor = new GiftDescriptor(getArguments());
		
		View root = inflater.inflate(R.layout.fragment_gifts_toplevel, container, false);
		
		((TextView) root.findViewById(R.id.fragment_gift_toplevel_header)).setText(descriptor.getBadgeName());
		((TextView) root.findViewById(R.id.fragment_gift_toplevel_description)).setText(descriptor.getDescription());
		Ion.with((ImageView) root.findViewById(R.id.fragment_gift_toplevel_thumbnail)).load(descriptor.getThumbnail());
		
		return root;
	}
}
