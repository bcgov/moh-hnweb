package ca.bc.gov.hlth.hnweb.controller;

import java.io.IOException;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.converter.MSHDefaults;
import ca.bc.gov.hlth.hnweb.converter.R50Converter;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsQuery;
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
	public EnrollSubscriberResponse enrollSubscriber(@Valid @RequestBody EnrollSubscriberRequest enrollSubscriberRequest) throws HL7Exception, IOException {
		
		logger.info("Subscriber enroll request: {} ", enrollSubscriberRequest.getPhn());
		
		R50Converter converter = new R50Converter(mshDefaults);
		R50 r50 = converter.convertRequest(enrollSubscriberRequest);
		String transactionId = UUID.randomUUID().toString();		
		Message r50Message = enrollmentService.enrollSubscriber(r50, transactionId);		
		EnrollSubscriberResponse enrollSubscriberResponse = converter.convertResponse(r50Message);
		
		/*
		 * TODO (daveb-hni) Add the wrapper to the returned response
		 * ResponseEntity<CheckEligibilityResponse> response = ResponseEntity.ok(enrollSubscriberResponse);		 * 		
		 */
		
		logger.info("Subscriber enroll Response: {} ", enrollSubscriberResponse.getAcknowledgementMessage());
		
		return enrollSubscriberResponse;
	}
	
	@PostMapping("/person-details")
	public GetPersonDetailsResponse getDemographicDetails(@Valid @RequestBody GetPersonDetailsQuery requestObj) throws HL7Exception, IOException {
		
		logger.info("Demographic request: {} ", requestObj.getMrn());
			
		String transactionId = UUID.randomUUID().toString();	
		
		GetPersonDetailsResponse personDetails  = enrollmentService.getDemographics(requestObj.getMrn(), transactionId);
		
		logger.info("Get Person Details Response: {} ", personDetails.getMessage().getDetails());
		
		return personDetails;
	}
	
}
