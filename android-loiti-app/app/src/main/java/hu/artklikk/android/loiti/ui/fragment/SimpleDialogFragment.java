package hu.artklikk.android.loiti.ui.fragment;

import hu.artklikk.android.loiti.R;
import hu.artklikk.android.loiti.ui.core.BackPressAware;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleDialogFragment extends Fragment implements BackPressAware {
	
	public static final String TAG = SimpleDialogFragment.class.getSimpleName();
	
	private static final String KEY_DESCRIPTOR = "KEY_DESCRIPTOR";
	
	public interface SimpleDialogListener {
		public void onAgree(SimpleDialogFragment caller);
		
		public void onCancel(SimpleDialogFragment caller);
	}
	
	public static class DialogDescriptor implements Parcelable {
		
		private static final int INVALID_VALUE = -1;
		
		private int messageId = INVALID_VALUE;
		private String customMessage;
		private int okStringId = INVALID_VALUE;
		private String customOkString;
		private boolean isCancelVisible = false;
		
		public DialogDescriptor() {
		}
		
		public DialogDescriptor(Parcel in) {
			messageId = in.readInt();
			customMessage = in.readString();
			okStringId = in.readInt();
			customOkString = in.readString();
			isCancelVisible = in.readInt() != 0;
		}
		
		public DialogDescriptor setMessageId(int messageId) {
			this.messageId = messageId;
			return this;
		}
		
		public DialogDescriptor setCustomMessage(String customMessage) {
			this.customMessage = customMessage;
			return this;
		}
		
		public DialogDescriptor setOkStringId(int okStringId) {
			this.okStringId = okStringId;
			return this;
		}
		
		public DialogDescriptor setCustomOkString(String customOkString) {
			this.customOkString = customOkString;
			return this;
		}
		
		public DialogDescriptor setCancelVisible(boolean isCancelVisible) {
			this.isCancelVisible = isCancelVisible;
			return this;
		}
		
		public boolean hasMessageId() {
			return messageId != INVALID_VALUE;
		}
		
		public int getMessageId() {
			return messageId;
		}
		
		public boolean hasCustomMessage() {
			return customMessage != null;
		}
		
		public String getCustomMessage() {
			return customMessage;
		}
		
		public boolean hasOkStringId() {
			return okStringId != INVALID_VALUE;
		}
		
		public int getOkStringId() {
			return okStringId;
		}
		
		public boolean hasCustomOkString() {
			return customOkString != null;
		}
		
		public String getCustomOkString() {
			return customOkString;
		}
		
		public boolean isCancelVisible() {
			return isCancelVisible;
		}
		
		@Override
		public int describeContents() {
			return 0;
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(messageId);
			dest.writeString(customMessage);
			dest.writeInt(okStringId);
			dest.writeString(customOkString);
			dest.writeInt(isCancelVisible ? 1 : 0);
		}
		
		public static final Parcelable.Creator<DialogDescriptor> CREATOR = new Parcelable.Creator<DialogDescriptor>() {
			public DialogDescriptor createFromParcel(Parcel in) {
				return new DialogDescriptor(in);
			}
			
			public DialogDescriptor[] newArray(int size) {
				return new DialogDescriptor[size];
			}
		};
		
	}
	
	public static SimpleDialogFragment create(int messageId) {
		return createBase(new DialogDescriptor().setMessageId(messageId).setOkStringId(R.string.dialog_ok));
	}
	
	public static SimpleDialogFragment create(String customMessage) {
		return createBase(new DialogDescriptor().setCustomMessage(customMessage).setOkStringId(R.string.dialog_ok));
	}
	
	public static SimpleDialogFragment createWithCancel(int messageId) {
		return createBase(new DialogDescriptor().setMessageId(messageId).setOkStringId(R.string.dialog_ok)
				.setCancelVisible(true));
	}
	
	public static SimpleDialogFragment createWithCancel(String customMessage) {
		return createBase(new DialogDescriptor().setCustomMessage(customMessage).setOkStringId(R.string.dialog_ok)
				.setCancelVisible(true));
	}
	
	public static SimpleDialogFragment create(DialogDescriptor descriptor) {
		return createBase(descriptor);
	}
	
	private static SimpleDialogFragment createBase(DialogDescriptor descriptor) {
		SimpleDialogFragment toReturn = new SimpleDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_DESCRIPTOR, descriptor);
		toReturn.setArguments(bundle);
		return toReturn;
	}
	
	private View outerArea;
	private TextView okButton;
	private View cancelButton;
	private TextView message;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.dialog, container, false);
		outerArea = root.findViewById(R.id.dialog_out_area);
		okButton = (TextView) root.findViewById(R.id.dialog_ok);
		cancelButton = root.findViewById(R.id.dialog_cancel);
		message = (TextView) root.findViewById(R.id.dialog_message);
		
		DialogDescriptor descriptor = getArguments().getParcelable(KEY_DESCRIPTOR);
		
		if (descriptor.hasMessageId()) {
			message.setText(descriptor.getMessageId());
		}
		else if (descriptor.hasCustomMessage()) {
			message.setText(descriptor.getCustomMessage());
		}
		
		if (descriptor.hasOkStringId()) {
			okButton.setText(descriptor.getOkStringId());
		}
		else if (descriptor.hasCustomOkString()) {
			okButton.setText(descriptor.getCustomOkString());
		}
		
		if (descriptor.isCancelVisible()) {
			cancelButton.setVisibility(View.VISIBLE);
		}
		
		outerArea.setOnClickListener(cancelClickListener);
		cancelButton.setOnClickListener(cancelClickListener);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getActivity() instanceof SimpleDialogListener) {
					((SimpleDialogListener) getActivity()).onAgree(SimpleDialogFragment.this);
				}
			}
		});
		
		return root;
	}
	
	private OnClickListener cancelClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			sendCancel();
		}
	};
	
	@Override
	public void onBackPressed() {
		sendCancel();
	}
	
	private void sendCancel() {
		if (getActivity() instanceof SimpleDialogListener) {
			((SimpleDialogListener) getActivity()).onCancel(this);
		}
	}
	
}
