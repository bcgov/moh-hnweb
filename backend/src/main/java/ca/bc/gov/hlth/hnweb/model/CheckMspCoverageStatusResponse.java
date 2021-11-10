package ca.bc.gov.hlth.hnweb.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Response message for API call to check MSP coverage status 
 *
 */
public class CheckMspCoverageStatusResponse {
	
	private String phn;

	private String surname;

	private String givenName;
	
	private String secondName;
	
	private String nameTypeCode;
	
	private String dateOfBirth;
	
	private String gender;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date dateOfService;
	
	private Boolean eligibleOnDateOfService;
	
	private String coverageEndDate;
	
	private String coverageEndReason;
	
	private String subsidyInsuredService;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date dateOfLastEyeExamination;
	
	private String patientRestriction;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getNameTypeCode() {
		return nameTypeCode;
	}

	public void setNameTypeCode(String nameTypeCode) {
		this.nameTypeCode = nameTypeCode;
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

	public Date getDateOfService() {
		return dateOfService;
	}

	public void setDateOfService(Date dateOfService) {
		this.dateOfService = dateOfService;
	}

	public Boolean getEligibleOnDateOfService() {
		return eligibleOnDateOfService;
	}

	public void setEligibleOnDateOfService(Boolean eligibleOnDateOfService) {
		this.eligibleOnDateOfService = eligibleOnDateOfService;
	}

	public String getCoverageEndDate() {
		return coverageEndDate;
	}

	public void setCoverageEndDate(String coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	public String getCoverageEndReason() {
		return coverageEndReason;
	}

	public void setCoverageEndReason(String coverageEndReason) {
		this.coverageEndReason = coverageEndReason;
	}

	public String getSubsidyInsuredService() {
		return subsidyInsuredService;
	}

	public void setSubsidyInsuredService(String subsidyInsuredService) {
		this.subsidyInsuredService = subsidyInsuredService;
	}

	public Date getDateOfLastEyeExamination() {
		return dateOfLastEyeExamination;
	}

	public void setDateOfLastEyeExamination(Date dateOfLastEyeExamination) {
		this.dateOfLastEyeExamination = dateOfLastEyeExamination;
	}

	public String getPatientRestriction() {
		return patientRestriction;
	}

	public void setPatientRestriction(String patientRestriction) {
		this.patientRestriction = patientRestriction;
	}

}
