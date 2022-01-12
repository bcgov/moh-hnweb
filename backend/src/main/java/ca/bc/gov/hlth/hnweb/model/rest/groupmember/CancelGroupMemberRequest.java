package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import java.time.LocalDate;

public class CancelGroupMemberRequest {
	private String phn;
	private String groupNumber;
	private LocalDate coverageCancelDate;
	// Valid values are K, E
	private String cancelReason;

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

	public LocalDate getCoverageCancelDate() {
		return coverageCancelDate;
	}

	public void setCoverageCancelDate(LocalDate coverageCancelDate) {
		this.coverageCancelDate = coverageCancelDate;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	@Override
	public String toString() {
		return "CancelGroupMemberRequest [phn=" + phn + ", groupNumber=" + groupNumber + ", coverageCancelDate=" + coverageCancelDate
				+ ", cancelReason=" + cancelReason + "]";
	}

}
