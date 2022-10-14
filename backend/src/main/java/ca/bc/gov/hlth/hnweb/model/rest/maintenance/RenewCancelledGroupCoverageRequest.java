package ca.bc.gov.hlth.hnweb.model.rest.maintenance;

import java.time.LocalDate;

public class RenewCancelledGroupCoverageRequest {
	private String groupNumber;
	private String phn;
	private LocalDate newCoverageEffectiveDate;

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public LocalDate getNewCoverageEffectiveDate() {
		return newCoverageEffectiveDate;
	}

	public void setNewCoverageEffectiveDate(LocalDate newEffectiveDate) {
		this.newCoverageEffectiveDate = newEffectiveDate;
	}

	@Override
	public String toString() {
		return "ChangeCancelDateRequest [groupNumber=" + groupNumber + ", phn=" + phn + ", newCoverageEffectiveDate="
				+ newCoverageEffectiveDate + "]";
	}
}
