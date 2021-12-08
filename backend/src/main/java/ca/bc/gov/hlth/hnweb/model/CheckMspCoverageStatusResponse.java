package ca.bc.gov.hlth.hnweb.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Response message for API call to check MSP coverage status
 *
 */
public class CheckMspCoverageStatusResponse extends BaseResponse {

	private String phn;

	private String surname;

	private String givenName;

	private String secondName;

	private String dateOfBirth;

	private String gender;

	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate dateOfService;

	private String eligibleOnDateOfService;

	private String coverageEndDate;

	private String coverageEndReason;

	private String subsidyInsuredService;

	private String dateOfLastEyeExamination;

	private String patientRestriction;

	private String careCardWarning;

	private String clientInstructions;

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

	public LocalDate getDateOfService() {
		return dateOfService;
	}

	public void setDateOfService(LocalDate dateOfService) {
		this.dateOfService = dateOfService;
	}

	public String getEligibleOnDateOfService() {
		return eligibleOnDateOfService;
	}

	public void setEligibleOnDateOfService(String eligibleOnDateOfService) {
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

	public String getDateOfLastEyeExamination() {
		return dateOfLastEyeExamination;
	}

	public void setDateOfLastEyeExamination(String dateOfLastEyeExamination) {
		this.dateOfLastEyeExamination = dateOfLastEyeExamination;
	}

	public String getPatientRestriction() {
		return patientRestriction;
	}

	public void setPatientRestriction(String patientRestriction) {
		this.patientRestriction = patientRestriction;
	}

	public String getCareCardWarning() {
		return careCardWarning;
	}

	public void setCareCardWarning(String careCardWarning) {
		this.careCardWarning = careCardWarning;
	}

	public String getClientInstructions() {
		return clientInstructions;
	}

	public void setClientInstructions(String clientInstructions) {
		this.clientInstructions = clientInstructions;
	}

	@Override
	public String toString() {
		return "CheckMspCoverageStatusResponse [phn=" + phn + ", surname=" + surname + ", givenName=" + givenName + ", secondName="
				+ secondName + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", dateOfService=" + dateOfService
				+ ", eligibleOnDateOfService=" + eligibleOnDateOfService + ", coverageEndDate=" + coverageEndDate + ", coverageEndReason="
				+ coverageEndReason + ", subsidyInsuredService=" + subsidyInsuredService + ", dateOfLastEyeExamination="
				+ dateOfLastEyeExamination + ", patientRestriction=" + patientRestriction + ", careCardWarning=" + careCardWarning
				+ ", clientInstructions=" + clientInstructions + ", status=" + status + ", message=" + message + "]";
	}

}
