package ca.bc.gov.hlth.hnweb.model.rest.pbf;

public class BcscPayeeMappingResponse {

	private String bcscGuid;

	private String payeeNumber;
	
    private Boolean payeeIsActive;

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

    public Boolean getPayeeIsActive() {
        return payeeIsActive;
    }

    public void setPayeeIsActive(Boolean payeeIsActive) {
        this.payeeIsActive = payeeIsActive;
    }

    @Override
    public String toString() {
        return "BcscPayeeMappingResponse [bcscGuid=" + bcscGuid + ", payeeNumber=" + payeeNumber + ", payeeIsActive="
                + payeeIsActive + "]";
    }

}
