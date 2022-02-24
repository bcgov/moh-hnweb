package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

public class ContractInquiryRequest {
	private String phn;
	private String groupNumber;
	
	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	@Override
	public String toString() {
		return "ContractInquiryRequest [phn=" + phn + "]";
	}

}
