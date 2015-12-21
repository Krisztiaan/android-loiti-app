package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.dto.Item;
import hu.artklikk.android.loiti.ui.adapter.AlacarteCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeaturedGalleryFragment extends Fragment {
	
	private static final String KEY_LIST = "KEY_LIST";
	
	private List<Item> itemList;

	private RecyclerView list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle in = null;
		if ((in = getArguments()) != null) {
			itemList = (List<Item>) in.get(KEY_LIST);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_featured_gallery, container, false);
		list = (RecyclerView) root.findViewById(R.id.fragment_featured_gallery_list);
		list.setAdapter(new AlacarteCategoryAdapter(itemList));
		return root;
	}
	
	public static FeaturedGalleryFragment newInstance(List<Item> itemList) {
		FeaturedGalleryFragment toReturn = new FeaturedGalleryFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(KEY_LIST, (ArrayList<? extends Parcelable>) itemList);
		toReturn.setArguments(bundle);
		return toReturn;
	}
	
}
