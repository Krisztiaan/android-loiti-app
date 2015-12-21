package hu.artklikk.android.loiti.backend.rest.core;

import hu.artklikk.android.loiti.backend.dto.ErrorInfo;

import org.apache.http.HttpException;

public class HttpExceptionWithInfo extends HttpException {

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
