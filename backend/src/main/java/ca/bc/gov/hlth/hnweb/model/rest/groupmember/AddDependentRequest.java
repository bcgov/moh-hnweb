package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import java.time.LocalDate;

public class AddDependentRequest {
	private String phn;
	private String groupNumber;
	private String dependentPHN;
	private LocalDate coverageEffectiveDate;
	private String relationship;
	
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

	public String getDependentPHN() {
		return dependentPHN;
	}

	public void setDependentPHN(String dependentPHN) {
		this.dependentPHN = dependentPHN;
	}

	public LocalDate getCoverageEffectiveDate() {
		return coverageEffectiveDate;
	}

	public void setCoverageEffectiveDate(LocalDate coverageEffectiveDate) {
		this.coverageEffectiveDate = coverageEffectiveDate;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	@Override
	public String toString() {
		return "AddDependentRequest [phn=" + phn + ", groupNumber=" + groupNumber + ", dependentPHN=" + dependentPHN
				+ ", coverageEffectiveDate=" + coverageEffectiveDate + ", relationship=" + relationship + "]";
	}

}
