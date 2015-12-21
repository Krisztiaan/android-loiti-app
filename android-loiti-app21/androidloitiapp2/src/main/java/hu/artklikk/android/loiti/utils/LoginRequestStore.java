package hu.artklikk.android.loiti.utils;

import hu.artklikk.android.loiti.backend.dto.LoginRequest;
import android.content.Context;

public class LoginRequestStore extends SharedStore<LoginRequest> {
	
	private static final String LOGIN_REQUEST_STORE = "LOGIN_REQUEST_STORE";
	
	public LoginRequestStore(Context ctx) {
		super(ctx);
	}
	
	@Override
	protected String getPreferencesName() {
		return LOGIN_REQUEST_STORE;
	}
	
	@Override
	protected Class<LoginRequest> getDataClass() {
		return LoginRequest.class;
	}
	
}
