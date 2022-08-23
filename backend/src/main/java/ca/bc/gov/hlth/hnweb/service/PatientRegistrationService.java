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

	/**
	 * This method checks if patient is registered with same MSP Payee number or Payee within/outside reporting group
	 * @param registrationRecords
	 * @param payee
	 * @param phn
	 * @return message if not registerd with assigned user payee.
	 */
	public String checktRegistrationDetails(List<PatientRegister> registrationRecords, String payee, String phn) {
		String registrationMessage = "";

		if (!registrationRecords.isEmpty()) {

			boolean isSamePayeeWithinGroup = registrationRecords.stream()
					.anyMatch(p -> payee.contentEquals(p.getPayeeNumber()));

			// Check if patient registered with different payee within reporting group
			if (!isSamePayeeWithinGroup) {
				registrationMessage = "Patient is registered with a different MSP Payee number within the reporting group";
			}

		} else {
			// Check if patient registered with different payee outside of reporting group
			List<String> payeeList = getPayeeByPHN(phn);

			if (!payeeList.isEmpty()) {

				registrationMessage = "Patient is registered with a different MSP Payee number outside of reporting group";

			}

		}
		return registrationMessage;
	}

}
