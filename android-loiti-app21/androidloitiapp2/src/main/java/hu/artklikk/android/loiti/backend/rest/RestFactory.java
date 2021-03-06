package hu.artklikk.android.loiti.backend.rest;

import hu.artklikk.android.loiti.backend.rest.core.RestBase;

public class RestFactory {

	
	private static StatefulRest statefullRest = new StatefulRest();
	
	private static NonStatefullRest nonStatefullRest = new NonStatefullRest();
	
	public static RestBase getStatefullRest() {
		return statefullRest;
	}
	
	public static RestBase getSimpleRest() {
		return nonStatefullRest;
	}
}
