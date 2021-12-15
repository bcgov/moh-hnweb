package ca.bc.gov.hlth.hnweb.controller;

import javax.validation.Valid;

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

import ca.bc.gov.hlth.hnweb.converter.MSHDefaults;
import ca.bc.gov.hlth.hnweb.converter.hl7v2.R50Converter;
import ca.bc.gov.hlth.hnweb.converter.hl7v3.FindCandidatesConverter;
import ca.bc.gov.hlth.hnweb.converter.hl7v3.GetDemographicsConverter;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.GetNameSearchRequest;
import ca.bc.gov.hlth.hnweb.model.GetNameSearchResponse;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesRequest;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResponse;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;
import ca.uhn.hl7v2.HL7Exception;
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
public class EnrollmentController {

	private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private MSHDefaults mshDefaults;

	@PostMapping("/enroll-subscriber")
	public ResponseEntity<EnrollSubscriberResponse> enrollSubscriber(
			@Valid @RequestBody EnrollSubscriberRequest enrollSubscriberRequest) throws HL7Exception, HNWebException {

		logger.info("Subscriber enroll request: {} ", enrollSubscriberRequest.getPhn());
		try {
			R50Converter converter = new R50Converter(mshDefaults);
			R50 r50 = converter.convertRequest(enrollSubscriberRequest);
			Message r50Message = enrollmentService.enrollSubscriber(r50);
			EnrollSubscriberResponse enrollSubscriberResponse = converter.convertResponse(r50Message);
			ResponseEntity<EnrollSubscriberResponse> responseEntity = ResponseEntity.ok(enrollSubscriberResponse);

			logger.info("Subscriber enroll Response: {} ", enrollSubscriberResponse.getAcknowledgementMessage());

			return responseEntity;
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /enroll subscriber request", hwe);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /enroll subscriber request", e);
		}
	}

	@PostMapping("/get-person-details")
	public ResponseEntity<GetPersonDetailsResponse> getPersonDetails(
			@Valid @RequestBody GetPersonDetailsRequest personDetails) {
		logger.info("Demographic request: {} ", personDetails.getPhn());

		try {
			GetDemographicsConverter converter = new GetDemographicsConverter();

			GetDemographicsRequest demographicsRequest = converter.convertRequest(personDetails.getPhn());
			GetDemographicsResponse demoGraphicsResponse = enrollmentService.getDemographics(demographicsRequest);
			GetPersonDetailsResponse personDetailsResponse = converter.convertResponse(demoGraphicsResponse);
			ResponseEntity<GetPersonDetailsResponse> responseEntity = ResponseEntity.ok(personDetailsResponse);

			return responseEntity;
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /person-details request", hwe);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /person-details request", e);
		}
	}

	@PostMapping("/get-name-search")
	public ResponseEntity<GetNameSearchResponse> getNameSearch(
			@Valid @RequestBody GetNameSearchRequest getNameSearchRequest) {
		logger.info("Name Search request: {} ", getNameSearchRequest.getGivenName());

		try {
			FindCandidatesConverter converter = new FindCandidatesConverter();

			FindCandidatesRequest findCandidatesRequest = converter.convertRequest(getNameSearchRequest.getSurname(),
					getNameSearchRequest.getGivenName(), getNameSearchRequest.getSecondName(),
					getNameSearchRequest.getDateOfBirth(), getNameSearchRequest.getGender());
			
			FindCandidatesResponse findCandidatesResponse = enrollmentService.findCandidates(findCandidatesRequest);
			GetNameSearchResponse getNameSearchResponse = converter.convertResponse(findCandidatesResponse);
			ResponseEntity<GetNameSearchResponse> responseEntity = ResponseEntity.ok(getNameSearchResponse);

			return responseEntity;
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /person-details request", hwe);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /person-details request", e);
		}
	}

}
