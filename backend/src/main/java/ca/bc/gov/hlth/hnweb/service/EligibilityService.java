package ca.bc.gov.hlth.hnweb.service;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

/**
 * Service for Eligibility (E45) requests
 *
 */
@Service
public class EligibilityService {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EligibilityService.class);

	@Autowired
	private Parser parser;
    
	@Autowired
	private WebClient enrollmentWebClient;	
	
	private static final String E45_RESPONSE_ERROR = "MSH|^~\\&|RAIELG-CNFRM|BC00001013|HNCLIENT|moh_hnclient_dev|20211108200334|rajan.reddy|E45|20211108170321|D|2.4||\r\n" 
		+ "MSA|AE|20211108170321|ELIG0001DATE OF SERVICE EXCEEDS SYSTEM LIMITS. MUST BE WITHIN THE LAST 18 MONTHS. CONTACT MSP.\r\n"
		+ "ERR|^^^ELIG0001&DATE OF SERVICE EXCEEDS SYSTEM LIMITS. MUST BE WITHIN THE LAST 18 MONTHS. CONTACT MSP.\r\n"
		+ "QAK|1|AE|E45^^HNET0003\r\n"
		+ "QPD|E45^^HNET0003|1|^^00000010^^^CANBC^XX^MOH|^^00000010^^^CANBC^XX^MOH|^^00000745^^^CANBC^XX^MOH|9390352021^^^CANBC^JHN^MOH||19570713||||||20200505||PVC^^HNET9909~EYE^^HNET9909~PRS^^HNET9909\r\n"
		+ "PID|||9390352021||^^^^^^L||19570713|\r\n"
		+ "IN1|||00000745^^^CANBC^XX^MOH||||||||||||||||||||||";
	
	private static final String E45_RESPONSE_SUCCESS = "MSH|^~\\&|RAIELG-CNFRM|BC00001013|HNCLINET|moh_hnclient_dev|20211108223817|rajan.reddy|E45^^||D|2.4||\r\n" 
	+ 
	"MSA|AA||HJMB001ISUCCESSFULLY COMPLETED\r\n" + 
	"ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n" + 
	"QAK|1|AA|E45^^HNET0471\r\n" + 
	"QPD|E45^^HNET0471|1|^^00000010^^^CANBC^XX^MOH|^^00000010^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9873944324^^^CANBC^JHN^MOH||19780601||||||20210914\r\n" + 
	"PID|||9873944324||TEST^ELIGIBILITY^MOH^^^^L||19780601|M\r\n" + 
	"IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||Y\r\n"
	;
	
	
	private static final String E45_PVC_RESPONSE_SUCCESS = "MSH|^~\\&|RAIELG-CNFRM|BC00001013|HNCLINET|moh_hnclient_dev|20211108232125|rajan.reddy|E45||D|2.4||\r\n" + 
			"MSA|AA||HJMB001ISUCCESSFULLY COMPLETED\r\n" + 
			"ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n" + 
			"QAK|1|AA|E45^^HNET0471\r\n" + 
			"QPD|E45^^HNET0471|1|^^00000010^^^CANBC^XX^MOH|^^00000010^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9873944324^^^CANBC^JHN^MOH||19780601||||||20210914||PVC^^HNET9909||\r\n" + 
			"PID|||9873944324||TEST^ELIGIBILITY^MOH^^^^L||19780601|M\r\n" + 
			"IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||Y\r\n" + 
			"ADJ|1|IN|||PVC^^HNET9908|N\r\n";
	
	private static final String E45_PVC_EYE_PRS_RESPONSE_SUCCESS = "MSH|^~\\&|RAIELG-CNFRM|BC00001013|HNCLINET|moh_hnclient_dev|20211109142135|rajan.reddy|E45^^||D|2.4||\r\n" + 
	"MSA|AA||HJMB001ISUCCESSFULLY COMPLETED\r\n" + 
	"ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n" + 
	"QAK|1|AA|E45^^HNET0471\r\n" + 
	"QPD|E45^^HNET0471|1|^^00000010^^^CANBC^XX^MOH|^^00000010^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9873944324^^^CANBC^JHN^MOH||19780601||||||20210914||PVC^^HNET9909~EYE^^HNET9909~PRS^^HNET9909~ENDRSN^^HNET9909~CCARD^^HNET9909||\r\n" + 
	"PID|||9873944324||TEST^ELIGIBILITY^MOH^^^^L||19780601|M\r\n" + 
	"IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||Y\r\n" + 
	"ADJ|1|IN|||PVC^^HNET9908|N\r\n" + 
	"ADJ|2|IN|||EYE^^HNET9908|20210914\r\n" + 
	"ADJ|3|IN|||PRS^^HNET9908|N\r\n" +
	"ADJ|4|IN|||ABC^^HNET9908|Huh";
	
	/**
	 * TODO (weskubo-cgi) This is just a stubbed out service for unit testing.
	 * The real service will integrated with a downstream application and return
	 * HL7 objects.
	 * @return
	 */
	public CheckEligibilityResponse checkEligibility(String phn, Date eligibilityDate) {
		CheckEligibilityResponse checkEligibilityResponse = new CheckEligibilityResponse();
		checkEligibilityResponse.setBeneficiaryOnDateChecked(Boolean.TRUE);
		checkEligibilityResponse.setCoverageEndDate(DateUtils.addDays(new Date(), 90));
		checkEligibilityResponse.setPhn(phn);
		checkEligibilityResponse.setReason("Some reason or other");
		checkEligibilityResponse.setExclusionPeriodEndDate(DateUtils.addDays(new Date(), 30));
		
		return checkEligibilityResponse;
	}

	public Message checkMspCoverageStatus(E45 e45, String transactionId) throws HL7Exception {		
		
		String e45v2 = encodeE45ToV2(e45);		
		logger.debug("Updated V2 message:\n{}", e45v2);
		
//		TODO (daveb-hni) Send to actual endpoint when it's available, currently response is stubbed.
//		ResponseEntity<String> response = postEnrollmentRequest(e45v2, transactionId);
//		Message message = parseE45v2ToAck(E45_PVC_EYE_PRS_RESPONSE_SUCCESS);
		return parseE45v2ToAck(E45_RESPONSE_ERROR);
	}
	
    private String encodeE45ToV2(E45 e45) throws HL7Exception {
    	
    	// Encode the message using the default HAPI parser and return it as a String
    	String encodedMessage = parser.encode(e45);
    	logger.debug("E45 Encoded Message:\n{}", encodedMessage);
    	
    	return encodedMessage;
    }

    private Message parseE45v2ToAck(String v2) throws HL7Exception {

    	// Parse the V2 String and build a message from it that gets returned
    	
    	Message message = parser.parse(v2.replaceAll("\\|E45\\|", "|E45^^|")); //TODO (daveb-hni) could move this to a custom parser
    	logger.debug("V2 message parsed to {} object", message.getName());
    	
    	return message;
    }
    
}
