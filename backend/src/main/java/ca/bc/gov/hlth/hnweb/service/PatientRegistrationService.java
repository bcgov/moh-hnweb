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

	public List<PatientRegister> getPatientRegistration(String payee, String phn) {
		return patientRegisterRepository.findPatientRegisterByPayeeClinic(payee, phn);
	}

	public List<String> getPayeeByPHN(String phn) {
		return patientRegisterRepository.findPayeeByphn(phn);
	}

}
