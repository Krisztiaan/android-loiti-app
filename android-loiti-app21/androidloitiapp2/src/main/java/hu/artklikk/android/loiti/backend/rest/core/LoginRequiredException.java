package hu.artklikk.android.loiti.backend.rest.core;

import hu.artklikk.android.loiti.backend.dto.ErrorInfo;

public class LoginRequiredException extends HttpExceptionWithInfo {

	private static final long serialVersionUID = 1L;

	public LoginRequiredException(ErrorInfo info) {
		super(401, info, "HTTP Status 401: Login required!\n"+(info != null ? info.message : ""));
	}
}
