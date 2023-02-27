package ca.bc.gov.hlth.hnweb.model.rest.pbf;

public class BcscPayeeMappingResponse {

	private String bcscGuid;

	private String payeeNumber;
	
    private PayeeStatus payeeStatus;

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

    public PayeeStatus getPayeeStatus() {
        return payeeStatus;
    }

    public void setPayeeStatus(PayeeStatus payeeStatus) {
        this.payeeStatus = payeeStatus;
    }

    @Override
    public String toString() {
        return "BcscPayeeMappingResponse [bcscGuid=" + bcscGuid + ", payeeNumber=" + payeeNumber + ", payeeStatus="
                + payeeStatus + "]";
    }
	
}
