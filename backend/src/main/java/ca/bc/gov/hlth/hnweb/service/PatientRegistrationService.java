package ca.bc.gov.hlth.hnweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PatientRegisterRepository;


/**
 * Service for processing patient registration requests 
 *
 */
@Service
public class PatientRegistrationService extends BaseService {

	@Autowired
	private PatientRegisterRepository patientRegisterRepository;
	
	public List<PatientRegister> getPatientRegister(String payee) {
		return patientRegisterRepository.findPatientRegisterByPayeeClinic(payee);
	}
}
