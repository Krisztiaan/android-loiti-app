package hu.artklikk.android.loiti.backend.rest.core;

public interface RestCallFinishListener {

	public void onFinish(Object result);
	
	public void onException(Exception e);
}
