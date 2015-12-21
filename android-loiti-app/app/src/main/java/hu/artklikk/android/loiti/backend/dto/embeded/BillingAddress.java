package hu.artklikk.android.loiti.backend.dto.embeded;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BillingAddress extends Address {

	private static final long serialVersionUID = 1L;

	private String name;
	@JsonInclude(Include.NON_NULL)
	private String taxId;

	public BillingAddress() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if("".equals(name)) name = null;
		this.name = name;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		if("".equals(taxId)) taxId = null;
		this.taxId = taxId;
	}
}
