package ca.bc.gov.hlth.hnweb.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;
import ca.bc.gov.hlth.hnweb.v2.V2MessageUtils;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

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
@RestController
public class EnrollmentController {
	
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EnrollmentController.class);

	@Autowired
	private EnrollmentService enrollmentService;
	
	@Autowired
	private Parser parser;
	
	@PutMapping("/enrollsubscriber")
	public EnrollSubscriberResponse enrollSubscriber(@RequestParam(name = "enrollSubscriberRequest", required = false) EnrollSubscriberRequest enrollSubscriberRequest) throws HL7Exception, IOException {
		
		LOGGER.info("Subscriber enroll request: {} ", enrollSubscriberRequest.getPhn());
		
		//Convert request to R50 message and get it as HL7 v2 String. (This message is used to send to external endpoint wrapped in JSON)
		R50 r50 = convertEnrollSubscriberRequestToR50(enrollSubscriberRequest);
		String r50v2 = encodeR50ToV2(r50);
		
		//Send to R50 endpoint
		String v2Response = enrollmentService.enrollSubscriber(r50v2);
		Message message = parseR50v2ToAck(v2Response);
		
		//Convert R50 endpoint response to HN Web response
		EnrollSubscriberResponse enrollSubscriberResponse = convertAckToEnrollSubscriberResponse(message);
		
		LOGGER.info("Subscriber enroll Response: {} ", enrollSubscriberResponse.getAcknowledgementMessage());
		
		return enrollSubscriberResponse;
	}
	
    private R50 convertEnrollSubscriberRequestToR50(EnrollSubscriberRequest enrollSubscriberRequest) throws HL7Exception, IOException {
    	
    	//Create a default R50 message with MSH-9 set to R50 Z06 
    	R50 r50 = new R50();    	 
    	r50.initQuickstart("R50", "Z06", "P");

    	setSegmentValues(r50, enrollSubscriberRequest);
    	     	
    	return r50;
    	
    }

    private String encodeR50ToV2(R50 r50) throws HL7Exception, IOException {
    	
    	// Encode the message using the default HAPI parser and return it as a String
    	String encodedMessage = parser.encode(r50);
    	LOGGER.debug("R50 Encoded Message: {}", encodedMessage);
    	
    	return encodedMessage;
    }

    private Message parseR50v2ToAck(String v2) throws HL7Exception {

    	// Parse the V2 String and build a message from it that gets returned  
    	Message message = parser.parse(v2);
    	LOGGER.debug("V2 message parsed to {} object", message.getName());
    	
    	return message;
    }
    
    private EnrollSubscriberResponse convertAckToEnrollSubscriberResponse(Message message) throws HL7Exception {
    	
    	EnrollSubscriberResponse enrollSubscriberResponse = new EnrollSubscriberResponse();
    	
    	// Use a Terser to access the message info
    	Terser terser = new Terser(message);
    	          
    	/* 
    	 * Retrieve required info by specifying the location
    	 */ 
    	String messageId = terser.get("/.MSH-10-1");
    	String acknowledgementCode = terser.get("/.MSA-1-1");
    	String acknowledgementMessage = terser.get("/.MSA-3-1");
    	
    	enrollSubscriberResponse.setMessageId(messageId);
    	enrollSubscriberResponse.setAcknowledgementCode(acknowledgementCode);
		enrollSubscriberResponse.setAcknowledgementMessage(acknowledgementMessage);
    	
    	return enrollSubscriberResponse;
    }
    
   	private void setSegmentValues(R50 r50, EnrollSubscriberRequest enrollSubscriberRequest) throws HL7Exception {
   		
   		//TODO This has been stubbed for now. Populate actual values from correct source.
    	V2MessageUtils.setMshValues(r50.getMSH(), "HNWeb", "BC01000030", "RAIENROL-EMP", "BC00001013", "20200529114230", "10-ANother", "20200529114230", "D", "2.4");
		V2MessageUtils.setZhdValues(r50.getZHD(), "20200529114230", "00000010", "HNAIADMINISTRATION", "2.4");
    	V2MessageUtils.setPidValues(r50.getPID(), enrollSubscriberRequest.getPhn(), "BC", "PH", "", "19700303", "M");   	  
    	V2MessageUtils.setZiaValues(r50.getZIA(), "20210101", "HELP^RERE^^^^^L", 
    											  "898 RETER ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^H", 
    										      "123 UIYUI ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^M", 
    										      "^PRN^PH^^^250^8578974", "S", "AB");
    	V2MessageUtils.setIn1Values(r50.getIN1(), "6337109", "789446", "123456", "20190501", "20201231");
    	V2MessageUtils.setZihValues(r50.getZIH(), "D");        	
    	V2MessageUtils.setZikValues(r50.getZIK(), "20210101", "20221231");    	
    }

}
