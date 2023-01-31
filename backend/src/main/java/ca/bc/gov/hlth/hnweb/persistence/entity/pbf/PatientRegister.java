package ca.bc.gov.hlth.hnweb.persistence.entity.pbf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "patient_register", schema = "pbf")
public class PatientRegister {

	@Id
	@Column(name = "patient_register_id", columnDefinition = "bigserial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long patientRegisterId;

	@Column(name = "phn")
	private String phn;

	@Column(name = "payee_number")
	private String payeeNumber;

	@Column(name = "registered_practitioner_number", nullable = false)
	private String registeredPractitionerNumber;

	@Column(name = "registered_practitioner_first_name", nullable = false)
	private String registeredPractitionerFirstName;

	@Column(name = "registered_practitioner_middle_name", nullable = false)
	private String registeredPractitionerMiddleName;

	@Column(name = "registered_practitioner_surname", nullable = false)
	private String registeredPractitionerSurname;

	@Column(name = "effective_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date effectiveDate;

	@Column(name = "cancel_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date cancelDate;

	@Column(name = "adminstrative_code", nullable = false)
	private String administrativeCode;

	@Column(name = "registration_reason_code", nullable = true)
	private String registrationReasonCode;

	@Column(name = "deregistration_reason_code", nullable = true)
	private String deregistrationReasonCode;

	@Column(name = "cancel_reason_code", nullable = true)
	private String cancelReasonCode;

	@Column(name = "archived", nullable = false)
	private Boolean archived = Boolean.FALSE;

	public Long getPatientRegisterId() {
		return patientRegisterId;
	}

	public void setPatientRegisterId(Long patientRegisterId) {
		this.patientRegisterId = patientRegisterId;
	}

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

	public String getRegisteredPractitionerFirstName() {
		return registeredPractitionerFirstName;
	}

	public void setRegisteredPractitionerFirstName(String registeredPractitionerFirstName) {
		this.registeredPractitionerFirstName = registeredPractitionerFirstName;
	}

	public String getRegisteredPractitionerMiddleName() {
		return registeredPractitionerMiddleName;
	}

	public void setRegisteredPractitionerMiddleName(String registeredPractitionerMiddleName) {
		this.registeredPractitionerMiddleName = registeredPractitionerMiddleName;
	}

	public String getRegisteredPractitionerSurname() {
		return registeredPractitionerSurname;
	}

	public void setRegisteredPractitionerSurname(String registeredPractitionerSurname) {
		this.registeredPractitionerSurname = registeredPractitionerSurname;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
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

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	@Override
	public String toString() {
		return "PatientRegister [patientRegisterId=" + patientRegisterId + ", phn=" + phn + ", payeeNumber=" + payeeNumber
				+ ", registeredPractitionerNumber=" + registeredPractitionerNumber + ", registeredPractitionerFirstName="
				+ registeredPractitionerFirstName + ", registeredPractitionerMiddleName=" + registeredPractitionerMiddleName
				+ ", registeredPractitionerSurname=" + registeredPractitionerSurname + ", effectiveDate=" + effectiveDate + ", cancelDate="
				+ cancelDate + ", administrativeCode=" + administrativeCode + ", registrationReasonCode=" + registrationReasonCode
				+ ", deregistrationReasonCode=" + deregistrationReasonCode + ", cancelReasonCode=" + cancelReasonCode + ", archived="
				+ archived + "]";
	}

}