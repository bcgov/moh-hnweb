package ca.bc.gov.hlth.hnweb.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.converter.hl7v3.GetDemographicsConverter;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegisterModel;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegistrationRequest;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegistrationResponse;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;
import ca.bc.gov.hlth.hnweb.service.PatientRegistrationService;

/**
 * Handles request related to R70 Patient Registration.
 */
@RequestMapping("/patient-registration")
@RestController
public class PatientRegistrationController extends BaseController {

	private static final String FUTURE_REGISTRATION = "Future Registration";

	private static final String DE_REGISTERED = "De-Registered";

	private static final String REGISTERED = "Registered";

	private static final String NOT_APPLICABLE = "N/A";

	private static final Logger logger = LoggerFactory.getLogger(PatientRegistrationController.class);

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private PatientRegistrationService patientRegistrationService;

	@PostMapping("/get-patient-registration")
	public ResponseEntity<PatientRegistrationResponse> getPatientRegistration(
			@Valid @RequestBody PatientRegistrationRequest patientRegistrationRequest, HttpServletRequest request) {

		logger.info("Patient Registration request: {} ", patientRegistrationRequest.getPhn());

		Transaction transaction = transactionStart(request, TransactionType.GET_PATIENT_REGISTRATION);
		addAffectedParty(transaction, IdentifierType.PHN, patientRegistrationRequest.getPhn(),
				AffectedPartyDirection.INBOUND);

		try {
			// Retrieve demographic details
			GetDemographicsConverter converter = new GetDemographicsConverter();
			GetDemographicsRequest demographicsRequest = converter.convertRequest(patientRegistrationRequest.getPhn());
			GetDemographicsResponse demoGraphicsResponse = enrollmentService.getDemographics(demographicsRequest,
					transaction);
			GetPersonDetailsResponse personDetailsResponse = converter.convertResponse(demoGraphicsResponse);

			PatientRegistrationResponse response = new PatientRegistrationResponse();
			response.setPersonDetail(personDetailsResponse);

			// Retrieve patient registration history
			boolean patientRegistrationExist = false;

			List<PatientRegister> registrationRecords = patientRegistrationService
					.getPatientRegistration(patientRegistrationRequest.getPayee(), patientRegistrationRequest.getPhn());

			String registrationMessage = patientRegistrationService.checktRegistrationDetails(registrationRecords,
					patientRegistrationRequest.getPayee(), patientRegistrationRequest.getPhn());

			if (!registrationRecords.isEmpty() || StringUtils.isNotBlank(registrationMessage)) {
				patientRegistrationExist = true;

			}

			List<PatientRegisterModel> registrationHistory = convertPatientRegistration(registrationRecords);
			response.setPatientRegistrationHistory(registrationHistory);

			handlePatientRegistrationResponse(response, patientRegistrationExist, registrationMessage);

			ResponseEntity<PatientRegistrationResponse> responseEntity = ResponseEntity.ok(response);

			transactionComplete(transaction);
			registrationRecords.forEach(record -> addAffectedParty(transaction, IdentifierType.PHN, record.getPhn(),
					AffectedPartyDirection.OUTBOUND));

			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	private PatientRegistrationResponse handlePatientRegistrationResponse(
			PatientRegistrationResponse patientRegistrationResponse, boolean registrationExist, String infoMessage) {

		Set<String> messages = new HashSet<>();
		patientRegistrationResponse.setStatus(StatusEnum.SUCCESS);
		patientRegistrationResponse.setMessage("Transaction completed successfully");

		GetPersonDetailsResponse personDetailsResponse = patientRegistrationResponse.getPersonDetail();

		if (registrationExist && !StringUtils.isEmpty(infoMessage)) {
			messages.add(infoMessage);
		} else if (!registrationExist) {
			// Check if demographics record found
			if (personDetailsResponse.getStatus() == StatusEnum.ERROR) {
				// If no demographics record found, set status as WARNING
				patientRegistrationResponse.setStatus(StatusEnum.WARNING);
				patientRegistrationResponse.setMessage("Patient could not be found in the EMPI or in the PBF.");
			} else {
				// Patient exists in EMPI but not in PBF
				messages.add("No registration information is found in the system for given PHN");
			}
		}

		if (personDetailsResponse.getStatus() != StatusEnum.SUCCESS) {
			messages.add(personDetailsResponse.getMessage());
		}
		patientRegistrationResponse.setAdditionalInfoMessage(String.join("\n", messages));

		return patientRegistrationResponse;
	}

	private List<PatientRegisterModel> convertPatientRegistration(List<PatientRegister> patientRegistrations) {
		List<PatientRegisterModel> pateintRegisterModels = new ArrayList<>();
		patientRegistrations.forEach(patientRegistration -> {
			PatientRegisterModel model = new PatientRegisterModel();

			model.setEffectiveDate(convertDate(patientRegistration.getEffectiveDate()));
			model.setCancelDate(convertDate(patientRegistration.getCancelDate()));
			model.setCurrentStatus(setPatientRegistrationStatus(model.getCancelDate(), model.getEffectiveDate()));
			model.setAdministrativeCode(patientRegistration.getAdministrativeCode());
			model.setRegistrationReasonCode(
					StringUtils.isEmpty(patientRegistration.getRegistrationReasonCode()) ? NOT_APPLICABLE
							: patientRegistration.getRegistrationReasonCode());
			model.setCancelReasonCode(StringUtils.isEmpty(patientRegistration.getCancelReasonCode()) ? NOT_APPLICABLE
					: patientRegistration.getCancelReasonCode());
			model.setDeregistrationReasonCode(
					StringUtils.isEmpty(patientRegistration.getDeregistrationReasonCode()) ? NOT_APPLICABLE
							: patientRegistration.getDeregistrationReasonCode());
			model.setPayeeNumber(patientRegistration.getPayeeNumber());
			model.setRegisteredPractitionerNumber(patientRegistration.getRegisteredPractitionerNumber());
			model.setPhn(patientRegistration.getPhn());

			pateintRegisterModels.add(model);

		});
		return pateintRegisterModels;

	}

	private LocalDate convertDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private String setPatientRegistrationStatus(LocalDate cancelDate, LocalDate effectiveDate) {
		String currentStatus = "";
		LocalDate today = LocalDate.now();

		// “Registered” when the current date is greater than or equal to the effective
		// date and less than or equal to the cancel date.
		// “De-Registered” when the current date is later than the registration cancel
		// date
		// “Future Registration” when the registration date is in the future

		if (today.compareTo(effectiveDate) >= 0 && today.compareTo(cancelDate) <= 0) {
			currentStatus = REGISTERED;
		} else if (today.compareTo(cancelDate) > 0) {
			currentStatus = DE_REGISTERED;
		} else if (effectiveDate.compareTo(today) > 0) {
			currentStatus = FUTURE_REGISTRATION;
		}

		return currentStatus;

	}

}
