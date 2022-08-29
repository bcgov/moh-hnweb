package ca.bc.gov.hlth.hnweb.model.rest.maintenance;

import java.time.LocalDate;

public class ChangeCancelDateRequest {
	private String groupNumber;
	private String phn;
	private LocalDate existingCancellationDate;
	private LocalDate newCancellationDate;

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

	@Override
	public String toString() {
		return "ChangeCancelDateRequest [groupNumber=" + groupNumber + ", phn=" + phn + ", existingCancellationDate="
				+ existingCancellationDate + ", newCancellationDate=" + newCancellationDate + "]";
	}

}
