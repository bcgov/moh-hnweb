package ca.bc.gov.hlth.hnweb.model.rest.pbf;

public class UserPayeeMappingRequest {

	private String userGuid;

	private String payeeNumber;

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public String getPayeeNumber() {
		return payeeNumber;
	}

	public void setPayeeNumber(String payeeNumber) {
		this.payeeNumber = payeeNumber;
	}

	@Override
	public String toString() {
		return "UserPayeeMappingRequest [userGuid=" + userGuid + ", payeeNumber=" + payeeNumber + "]";
	}

}