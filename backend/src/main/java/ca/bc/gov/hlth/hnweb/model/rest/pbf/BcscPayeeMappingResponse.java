package ca.bc.gov.hlth.hnweb.model.rest.pbf;

public class BcscPayeeMappingResponse {

	private String bcscGuid;

	private String payeeNumber;

	public String getBcscGuid() {
		return bcscGuid;
	}

	public void setBcscGuid(String bcscGuid) {
		this.bcscGuid = bcscGuid;
	}

	public String getPayeeNumber() {
		return payeeNumber;
	}

	public void setPayeeNumber(String payeeNumber) {
		this.payeeNumber = payeeNumber;
	}

	@Override
	public String toString() {
		return "BcscPayeeMapping [bcscGuid=" + bcscGuid + ", payeeNumber=" + payeeNumber + "]";
	}

}
