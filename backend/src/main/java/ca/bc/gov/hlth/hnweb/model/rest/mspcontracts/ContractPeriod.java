package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

public class ContractPeriod {

	private String contractHolder;
	
	private String relationship;
	
	private String groupNumber;
	
	private String effectiveDate;
	
	private String cancelDate;
	
	private String cancelReason;

	public String getContractHolder() {
		return contractHolder;
	}

	public void setContractHolder(String contractHolder) {
		this.contractHolder = contractHolder;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	@Override
	public String toString() {
		return "ContractPeriod [contractHolder=" + contractHolder + ", relationship=" + relationship + ", groupNumber="
				+ groupNumber + ", effectiveDate=" + effectiveDate + ", cancelDate=" + cancelDate + ", cancelReason="
				+ cancelReason + "]";
	}
		
}
