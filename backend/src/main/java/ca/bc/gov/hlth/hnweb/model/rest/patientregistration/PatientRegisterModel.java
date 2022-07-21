package ca.bc.gov.hlth.hnweb.model.rest.patientregistration;

import java.time.LocalDate;

public class PatientRegisterModel {

	private String phn;

	private String payeeNumber;

	private String registeredPractitionerNumber;

	private LocalDate effectiveDate;

	private LocalDate cancelDate;

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

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public LocalDate getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDate cancelDate) {
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
