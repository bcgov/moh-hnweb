package ca.bc.gov.hlth.hnweb.model.rest.eligibility;

public class LookupPhnRequest {
	private String contractNumber;

	private String groupNumber;

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	@Override
	public String toString() {
		return "LookupPhnRequest [contractNumber=" + contractNumber + ", groupNumber=" + groupNumber + "]";
	}

}
