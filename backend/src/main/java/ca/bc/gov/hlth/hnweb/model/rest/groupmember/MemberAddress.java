package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

public class MemberAddress {
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String addressLine4;
	private String postalCode;

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getAddressLine4() {
		return addressLine4;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Override
	public String toString() {
		return "MemberAddress [addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", addressLine3=" + addressLine3
				+ ", addressLine4=" + addressLine4 + ", postalCode=" + postalCode + "]";
	}

}
