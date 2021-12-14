package ca.bc.gov.hlth.hnweb.model.eligibility;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Request message for API call to check MSP coverage status 
 *
 */
public class CheckMspCoverageStatusRequest {
	
	private String phn;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dateOfBirth;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dateOfService;
	
	private Boolean checkSubsidyInsuredService;
	
	private Boolean checkLastEyeExam; 
	
	private Boolean checkPatientRestriction;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	@JsonFormat(pattern="yyyy-MM-dd")
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	@JsonFormat(pattern="yyyy-MM-dd")
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@JsonFormat(pattern="yyyy-MM-dd")
	public LocalDate getDateOfService() {
		return dateOfService;
	}

	@JsonFormat(pattern="yyyy-MM-dd")
	public void setDateOfService(LocalDate dateOfService) {
		this.dateOfService = dateOfService;
	}

	public Boolean getCheckSubsidyInsuredService() {
		return checkSubsidyInsuredService;
	}

	public void setCheckSubsidyInsuredService(Boolean checkSubsidyInsuredService) {
		this.checkSubsidyInsuredService = checkSubsidyInsuredService;
	}

	public Boolean getCheckLastEyeExam() {
		return checkLastEyeExam;
	}

	public void setCheckLastEyeExam(Boolean checkLastEyeExam) {
		this.checkLastEyeExam = checkLastEyeExam;
	}

	public Boolean getCheckPatientRestriction() {
		return checkPatientRestriction;
	}

	public void setCheckPatientRestriction(Boolean checkPatientRestriction) {
		this.checkPatientRestriction = checkPatientRestriction;
	}

	@Override
	public String toString() {
		return "CheckMspCoverageStatusRequest [phn=" + phn + ", dateOfBirth=" + dateOfBirth + ", dateOfService="
				+ dateOfService + ", checkSubsidyInsuredService=" + checkSubsidyInsuredService + ", checkLastEyeExam="
				+ checkLastEyeExam + ", checkPatientRestriction=" + checkPatientRestriction + "]";
	}
}
