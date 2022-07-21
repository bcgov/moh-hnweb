package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import java.time.LocalDate;

public class ChangeEffectiveDateRequest {
	private String phn;
	private String groupNumber;
	private LocalDate existingEffectiveDate;
	private LocalDate newEffectiveDate;

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



	public LocalDate getExistingEffectiveDate() {
		return existingEffectiveDate;
	}

	public void setExistingEffectiveDate(LocalDate existingEffectiveDate) {
		this.existingEffectiveDate = existingEffectiveDate;
	}

	public LocalDate getNewEffectiveDate() {
		return newEffectiveDate;
	}

	public void setNewEffectiveDate(LocalDate newEffectiveDate) {
		this.newEffectiveDate = newEffectiveDate;
	}

	@Override
	public String toString() {
		return "ChangeEffectiveDateRequest [phn=" + phn + ", groupNumber=" + groupNumber + ", existingEffectiveDate=" + existingEffectiveDate
				+ ", newEffectiveDate=" + newEffectiveDate + "]";
	}

}
