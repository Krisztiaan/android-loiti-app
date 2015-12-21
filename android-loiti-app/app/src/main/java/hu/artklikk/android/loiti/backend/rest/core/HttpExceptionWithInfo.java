package hu.artklikk.android.loiti.backend.rest.core;

import java.io.IOException;

import hu.artklikk.android.loiti.backend.dto.ErrorInfo;

public class HttpExceptionWithInfo extends IOException {

	private static final long serialVersionUID = 1L;
	
	private int status;
	private ErrorInfo errorInfo;

	public HttpExceptionWithInfo(int status, ErrorInfo info, String msg) {
		super(msg);
		
		this.status = status;
		this.errorInfo = info;
	}

	public int getStatus() {
		return status;
	}

	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}
}
