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
	
	@JsonFormat(pattern="yyyyMMdd")
	private Date dateOfService;
	
	private String eligibleOnDateOfService;
	
	private String coverageEndDate;
	
	private String coverageEndReason;
	
	private String subsidyInsuredService;
	
	private String dateOfLastEyeExamination;
	
	private String patientRestriction;
	
	private String carecardWarning;

	private String errorMessage;
	
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

	public String getCarecardWarning() {
		return carecardWarning;
	}

	public void setCarecardWarning(String carecardWarning) {
		this.carecardWarning = carecardWarning;
	}
		
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carecardWarning == null) ? 0 : carecardWarning.hashCode());
		result = prime * result + ((coverageEndDate == null) ? 0 : coverageEndDate.hashCode());
		result = prime * result + ((coverageEndReason == null) ? 0 : coverageEndReason.hashCode());
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((dateOfLastEyeExamination == null) ? 0 : dateOfLastEyeExamination.hashCode());
		result = prime * result + ((dateOfService == null) ? 0 : dateOfService.hashCode());
		result = prime * result + ((eligibleOnDateOfService == null) ? 0 : eligibleOnDateOfService.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((givenName == null) ? 0 : givenName.hashCode());
		result = prime * result + ((nameTypeCode == null) ? 0 : nameTypeCode.hashCode());
		result = prime * result + ((patientRestriction == null) ? 0 : patientRestriction.hashCode());
		result = prime * result + ((phn == null) ? 0 : phn.hashCode());
		result = prime * result + ((secondName == null) ? 0 : secondName.hashCode());
		result = prime * result + ((subsidyInsuredService == null) ? 0 : subsidyInsuredService.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CheckMspCoverageStatusResponse other = (CheckMspCoverageStatusResponse) obj;
		if (carecardWarning == null) {
			if (other.carecardWarning != null)
				return false;
		} else if (!carecardWarning.equals(other.carecardWarning))
			return false;
		if (coverageEndDate == null) {
			if (other.coverageEndDate != null)
				return false;
		} else if (!coverageEndDate.equals(other.coverageEndDate))
			return false;
		if (coverageEndReason == null) {
			if (other.coverageEndReason != null)
				return false;
		} else if (!coverageEndReason.equals(other.coverageEndReason))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (dateOfLastEyeExamination == null) {
			if (other.dateOfLastEyeExamination != null)
				return false;
		} else if (!dateOfLastEyeExamination.equals(other.dateOfLastEyeExamination))
			return false;
		if (dateOfService == null) {
			if (other.dateOfService != null)
				return false;
		} else if (!dateOfService.equals(other.dateOfService))
			return false;
		if (eligibleOnDateOfService == null) {
			if (other.eligibleOnDateOfService != null)
				return false;
		} else if (!eligibleOnDateOfService.equals(other.eligibleOnDateOfService))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (givenName == null) {
			if (other.givenName != null)
				return false;
		} else if (!givenName.equals(other.givenName))
			return false;
		if (nameTypeCode == null) {
			if (other.nameTypeCode != null)
				return false;
		} else if (!nameTypeCode.equals(other.nameTypeCode))
			return false;
		if (patientRestriction == null) {
			if (other.patientRestriction != null)
				return false;
		} else if (!patientRestriction.equals(other.patientRestriction))
			return false;
		if (phn == null) {
			if (other.phn != null)
				return false;
		} else if (!phn.equals(other.phn))
			return false;
		if (secondName == null) {
			if (other.secondName != null)
				return false;
		} else if (!secondName.equals(other.secondName))
			return false;
		if (subsidyInsuredService == null) {
			if (other.subsidyInsuredService != null)
				return false;
		} else if (!subsidyInsuredService.equals(other.subsidyInsuredService))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CheckMspCoverageStatusResponse [phn=" + phn + ", surname=" + surname + ", givenName=" + givenName
				+ ", secondName=" + secondName + ", nameTypeCode=" + nameTypeCode + ", dateOfBirth=" + dateOfBirth
				+ ", gender=" + gender + ", dateOfService=" + dateOfService + ", eligibleOnDateOfService="
				+ eligibleOnDateOfService + ", coverageEndDate=" + coverageEndDate + ", coverageEndReason="
				+ coverageEndReason + ", subsidyInsuredService=" + subsidyInsuredService + ", dateOfLastEyeExamination="
				+ dateOfLastEyeExamination + ", patientRestriction=" + patientRestriction + ", carecardWarning="
				+ carecardWarning + "]";
	}
}
