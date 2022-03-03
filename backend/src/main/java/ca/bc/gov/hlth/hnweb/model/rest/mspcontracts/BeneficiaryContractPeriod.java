package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

public class BeneficiaryContractPeriod {

	private String phn;

	private String firstName;

	private String lastName;

	private String dateOfBirth;

	private String gender;

	private String contractHolder;
	
	private String relationship;
	
	private String groupNumber;
	
	private String effectiveDate;
	
	private String cancelDate;
	
	private String cancelReason;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

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
		return "BeneficiaryContractPeriod [phn=" + phn + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", contractHolder=" + contractHolder
				+ ", relationship=" + relationship + ", groupNumber=" + groupNumber + ", effectiveDate=" + effectiveDate
				+ ", cancelDate=" + cancelDate + ", cancelReason=" + cancelReason + "]";
	}

}
