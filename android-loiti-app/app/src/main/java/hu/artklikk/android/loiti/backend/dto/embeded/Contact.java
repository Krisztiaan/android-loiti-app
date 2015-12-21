package hu.artklikk.android.loiti.backend.dto.embeded;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonInclude(Include.NON_EMPTY)
	private List<String> email = new ArrayList<String>(3);
	@JsonInclude(Include.NON_EMPTY)
	private List<String> phone = new ArrayList<String>(3);

	public Contact() {
	}

	public void addEmail(String email) {
		if(email == null || "".equals(email)) return;
		this.email.add(email);
	}

	public void addPhone(String phone) {
		if(phone == null  || "".equals(phone)) return;
		this.phone.add(phone);
	}

	public List<String> getEmail() {
		return email;
	}

	public List<String> getPhone() {
		return phone;
	}
}
