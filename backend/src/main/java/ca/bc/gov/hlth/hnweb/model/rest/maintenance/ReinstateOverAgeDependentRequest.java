package ca.bc.gov.hlth.hnweb.model.rest.maintenance;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReinstateOverAgeDependentRequest {

	private String phn;

	private String groupNumber;

	private String dependentPhn;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dependentDateOfBirth;

	private String isStudent;

	@JsonFormat(pattern = "yyyy-MM-dd")
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

	public LocalDate getDependentDateOfBirth() {
		return dependentDateOfBirth;
	}

	public void setDependentDateOfBirth(LocalDate dependentDateOfBirth) {
		this.dependentDateOfBirth = dependentDateOfBirth;
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
}
