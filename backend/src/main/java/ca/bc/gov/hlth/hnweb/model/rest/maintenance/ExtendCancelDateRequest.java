package ca.bc.gov.hlth.hnweb.model.rest.maintenance;

import java.time.LocalDate;

public class ExtendCancelDateRequest {
	private String groupNumber;
	private String phn;
	private LocalDate existingCancellationDate;
	private LocalDate newCancellationDate;
	private String immigrationCode;
	private LocalDate permitIssueDate;
	private LocalDate permitExpiryDate;

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

	public LocalDate getExistingCancellationDate() {
		return existingCancellationDate;
	}

	public void setExistingCancellationDate(LocalDate existingCancellationDate) {
		this.existingCancellationDate = existingCancellationDate;
	}

	public LocalDate getNewCancellationDate() {
		return newCancellationDate;
	}

	public void setNewCancellationDate(LocalDate newCancellationDate) {
		this.newCancellationDate = newCancellationDate;
	}

	public String getImmigrationCode() {
		return immigrationCode;
	}

	public void setImmigrationCode(String immigrationCode) {
		this.immigrationCode = immigrationCode;
	}

	public LocalDate getPermitIssueDate() {
		return permitIssueDate;
	}

	public void setPermitIssueDate(LocalDate permitIssueDate) {
		this.permitIssueDate = permitIssueDate;
	}

	public LocalDate getPermitExpiryDate() {
		return permitExpiryDate;
	}

	public void setPermitExpiryDate(LocalDate permitExpiryDate) {
		this.permitExpiryDate = permitExpiryDate;
	}

	@Override
	public String toString() {
		return "ExtendCancelDateRequest [groupNumber=" + groupNumber + ", phn=" + phn + ", existingCancellationDate="
				+ existingCancellationDate + ", newCancellationDate=" + newCancellationDate + ", immigrationCode="
				+ immigrationCode + ", permitIssueDate=" + permitIssueDate + ", permitExpiryDate=" + permitExpiryDate
				+ "]";
	}

}
