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
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.ViewPatientRegisterRequest;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.ViewPatientRegisterResponse;
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

	private static final Logger logger = LoggerFactory.getLogger(PatientRegistrationController.class);

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private PatientRegistrationService patientRegistrationService;

	@PostMapping("/registration-history")
	public ResponseEntity<ViewPatientRegisterResponse> getRegistrationHistory(
			@Valid @RequestBody ViewPatientRegisterRequest viewPatientRegisterRequest, HttpServletRequest request) {

		logger.info("View Patient Register request: {} ", viewPatientRegisterRequest.getPhn());

		Transaction transaction = transactionStart(request, TransactionType.GET_PERSON_DETAILS);
		addAffectedParty(transaction, IdentifierType.PHN, viewPatientRegisterRequest.getPhn(),
				AffectedPartyDirection.INBOUND);

		try {
			// Retrieve demographic details
			GetDemographicsConverter converter = new GetDemographicsConverter();
			GetDemographicsRequest demographicsRequest = converter.convertRequest(viewPatientRegisterRequest.getPhn());
			GetDemographicsResponse demoGraphicsResponse = enrollmentService.getDemographics(demographicsRequest,
					transaction);
			GetPersonDetailsResponse personDetailsResponse = converter.convertResponse(demoGraphicsResponse);

			ViewPatientRegisterResponse response = new ViewPatientRegisterResponse();
			response.setPersonDetail(personDetailsResponse);

			// Retrieve patient registration history
			List<PatientRegisterModel> registrationHistory = convertPatientRegister(
					patientRegistrationService.getPatientRegister(viewPatientRegisterRequest.getPayee()));
			response.setPatientRegistrationHistory(registrationHistory);

			handlePatientRegistrationResponse(response);

			ResponseEntity<ViewPatientRegisterResponse> responseEntity = ResponseEntity.ok(response);

			auditvViewPatientRegisterComplete(transaction, personDetailsResponse);

			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	private ViewPatientRegisterResponse handlePatientRegistrationResponse(
			ViewPatientRegisterResponse patientRegistrationResponse) {
		Set<String> messages = new HashSet<>();
		patientRegistrationResponse.setStatus(StatusEnum.SUCCESS);

		GetPersonDetailsResponse personDetailsResponse = patientRegistrationResponse.getPersonDetail();
		//Person detail error will be considered as warning for patient register response
		if (personDetailsResponse.getStatus() == StatusEnum.ERROR) {
			patientRegistrationResponse.setPersonDetail(null);
			patientRegistrationResponse.setStatus(StatusEnum.WARNING);
			messages.add(personDetailsResponse.getMessage());
		}
		if (personDetailsResponse.getStatus() == StatusEnum.WARNING) {
			patientRegistrationResponse.setStatus(StatusEnum.WARNING);
			messages.add(personDetailsResponse.getMessage());
		}

		if (patientRegistrationResponse.getPatientRegistrationHistory().size() > 0) {
			if (personDetailsResponse.getStatus() == StatusEnum.SUCCESS) {
				messages.add("Transaction completed successfully");
			}
		} else {
			messages.add(
					"No Patient Register History were returned. Please refine your search criteria, and try again");
			patientRegistrationResponse.setStatus(StatusEnum.WARNING);
		}
		patientRegistrationResponse.setMessage(String.join("\n", messages));

		return patientRegistrationResponse;
	}

	private List<PatientRegisterModel> convertPatientRegister(List<PatientRegister> patientRegistrations) {
		List<PatientRegisterModel> pateintRegisterModels = new ArrayList<>();
		patientRegistrations.forEach(patientRegistration -> {
			PatientRegisterModel model = new PatientRegisterModel();

			model.setEffectiveDate(convertDate(patientRegistration.getEffectiveDate()));
			model.setCurrentStatus(setStatus(patientRegistration.getCancelDate()));
			model.setCancelDate(convertDate(patientRegistration.getCancelDate()));
			model.setAdministrativeCode(patientRegistration.getAdministrativeCode());
			model.setRegistrationReasonCode(StringUtils.isEmpty(patientRegistration.getRegistrationReasonCode()) ? "N/A"
					: patientRegistration.getRegistrationReasonCode());
			model.setCancelReasonCode(StringUtils.isEmpty(patientRegistration.getCancelReasonCode()) ? "N/A"
					: patientRegistration.getCancelReasonCode());
			model.setDeregistrationReasonCode(
					StringUtils.isEmpty(patientRegistration.getDeregistrationReasonCode()) ? "N/A"
							: patientRegistration.getDeregistrationReasonCode());
			model.setPayeeNumber(patientRegistration.getPayeeNumber());
			model.setRegisteredPractitionerNumber(patientRegistration.getRegisteredPractitionerNumber());
			model.setPhn(patientRegistration.getPhn());

			pateintRegisterModels.add(model);

		});
		return pateintRegisterModels;

	}

	private void auditvViewPatientRegisterComplete(Transaction transaction, GetPersonDetailsResponse personDetailsResponse) {
		transactionComplete(transaction);
		if (StringUtils.isNotBlank(personDetailsResponse.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, personDetailsResponse.getPhn(),
					AffectedPartyDirection.OUTBOUND);
		}
	}

	private LocalDate convertDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private String setStatus(Date cancelDate) {
		Date date = new Date();
		return date.compareTo(cancelDate) > 0 ? "Registered" : "De-Registered";

	}

}
