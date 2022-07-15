package ca.bc.gov.hlth.hnweb.controller;

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

import ca.bc.gov.hlth.hnweb.converter.hl7v2.MSHDefaults;
import ca.bc.gov.hlth.hnweb.converter.hl7v3.GetDemographicsConverter;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegistrationResponse;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;

/**
 * Handles request related to R50 Enroll Subscriber. These will include:
 * <ul>
 * <li>Z03
 * <li>Z04
 * <li>Z05 Enroll Visa Subscriber without PHN
 * <li>Z06 Enroll Visa Subscriber with PHN
 * <ul>
 *
 */
@RequestMapping("/patient-registration")
@RestController
public class PBFController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PBFController.class);

	@Autowired
	private EnrollmentService enrollmentService;



	@PostMapping("/get-registration-history")
	public ResponseEntity<PatientRegistrationResponse> getR(@Valid @RequestBody GetPersonDetailsRequest personDetailsRequest, HttpServletRequest request) {
		
		logger.info("Demographic request: {} ", personDetailsRequest.getPhn());

		Transaction transaction = transactionStart(request, TransactionType.GET_PERSON_DETAILS);
		addAffectedParty(transaction, IdentifierType.PHN, personDetailsRequest.getPhn(), AffectedPartyDirection.INBOUND);

		try {
			GetDemographicsConverter converter = new GetDemographicsConverter();
			GetDemographicsRequest demographicsRequest = converter.convertRequest(personDetailsRequest.getPhn());
			GetDemographicsResponse demoGraphicsResponse = enrollmentService.getDemographics(demographicsRequest, transaction);
			GetPersonDetailsResponse personDetailsResponse = converter.convertResponse(demoGraphicsResponse);
			PatientRegistrationResponse response = new PatientRegistrationResponse();
			response.setGivenName(personDetailsResponse.getGivenName());
			response.setSurname(personDetailsResponse.getSurname());
			response.setDateOfBirth(personDetailsResponse.getDateOfBirth());
			response.setPhn(personDetailsResponse.getPhn());
			response.setGender(personDetailsResponse.getGender());
			
			ResponseEntity<PatientRegistrationResponse> responseEntity = ResponseEntity.ok(response);

			auditGetPersonSearchComplete(transaction, personDetailsResponse);
			
			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	private void auditGetPersonSearchComplete(Transaction transaction, GetPersonDetailsResponse personDetailsResponse) {
		transactionComplete(transaction);
		if (StringUtils.isNotBlank(personDetailsResponse.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, personDetailsResponse.getPhn(), AffectedPartyDirection.OUTBOUND);
		}
	}

	
}
