package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.core.TextureVideoView;
import hu.artklikk.android.loiti.ui.core.TextureVideoView.State;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainVideoFragment extends Fragment {
	
	public static final String TAG = MainVideoFragment.class.getSimpleName();
	
	private static final Uri VIDEO_URI = Uri.parse("android.resource://hu.artklikk.android.loiti/" + R.raw.teszt);
	
	private TextureVideoView video;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_main_video, container, false);
		video = (TextureVideoView) root.findViewById(R.id.fragment_main_video_view);
		final View whiteLayer = root.findViewById(R.id.fragment_main_video_white_layer);
		if (savedInstanceState == null) {
			whiteLayer.setAlpha(1f);
			whiteLayer.post(new Runnable() {
				
				@Override
				public void run() {
					whiteLayer.animate().setStartDelay(100).alpha(0.75f).setDuration(5000).start();
				}
			});
		}
		
		return root;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (video.getState() == State.STOP) {
			video.play();
		}
		else {
			video.setDataSource(getActivity(), VIDEO_URI);
			video.setLooping(true);
			video.play();
		}
	}
	
	@Override
	public void onPause() {
		video.stop();
		super.onPause();
	}
	
}
