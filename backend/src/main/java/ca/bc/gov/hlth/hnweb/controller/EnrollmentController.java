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
import ca.bc.gov.hlth.hnweb.converter.hl7v2.R50Converter;
import ca.bc.gov.hlth.hnweb.converter.hl7v3.FindCandidatesConverter;
import ca.bc.gov.hlth.hnweb.converter.hl7v3.GetDemographicsConverter;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesRequest;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResponse;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;
import ca.uhn.hl7v2.model.Message;

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
@RequestMapping("/enrollment")
@RestController
public class EnrollmentController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private MSHDefaults mshDefaults;

	@PostMapping("/enroll-subscriber")
	public ResponseEntity<EnrollSubscriberResponse> enrollSubscriber(@Valid @RequestBody EnrollSubscriberRequest enrollSubscriberRequest, HttpServletRequest request) {

		logger.info("Subscriber enroll request: {} ", enrollSubscriberRequest.getPhn());
		
		Transaction transaction = auditEnrollSubscriberStart(enrollSubscriberRequest, request);

		try {
			R50Converter converter = new R50Converter(mshDefaults);
			R50 r50 = converter.convertRequest(enrollSubscriberRequest);
			Message r50Message = enrollmentService.enrollSubscriber(r50, transaction);
			EnrollSubscriberResponse enrollSubscriberResponse = converter.convertResponse(r50Message);
			ResponseEntity<EnrollSubscriberResponse> responseEntity = ResponseEntity.ok(enrollSubscriberResponse);

			logger.info("Subscriber enroll Response: {} ", enrollSubscriberResponse.getMessage());

			auditEnrollSubscriberComplete(transaction, enrollSubscriberResponse);

			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	@PostMapping("/get-person-details")
	public ResponseEntity<GetPersonDetailsResponse> getPersonDetails(@Valid @RequestBody GetPersonDetailsRequest personDetailsRequest, HttpServletRequest request) {
		
		logger.info("Demographic request: {} ", personDetailsRequest.getPhn());

		Transaction transaction = transactionStart(request, TransactionType.GET_PERSON_DETAILS);
		addAffectedParty(transaction, IdentifierType.PHN, personDetailsRequest.getPhn(), AffectedPartyDirection.INBOUND);

		try {
			GetDemographicsConverter converter = new GetDemographicsConverter();
			GetDemographicsRequest demographicsRequest = converter.convertRequest(personDetailsRequest.getPhn());
			GetDemographicsResponse demoGraphicsResponse = enrollmentService.getDemographics(demographicsRequest, transaction);
			GetPersonDetailsResponse personDetailsResponse = converter.convertResponse(demoGraphicsResponse);
			ResponseEntity<GetPersonDetailsResponse> responseEntity = ResponseEntity.ok(personDetailsResponse);

			auditGetPersonSearchComplete(transaction, personDetailsResponse);
			
			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	@PostMapping("/name-search")
	public ResponseEntity<NameSearchResponse> getNameSearch(@Valid @RequestBody NameSearchRequest nameSearchRequest, HttpServletRequest request) {
		
		logger.info("Name Search request: {} ", nameSearchRequest.getGivenName());

		Transaction transaction = transactionStart(request, TransactionType.NAME_SEARCH);

		try {
			FindCandidatesConverter converter = new FindCandidatesConverter();
			FindCandidatesRequest findCandidatesRequest = converter.convertRequest(nameSearchRequest);
			FindCandidatesResponse findCandidatesResponse = enrollmentService.findCandidates(findCandidatesRequest, transaction);
			NameSearchResponse nameSearchResponse = converter.convertResponse(findCandidatesResponse);
			ResponseEntity<NameSearchResponse> responseEntity = ResponseEntity.ok(nameSearchResponse);

			auditGetNameSearchComplete(transaction, nameSearchResponse);

			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	private Transaction auditEnrollSubscriberStart(EnrollSubscriberRequest enrollSubscriberRequest,	HttpServletRequest request) {
		
		Transaction transaction = transactionStart(request, TransactionType.ENROLL_SUBSCRIBER);		
		//Some requests do not contain the PHN e.g R50 z05 as it is Enroll subscriber without PHN
		if (StringUtils.isNotBlank(enrollSubscriberRequest.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, enrollSubscriberRequest.getPhn(), AffectedPartyDirection.INBOUND);
		}
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, enrollSubscriberRequest.getGroupNumber(), AffectedPartyDirection.INBOUND);
		if (StringUtils.isNotBlank(enrollSubscriberRequest.getGroupMemberNumber())) {
			addAffectedParty(transaction, IdentifierType.GROUP_MEMBER_NUMBER, enrollSubscriberRequest.getGroupMemberNumber(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(enrollSubscriberRequest.getDepartmentNumber())) {
			addAffectedParty(transaction, IdentifierType.DEPARTMENT_NUMBER, enrollSubscriberRequest.getDepartmentNumber(), AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}

	private void auditEnrollSubscriberComplete(Transaction transaction,	EnrollSubscriberResponse enrollSubscriberResponse) {
		
		transactionComplete(transaction);
		//Some responses do not contain the PHN e.g. in the case of R50 z06 it is just an ACK
		if (StringUtils.isNotBlank(enrollSubscriberResponse.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, enrollSubscriberResponse.getPhn(), AffectedPartyDirection.OUTBOUND);
		}
	}

	private void auditGetPersonSearchComplete(Transaction transaction, GetPersonDetailsResponse personDetailsResponse) {
		transactionComplete(transaction);
		if (StringUtils.isNotBlank(personDetailsResponse.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, personDetailsResponse.getPhn(), AffectedPartyDirection.OUTBOUND);
		}
	}

	private void auditGetNameSearchComplete(Transaction transaction, NameSearchResponse nameSearchResponse) {
		transactionComplete(transaction);
		if (nameSearchResponse.getCandidates() != null) {
			nameSearchResponse.getCandidates().forEach(candidate -> addAffectedParty(transaction, IdentifierType.PHN, candidate.getPhn(), AffectedPartyDirection.OUTBOUND));
		}
	}

}
