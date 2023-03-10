package ca.bc.gov.hlth.hnweb.model.rest.pbf;

public class UserPayeeMappingResponse {

	private String userGuid;

	private String payeeNumber;

	private Boolean payeeIsActive;

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

	public Boolean getPayeeIsActive() {
		return payeeIsActive;
	}

	public void setPayeeIsActive(Boolean payeeIsActive) {
		this.payeeIsActive = payeeIsActive;
	}

	@Override
	public String toString() {
		return "UserPayeeMappingResponse [userGuid=" + userGuid + ", payeeNumber=" + payeeNumber + ", payeeIsActive=" + payeeIsActive + "]";
	}

}
