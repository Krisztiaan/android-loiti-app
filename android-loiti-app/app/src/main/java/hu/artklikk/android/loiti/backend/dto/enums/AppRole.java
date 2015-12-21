package hu.artklikk.android.loiti.backend.dto.enums;

public enum AppRole {
	ADMIN (1),
	NEW_USER (2),
	USER (3),
	EMPLOYEE (4),
	HOST (5);

	private int bit;

	AppRole(int bit) {
		this.bit = bit;
	}

}
