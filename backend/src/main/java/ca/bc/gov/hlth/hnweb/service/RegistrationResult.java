package ca.bc.gov.hlth.hnweb.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;

public class RegistrationResult {
	private List<PatientRegister> patientRegisters = new ArrayList<>();

	private String registrationMessage;

	public List<PatientRegister> getPatientRegisters() {
		return patientRegisters;
	}

	public void setPatientRegisters(List<PatientRegister> patientRegisters) {
		this.patientRegisters = patientRegisters;
	}

	public String getRegistrationMessage() {
		return registrationMessage;
	}

	public void setRegistrationMessage(String registrationMessage) {
		this.registrationMessage = registrationMessage;
	}
	
	public Boolean exists() {
		return !patientRegisters.isEmpty() || StringUtils.isNotEmpty(registrationMessage);
	}

}
