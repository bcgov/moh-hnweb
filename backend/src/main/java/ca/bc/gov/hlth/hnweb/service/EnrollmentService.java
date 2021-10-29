package ca.bc.gov.hlth.hnweb.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

/**
 * Service for processing enrollment requests 
 *
 */
@Service
public class EnrollmentService {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EnrollmentService.class);

	@Autowired
	private Parser parser;
	
	public Message enrollSubscriber(R50 r50) throws HL7Exception, IOException {
		
		logger.debug("Enroll subscriber");

		String r50v2 = encodeR50ToV2(r50);
		
	    //TODO (daveb-hni) Add the code to send to external endpoint
		
		//Return stubbed response.
		return parseR50v2ToAck("MSH|^~\\&|RAIPRSN-NM-SRCH|BC00002041|HNWeb|moh_hnclient_dev|20211013124847.746-0700||ACK|71902|D|2.4\r\n" + 
				"MSA|AE|20191108082211|NHR529E^SEVERE SYSTEM ERROR\r\n" + 
				"ERR|^^^NHR529E");
	}
		
    private String encodeR50ToV2(R50 r50) throws HL7Exception, IOException {
    	
    	// Encode the message using the default HAPI parser and return it as a String
    	String encodedMessage = parser.encode(r50);
    	logger.debug("R50 Encoded Message: {}", encodedMessage);
    	
    	return encodedMessage;
    }

    private Message parseR50v2ToAck(String v2) throws HL7Exception {

    	// Parse the V2 String and build a message from it that gets returned  
    	Message message = parser.parse(v2);
    	logger.debug("V2 message parsed to {} object", message.getName());
    	
    	return message;
    }
    
}
