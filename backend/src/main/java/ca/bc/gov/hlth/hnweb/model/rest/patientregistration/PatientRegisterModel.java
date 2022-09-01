package ca.bc.gov.hlth.hnweb.model.rest.patientregistration;

public class PatientRegisterModel {

	private String phn;

	private String payeeNumber;

	private String registeredPractitionerNumber;

	private String effectiveDate;

	private String cancelDate;

	private String administrativeCode;

	private String registrationReasonCode;

	private String deregistrationReasonCode;

	private String cancelReasonCode;
	
	private String currentStatus;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getPayeeNumber() {
		return payeeNumber;
	}

	public void setPayeeNumber(String payeeNumber) {
		this.payeeNumber = payeeNumber;
	}

	public String getRegisteredPractitionerNumber() {
		return registeredPractitionerNumber;
	}

	public void setRegisteredPractitionerNumber(String registeredPractitionerNumber) {
		this.registeredPractitionerNumber = registeredPractitionerNumber;
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

	public String getAdministrativeCode() {
		return administrativeCode;
	}

	public void setAdministrativeCode(String administrativeCode) {
		this.administrativeCode = administrativeCode;
	}

	public String getRegistrationReasonCode() {
		return registrationReasonCode;
	}

	public void setRegistrationReasonCode(String registrationReasonCode) {
		this.registrationReasonCode = registrationReasonCode;
	}

	public String getDeregistrationReasonCode() {
		return deregistrationReasonCode;
	}

	public void setDeregistrationReasonCode(String deregistrationReasonCode) {
		this.deregistrationReasonCode = deregistrationReasonCode;
	}

	public String getCancelReasonCode() {
		return cancelReasonCode;
	}

	public void setCancelReasonCode(String cancelReasonCode) {
		this.cancelReasonCode = cancelReasonCode;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
}
