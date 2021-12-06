package ca.bc.gov.hlth.hnweb.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.converter.XmlConverter;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.converter.MSHDefaults;
import ca.bc.gov.hlth.hnweb.converter.R50Converter;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.R50Response;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
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

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EnrollmentController.class);

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
			String transactionId = UUID.randomUUID().toString();
			Message r50Message = enrollmentService.enrollSubscriber(r50, transactionId);
			R50Response r50Response = converter.convertResponse(r50Message);
			EnrollSubscriberResponse enrollSubscriberResponse = converter.buildEnrollSubscribeResponse(r50Response);
			ResponseEntity<EnrollSubscriberResponse> responseEntity = ResponseEntity.ok(enrollSubscriberResponse);

			logger.info("Subscriber enroll Response: {} ", r50Response.getAcknowledgementMessage());

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

	@PostMapping("/person-details")
	public ResponseEntity<GetPersonDetailsResponse> getDemographicDetails(
			@Valid @RequestBody GetPersonDetailsRequest requestObj) {
		logger.info("Demographic request: {} ", requestObj.getPhn());

		try {
			String transactionId = UUID.randomUUID().toString();
			XmlConverter converter = new XmlConverter(transactionId);

			String xmlString = converter.convertRequest(requestObj.getPhn());

			ResponseEntity<String> demoGraphicsResponse = enrollmentService.getDemographics(xmlString, transactionId);
			GetPersonDetailsResponse personDetails = converter.convertResponse(demoGraphicsResponse.getBody());
			ResponseEntity<GetPersonDetailsResponse> responseEntity = ResponseEntity.ok(personDetails);

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
