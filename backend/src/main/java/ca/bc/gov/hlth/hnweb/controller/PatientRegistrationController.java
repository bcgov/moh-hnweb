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
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.ViewPatientRegistrationRequest;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.ViewPatientRegistrationResponse;
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

	@PostMapping("/view-patient-registration")
	public ResponseEntity<ViewPatientRegistrationResponse> getPatientRegistrationHistory(
			@Valid @RequestBody ViewPatientRegistrationRequest viewPatientRegistrationRequest,
			HttpServletRequest request) {

		logger.info("View Patient Registration request: {} ", viewPatientRegistrationRequest.getPhn());

		Transaction transaction = transactionStart(request, TransactionType.GET_PATIENT_REGISTRATION);
		addAffectedParty(transaction, IdentifierType.PHN, viewPatientRegistrationRequest.getPhn(),
				AffectedPartyDirection.INBOUND);

		try {
			List<PatientRegister> patientRegister = new ArrayList<>();
			
			// Retrieve demographic details
			GetDemographicsConverter converter = new GetDemographicsConverter();
			GetDemographicsRequest demographicsRequest = converter
					.convertRequest(viewPatientRegistrationRequest.getPhn());
			GetDemographicsResponse demoGraphicsResponse = enrollmentService.getDemographics(demographicsRequest,
					transaction);
			GetPersonDetailsResponse personDetailsResponse = converter.convertResponse(demoGraphicsResponse);

			ViewPatientRegistrationResponse response = new ViewPatientRegistrationResponse();
			response.setPersonDetail(personDetailsResponse);

			// Retrieve patient registration history
			patientRegister = patientRegistrationService.getPatientRegistration(
					viewPatientRegistrationRequest.getPayee(), viewPatientRegistrationRequest.getPhn());

			boolean isDiffPayeeOutsideGroup = false;
			boolean isSamePayeeWithinGroup = false;

			// Check if patient registered with different payee within reporting group
			if (patientRegister.size() > 0) {
				isSamePayeeWithinGroup = patientRegister.stream()
						.anyMatch(p -> viewPatientRegistrationRequest.getPayee().contentEquals(p.getPayeeNumber()));

			} else {
				// Check if patient registered with different payee outside reporting group
				List<String> payeeList = patientRegistrationService
						.getPayeeByPHN(viewPatientRegistrationRequest.getPhn());

				// Check if patient record found outside group
				if (patientRegistrationService.getPBFClinicPayeeByPayee(payeeList).size() > 0) {
					patientRegister = patientRegistrationService.getPatientRegistration(payeeList.get(0), viewPatientRegistrationRequest.getPhn());
					isDiffPayeeOutsideGroup = true;
				}

			}

			List<PatientRegisterModel> registrationHistory = convertPatientRegistration(patientRegister);
			response.setPatientRegistrationHistory(registrationHistory);

			handlePatientRegistrationResponse(response, isSamePayeeWithinGroup, isDiffPayeeOutsideGroup);

			ResponseEntity<ViewPatientRegistrationResponse> responseEntity = ResponseEntity.ok(response);

			auditViewPatientRegistrationComplete(transaction, personDetailsResponse);

			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	private ViewPatientRegistrationResponse handlePatientRegistrationResponse(
			ViewPatientRegistrationResponse patientRegistrationResponse, boolean isSamePayeeWithinGroup,
			boolean isDiffPayeeOutsideGroup) {

		Set<String> messages = new HashSet<>();
		patientRegistrationResponse.setStatus(StatusEnum.SUCCESS);
		patientRegistrationResponse.setMessage("Transaction completed successfully");

		GetPersonDetailsResponse personDetailsResponse = patientRegistrationResponse.getPersonDetail();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();

		if (patientRegistrationHistory.size() == 0) {
			// Check if demographics record found
			if (personDetailsResponse.getStatus() == StatusEnum.ERROR) {
				// If no demographics record found, set status as WARNING
				patientRegistrationResponse.setStatus(StatusEnum.WARNING);
				patientRegistrationResponse.setMessage("Patient could not be found in the EMPI or in the PBF. ");
			} else {
				messages.add("No registration information is found in the system for given PHN");
			}
		} else if (!isSamePayeeWithinGroup && !isDiffPayeeOutsideGroup) {
			messages.add("Patient is registered with a different MSP Payee number within the reporting group");
		} else if (isDiffPayeeOutsideGroup) {
			patientRegistrationResponse.setPatientRegistrationHistory(new ArrayList<>());
			messages.add("Patient is registered with a different MSP Payee number outside of reporting group");
		}

		if (personDetailsResponse.getStatus() == StatusEnum.WARNING
				|| personDetailsResponse.getStatus() == StatusEnum.ERROR) {
			patientRegistrationResponse.setAdditionalInfoMessage(personDetailsResponse.getMessage());
		}

		messages.add(personDetailsResponse.getMessage());
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

	private void auditViewPatientRegistrationComplete(Transaction transaction,
			GetPersonDetailsResponse personDetailsResponse) {
		transactionComplete(transaction);
		if (StringUtils.isNotBlank(personDetailsResponse.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, personDetailsResponse.getPhn(),
					AffectedPartyDirection.OUTBOUND);
		}
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
			currentStatus = "Registered";
		} else if (today.compareTo(cancelDate) > 0) {
			currentStatus = "De-Registered";
		} else if (effectiveDate.compareTo(today) > 0) {
			currentStatus = "Future Registration";
		}

		return currentStatus;

	}

}
