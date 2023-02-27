package ca.bc.gov.hlth.hnweb.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.converter.hl7v3.GetDemographicsConverter;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegisterModel;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegistrationRequest;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegistrationResponse;
import ca.bc.gov.hlth.hnweb.model.rest.pbf.PayeeStatus;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.BcscPayeeMapping;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.service.BcscPayeeMappingService;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;
import ca.bc.gov.hlth.hnweb.service.PBFClinicPayeeService;
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

	private static final String DATE_FORMAT = "yyyyMMdd";
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER_yyyyMMdd = DateTimeFormatter.ofPattern(DATE_FORMAT);

	private static final Logger logger = LoggerFactory.getLogger(PatientRegistrationController.class);

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private PatientRegistrationService patientRegistrationService;
	
	@Autowired
	private BcscPayeeMappingService bcscPayeeMappingService;	

    @Autowired
    private PBFClinicPayeeService pbfClinicPayeeService; 
    
    @PostMapping("/get-patient-registration")
	public ResponseEntity<PatientRegistrationResponse> getPatientRegistration(
			@Valid @RequestBody PatientRegistrationRequest patientRegistrationRequest, HttpServletRequest request) {

		logger.info("Patient Registration request: {} ", patientRegistrationRequest.getPhn());

		Transaction transaction = transactionStart(request, TransactionType.GET_PATIENT_REGISTRATION);
		addAffectedParty(transaction, IdentifierType.PHN, patientRegistrationRequest.getPhn(),
				AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PAYEE_NUMBER, patientRegistrationRequest.getPayee(),
				AffectedPartyDirection.INBOUND);

		try {
			validatePayeeNumberMapping(patientRegistrationRequest);
			
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

			String registrationMessage = patientRegistrationService.checkRegistrationDetails(registrationRecords,
					patientRegistrationRequest.getPayee(), patientRegistrationRequest.getPhn());

			if (!registrationRecords.isEmpty() || StringUtils.isNotBlank(registrationMessage)) {
				patientRegistrationExist = true;

			}

			List<PatientRegisterModel> registrationHistory = convertPatientRegistration(registrationRecords);
			response.setPatientRegistrationHistory(registrationHistory);

			handlePatientRegistrationResponse(response, patientRegistrationExist, registrationMessage);

			ResponseEntity<PatientRegistrationResponse> responseEntity = ResponseEntity.ok(response);

			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, personDetailsResponse.getPhn(), AffectedPartyDirection.OUTBOUND);
			registrationRecords.forEach(record -> {
				addAffectedParty(transaction, IdentifierType.PAYEE_NUMBER, record.getPayeeNumber(), AffectedPartyDirection.OUTBOUND);
				addAffectedParty(transaction, IdentifierType.PRACTITIONER_NUMBER, record.getRegisteredPractitionerNumber(), AffectedPartyDirection.OUTBOUND);
			});

			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	/**
	 * The Payee number submitted in the request must match the Payee Number mapped to the current user in the BCSC to Payee Number mappings.
	 * 
	 * @param patientRegistrationRequest
	 * @throws HNWebException
	 */
	private void validatePayeeNumberMapping(PatientRegistrationRequest patientRegistrationRequest) {
		UserInfo userInfo = SecurityUtil.loadUserInfo();
		Optional<BcscPayeeMapping> bcscPayeeMappingOptional = bcscPayeeMappingService.find(userInfo.getUserId());
		if (bcscPayeeMappingOptional.isEmpty()) {
			logger.error("No Payee Number mapping was found for the current user");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Payee Number mapping was found for the current user");
		}
		String mappedPayeeNumber = bcscPayeeMappingOptional.get().getPayeeNumber();
        String requestPayeeNumber = patientRegistrationRequest.getPayee();
        if (!StringUtils.equals(requestPayeeNumber, mappedPayeeNumber)) {
			logger.error("Payee field value {} does not match the Payee Number mapped to this user", requestPayeeNumber);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Payee field value %s does not match the Payee Number mapped to this user", requestPayeeNumber));
		}
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus(mappedPayeeNumber);
        if (PayeeStatus.ACTIVE != payeeStatus) {
            logger.error("Payee {} is not Active as their status is {}", requestPayeeNumber, payeeStatus);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Payee %s is not Active as their status is %s", requestPayeeNumber, payeeStatus));
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

			model.setEffectiveDate(formatDate(patientRegistration.getEffectiveDate()));
			model.setCancelDate(formatDate(patientRegistration.getCancelDate()));
			model.setCurrentStatus(setPatientRegistrationStatus(patientRegistration.getCancelDate(),
					patientRegistration.getEffectiveDate()));
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
			model.setRegisteredPractitionerFirstName(patientRegistration.getRegisteredPractitionerFirstName());
			model.setRegisteredPractitionerMiddleName(patientRegistration.getRegisteredPractitionerMiddleName());
			model.setRegisteredPractitionerSurname(patientRegistration.getRegisteredPractitionerSurname());
			model.setPhn(patientRegistration.getPhn());

			pateintRegisterModels.add(model);

		});
		return pateintRegisterModels;

	}

	private String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		LocalDate localDate = convertDate(date);
		return localDate.format(DATE_TIME_FORMATTER_yyyyMMdd);
	}

	private LocalDate convertDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private String setPatientRegistrationStatus(Date cancelDate, Date effectiveDate) {
		String currentStatus = "";
		LocalDate today = LocalDate.now();
		LocalDate convertedEffectiveDate = convertDate(effectiveDate);
		
		LocalDate convertedCancelDate = null;
		if (cancelDate != null) {
			convertedCancelDate = convertDate(cancelDate);
		} else {
			// Default the cancelDate to end of time to simplify logic
			convertedCancelDate = LocalDate.of(9999, 12, 31);
		}

		// “Registered” when the current date is greater than or equal to the effective
		// date and less than or equal to the cancel date.
		// “De-Registered” when the current date is later than the registration cancel
		// date
		// “Future Registration” when the registration date is in the future

		if (today.compareTo(convertedEffectiveDate) >= 0 && today.compareTo(convertedCancelDate) <= 0) {
			currentStatus = REGISTERED;
		} else if (today.compareTo(convertedCancelDate) > 0) {
			currentStatus = DE_REGISTERED;
		} else if (convertedEffectiveDate.compareTo(today) > 0) {
			currentStatus = FUTURE_REGISTRATION;
		}

		return currentStatus;

	}

}
