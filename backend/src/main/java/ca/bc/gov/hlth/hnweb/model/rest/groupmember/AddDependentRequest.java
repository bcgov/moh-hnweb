package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import java.time.LocalDate;

public class AddDependentRequest {
	private String phn;
	private String groupNumber;
	private String dependentPhn;
	private LocalDate coverageEffectiveDate;
	private String relationship;
	private String isStudent;
	private LocalDate studentEndDate;
	
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

	public String getDependentPhn() {
		return dependentPhn;
	}

	public void setDependentPhn(String dependentPhn) {
		this.dependentPhn = dependentPhn;
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

	public String getIsStudent() {
		return isStudent;
	}

	public void setIsStudent(String isStudent) {
		this.isStudent = isStudent;
	}

	public LocalDate getStudentEndDate() {
		return studentEndDate;
	}

	public void setStudentEndDate(LocalDate studentEndDate) {
		this.studentEndDate = studentEndDate;
	}

	@Override
	public String toString() {
		return "AddDependentRequest [phn=" + phn + ", groupNumber=" + groupNumber + ", dependentPhn=" + dependentPhn
				+ ", coverageEffectiveDate=" + coverageEffectiveDate + ", relationship=" + relationship + ", isStudent="
				+ isStudent + ", studentEndDate=" + studentEndDate + "]";
	}

}
