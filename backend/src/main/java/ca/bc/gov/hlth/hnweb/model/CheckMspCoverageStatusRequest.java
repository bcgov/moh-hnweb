package ca.bc.gov.hlth.hnweb.model;

import java.util.Date;

/**
 * Request message for API call to check MSP coverage status 
 *
 */
public class CheckMspCoverageStatusRequest {
	
	private String phn;
	
	private Date dateOfBirth;
	
	private Date dateOfService;
	
	private Boolean checkSubsidyInsuredService;
	
	private Boolean checkLastEyeExam; 
	
	private Boolean checkPatientRestriction;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getDateOfService() {
		return dateOfService;
	}

	public void setDateOfService(Date dateOfService) {
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
