package ca.bc.gov.hlth.hnweb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.pbf.PBFProperties;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PBFClinicPayeeRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PatientRegisterRepository;

/**
 * Service for processing patient registration requests
 *
 */
@Service
public class PatientRegistrationService extends BaseService {

	@Autowired
	private PatientRegisterRepository patientRegisterRepository;
	
	@Autowired
	private PBFClinicPayeeRepository pbfClinicPayeeRepository;
	
	@Autowired
	private PBFProperties pbfProperties;

	/**
	 * Gets a history of Patient Registration.
	 * @param payee The user's payee
	 * @param phn The patient's PHN
	 * @return The results and message (if applicable)
	 */
	public RegistrationResult getPatientRegistration(String payee, String phn) {
		RegistrationResult result = new RegistrationResult();		
		List<String> validPayees = pbfClinicPayeeRepository.findPayeeClinicViaReportGroup(payee);
		
		// Find all registration records
		List<PatientRegister> patientRegisters = patientRegisterRepository.findByPhnAndArchivedIsFalseOrderByEffectiveDateDesc(phn);
		
		if (patientRegisters.isEmpty()) {
			return result;
		}
		
		// Check the most recent record
		PatientRegister currentPatientRegister = patientRegisters.get(0);
		
		String registrationMessage = null;
		// If the current payee matches the user's payee do nothing
		if (!StringUtils.equals(currentPatientRegister.getPayeeNumber(), payee)) {

			if (validPayees.contains(currentPatientRegister.getPayeeNumber())) {
				// If the current payee belongs to the user's report group show a warning		
				registrationMessage = "Patient is registered with a different MSP Payee number within the reporting group";
			} else {
				// If the current payee is outisde the user's reporting group show a warning			
				registrationMessage = "Patient is registered with a different MSP Payee number outside of reporting group";
			}
			
		}

		// Filter out records from outside the reporting group
		List<PatientRegister> filteredPatientRegisters = patientRegisters.stream().filter(pr -> validPayees.contains(pr.getPayeeNumber())).collect(Collectors.toList());
		
		// Populate descriptions
		filteredPatientRegisters.forEach(pr -> {
			populateDescriptions(pr);
		});
		
		result.setPatientRegisters(filteredPatientRegisters);
		result.setRegistrationMessage(registrationMessage);
		return result;
	}
	
	private void populateDescriptions(PatientRegister patientRegister) {
		if (StringUtils.isNotEmpty(patientRegister.getCancelReasonCode())) {
			patientRegister.setCancelReasonDesc(pbfProperties.getCancelReasons().get(patientRegister.getCancelReasonCode()));
		}
		if (StringUtils.isNotEmpty(patientRegister.getDeregistrationReasonCode())) {
			patientRegister.setDeregistrationReasonDesc(pbfProperties.getDeregistrationReasons().get(patientRegister.getDeregistrationReasonCode()));
		}
		if (StringUtils.isNotEmpty(patientRegister.getRegistrationReasonCode())) {
			patientRegister.setRegistrationReasonDesc(pbfProperties.getRegistrationReasons().get(patientRegister.getRegistrationReasonCode()));
		}
	}

}
