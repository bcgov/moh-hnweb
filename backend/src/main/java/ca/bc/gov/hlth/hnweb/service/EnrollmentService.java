package ca.bc.gov.hlth.hnweb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.bc.gov.hlth.hnweb.config.HL7Config;
import ca.bc.gov.hlth.hnweb.model.GetDemographicsQuery;
import ca.bc.gov.hlth.hnweb.model.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.serialization.HL7Serializer;
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

	public static final String TRANSACTION_ID = "TransactionID";

	@Autowired
	private Parser parser;
    
	@Autowired
	private WebClient enrollmentWebClient;
	
	@Autowired
	private WebClient hcimWebClient;
	
	
	/**
	 * Enrolls a subscriber by sending a HL7 V2 message to external enrollment service. It was the R50 message to create a V2 String and sends this
	 * over Https using TLS. The endpoint also requires Basic Authentication. 
	 * 
	 * @param r50
	 * @param transactionId
	 * @return
	 * @throws HL7Exception
	 * @throws IOException
	 */
	public Message enrollSubscriber(R50 r50, String transactionId) throws HL7Exception, IOException {
		
		logger.debug("Enroll subscriber for Message ID [{}]; Transaction ID [{}]", r50.getMSH().getMsh10_MessageControlID(), transactionId);

		String r50v2 = encodeR50ToV2(r50);		
		String r50v2RequiredFormat = formatMessage(r50v2);
		logger.debug("Updated V2 message:\n{}", r50v2RequiredFormat);
		
		ResponseEntity<String> response = postEnrollmentRequest(r50v2RequiredFormat, transactionId);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		return parseR50v2ToAck(response.getBody());
	}
	
	  /**
	   * Retrieves GetDemographicsResponse object based on a unique identifier
	   *
	   * @param params GetDemographicsQuery - Query parameter
	   * @param mmd MessageMetaData - Meta Data for message
	   * @return GetDemographicsResponse
	   * @throws HL7Exception
	   */
	  public GetDemographicsResponse getDemographics(GetDemographicsQuery reqObj, MessageMetaData mmd, String transactionId)
	      throws HL7Exception, IOException {
		  
		logger.debug("Get Demographics details for phn [{}]; Transaction ID [{}]", reqObj.getMrn(), transactionId);
		
		HL7Serializer hl7 = new HL7Serializer(new HL7Config());
		
	    Object request = hl7.toXml(reqObj, mmd);
	    String historyRequest = request.toString();
	
	    ResponseEntity<String> getDemoResponse = postDemographicRequest(historyRequest, transactionId);
	    logger.info(getDemoResponse.toString());
	    GetDemographicsResponse results = hl7.fromXml(getDemoResponse.getBody().toString(), GetDemographicsResponse.class);

	     if (results.getResultCount() == 0) {
	    	  logger.debug("Error performing get demographics");
	      }
	    return results;
	 
	  }
	  
	private ResponseEntity<String> postDemographicRequest(String data, String transactionId) {
		   return hcimWebClient
	                .post()
	                .contentType(MediaType.TEXT_XML)
	                .header(TRANSACTION_ID, transactionId)
	                .bodyValue(data)
	                .retrieve()
	                .toEntity(String.class)
	                .block();
	    	
	}
	
	private ResponseEntity<String> postEnrollmentRequest(String data, String transactionId) {
        return enrollmentWebClient
                .post()
                .contentType(MediaType.TEXT_PLAIN)
                .header(TRANSACTION_ID, transactionId)
                .bodyValue(data)
                .retrieve()
                .toEntity(String.class)
                .block();
    }	
	 
	
    private String encodeR50ToV2(R50 r50) throws HL7Exception {
    	
    	// Encode the message using the default HAPI parser and return it as a String
    	String encodedMessage = parser.encode(r50);
    	logger.debug("R50 Encoded Message:\n{}", encodedMessage);
    	
    	return encodedMessage;
    }

    private Message parseR50v2ToAck(String v2) throws HL7Exception {

    	// Parse the V2 String and build a message from it that gets returned  
    	Message message = parser.parse(v2);
    	logger.debug("V2 message parsed to {} object", message.getName());
    	
    	return message;
    }
    
	/**
	 * TODO (daveb-hni) The message is currently being adjusted to avoid an error in the endpoint. Confirm if this is needed when a response is received to the email that has been sent to the endpoint creator. 
	 * The endpoint currently expects the message in slightly different format to the HL7 V2 2.4 spec. The difference in the messages is that there is an empty field expected 
	 * at the end of the MSH Segment after the Version field. e.g.
	 * MSH|^~\\&|HNWeb|BC01000030|RAIENROL-EMP|BC00001013|20200529114230|10-TEST|R50^Z06|20200529114230|D|2.4||

		versus the spec version

	 * MSH|^~\\&|HNWeb|BC01000030|RAIENROL-EMP|BC00001013|20200529114230|10-ANother|R50^Z06|20200529114230|D|2.4
	 * @param r50v2
	 * @return
	 */
	private String formatMessage(String r50v2) {
		return  r50v2.replaceFirst("\r", "||\r");
	}
	
}
