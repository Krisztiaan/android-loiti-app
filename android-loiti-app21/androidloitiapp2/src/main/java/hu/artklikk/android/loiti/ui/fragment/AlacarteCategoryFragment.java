package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.backend.dto.Item;
import hu.artklikk.android.loiti.backend.dto.ItemCategory;
import hu.artklikk.android.loiti.ui.adapter.AlacarteCategoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.koushikdutta.ion.Ion;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AlacarteCategoryFragment extends Fragment {
	
	private static final String KEY_CATEGORY = "KEY_CATEGORY";
	private static final String KEY_LIST = "KEY_LIST";
	
	private ItemCategory category;
	private List<Item> itemList;
	
	private TextView headerText;
	private ImageView headerImage;
	private RecyclerView list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle in = null;
		if ((in = getArguments()) != null) {
			category = in.getParcelable(KEY_CATEGORY);
			itemList = (List<Item>) in.get(KEY_LIST);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_alacarte_category, container, false);
		headerText = (TextView) root.findViewById(R.id.fragment_alacarte_category_header_name);
		headerText.setText(category.name.toUpperCase(Locale.ENGLISH));
		headerImage = (ImageView) root.findViewById(R.id.fragment_alacarte_category_header_thumbnail);
		Ion.with(headerImage).load(category.imgProfile);
		list = (RecyclerView) root.findViewById(R.id.fragment_alacarte_category_list);
		list.setAdapter(new AlacarteCategoryAdapter(itemList));
		return root;
	}
	
	public static AlacarteCategoryFragment newInstance(List<Item> itemList, ItemCategory category) {
		AlacarteCategoryFragment toReturn = new AlacarteCategoryFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_CATEGORY, category);
		bundle.putParcelableArrayList(KEY_LIST, (ArrayList<? extends Parcelable>) itemList);
		toReturn.setArguments(bundle);
		return toReturn;
	}
	
}
