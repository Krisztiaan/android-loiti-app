package hu.artklikk.android.loiti.backend.rest.core;

interface OnRestExecutorFinished {
	
	public void onFinish(RestExecuteTask task, Object result);
	
	public void onException(RestExecuteTask task, Exception e);
}
