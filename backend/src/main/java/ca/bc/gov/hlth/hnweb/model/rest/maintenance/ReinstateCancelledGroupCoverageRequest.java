package ca.bc.gov.hlth.hnweb.model.rest.maintenance;

import java.time.LocalDate;

public class ReinstateCancelledGroupCoverageRequest {
	private String groupNumber;
	private String phn;
	private LocalDate cancelDateToBeRemoved;

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

	public LocalDate getCancelDateToBeRemoved() {
		return cancelDateToBeRemoved;
	}

	public void setCancelDateToBeRemoved(LocalDate cancelDateToBeCovered) {
		this.cancelDateToBeRemoved = cancelDateToBeCovered;
	}

	@Override
	public String toString() {
		return "ReinstateCancelledGroupCoverageRequest [groupNumber=" + groupNumber + ", phn=" + phn
				+ ", cancelDateToBeRemoved=" + cancelDateToBeRemoved + "]";
	}
	
}
