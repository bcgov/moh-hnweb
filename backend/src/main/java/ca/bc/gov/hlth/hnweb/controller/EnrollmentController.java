package ca.bc.gov.hlth.hnweb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
 * <li>Z05
 * <li>Z06
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
		
		Transaction transaction = transactionStart(request, TransactionType.ENROLL_SUBSCRIBER);
		addAffectedParty(transaction, IdentifierType.PHN, enrollSubscriberRequest.getPhn());

		try {
			R50Converter converter = new R50Converter(mshDefaults);
			R50 r50 = converter.convertRequest(enrollSubscriberRequest);
			Message r50Message = enrollmentService.enrollSubscriber(r50, transaction);
			EnrollSubscriberResponse enrollSubscriberResponse = converter.convertResponse(r50Message);
			ResponseEntity<EnrollSubscriberResponse> responseEntity = ResponseEntity.ok(enrollSubscriberResponse);

			logger.info("Subscriber enroll Response: {} ", enrollSubscriberResponse.getMessage());

			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, enrollSubscriberRequest.getPhn());

			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	@PostMapping("/get-person-details")
	public ResponseEntity<GetPersonDetailsResponse> getPersonDetails(@Valid @RequestBody GetPersonDetailsRequest personDetailsRequest, HttpServletRequest request) {
		
		logger.info("Demographic request: {} ", personDetailsRequest.getPhn());

		Transaction transaction = transactionStart(request, TransactionType.ENROLL_SUBSCRIBER);
		addAffectedParty(transaction, IdentifierType.PHN, personDetailsRequest.getPhn());

		try {
			GetDemographicsConverter converter = new GetDemographicsConverter();

			GetDemographicsRequest demographicsRequest = converter.convertRequest(personDetailsRequest.getPhn());
			GetDemographicsResponse demoGraphicsResponse = enrollmentService.getDemographics(demographicsRequest, transaction);
			GetPersonDetailsResponse personDetailsResponse = converter.convertResponse(demoGraphicsResponse);
			ResponseEntity<GetPersonDetailsResponse> responseEntity = ResponseEntity.ok(personDetailsResponse);

			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, personDetailsRequest.getPhn());
			
			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	@PostMapping("/name-search")
	public ResponseEntity<NameSearchResponse> getNameSearch(@Valid @RequestBody NameSearchRequest nameSearchRequest, HttpServletRequest request) {
		
		logger.info("Name Search request: {} ", nameSearchRequest.getGivenName());

		Transaction transaction = transactionStart(request, TransactionType.ENROLL_SUBSCRIBER);
		addAffectedParty(transaction, IdentifierType.PHN, nameSearchRequest.getSurname());

		try {
			FindCandidatesConverter converter = new FindCandidatesConverter();

			FindCandidatesRequest findCandidatesRequest = converter.convertRequest(nameSearchRequest);
			
			FindCandidatesResponse findCandidatesResponse = enrollmentService.findCandidates(findCandidatesRequest, transaction);
			NameSearchResponse nameSearchResponse = converter.convertResponse(findCandidatesResponse);
			ResponseEntity<NameSearchResponse> responseEntity = ResponseEntity.ok(nameSearchResponse);

			transactionComplete(transaction);
			nameSearchResponse.getCandidates().forEach(candidate -> addAffectedParty(transaction, IdentifierType.PHN, candidate.getPhn()));

			return responseEntity;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

}
