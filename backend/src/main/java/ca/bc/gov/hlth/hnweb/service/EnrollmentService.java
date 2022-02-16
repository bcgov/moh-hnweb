package ca.bc.gov.hlth.hnweb.service;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.bc.gov.hlth.hnweb.exception.ExceptionType;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesRequest;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResponse;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.serialization.HL7Config;
import ca.bc.gov.hlth.hnweb.serialization.HL7Serializer;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.bc.gov.hlth.hnweb.util.V3MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;


/**
 * Service for processing enrollment requests 
 *
 */
@Service
public class EnrollmentService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
	private static final String SOURCE_SYSTEM_OVERRIDE = "MOH_CRS";
	private static final String ORGANIZATION = "MOH_CRS";
	public static final String TRANSACTION_ID = "TransactionID";

	@Value("${hibc.r50.path}")
	private String r50Path;

	@Value("${hibc.r50.username}")
	private String r50Username;
	
	@Value("${hibc.r50.password}")
	private String r50Password;

	@Autowired
	private Parser parser;
    	
	@Autowired
	private WebClient hibcWebClient;
	
	@Autowired
	private WebClient hcimWebClient;
	
	
	/**
	 * Enrolls a subscriber by sending a HL7 V2 message to external enrollment service. It was the R50 message to create a V2 String and sends this
	 * over Https using TLS. The endpoint also requires Basic Authentication. 
	 * 
	 * @param r50
	 * @param transaction 
	 * @return
	 * @throws HL7Exception
	 * @throws IOException
	 */
	public Message enrollSubscriber(R50 r50, Transaction transaction) throws HNWebException, HL7Exception{
		logger.debug("Enroll subscriber for Message ID [{}]; Transaction ID [{}]", r50.getMSH().getMsh10_MessageControlID(), transaction.getTransactionId().toString());
		
		String r50v2RequiredFormat = formatMessage(r50);
		logger.debug("Updated V2 message:\n{}", r50v2RequiredFormat);
		
		messageSent(transaction, V2MessageUtil.getMessageID(r50));
		ResponseEntity<String> response = postHibcRequest(r50Path, r50Username, r50Password, r50v2RequiredFormat);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
	
		Message v2Response = parseR50v2ToAck(response.getBody());
    	messageReceived(transaction, V2MessageUtil.getMessageID(v2Response));
				
		return v2Response;
	}
	
	 
	 /**
	 * Gets the demographic details by sending a V3 message to external endpoint.
	 * Calls HL7Serializer api to serialize and deserialize request/response
	 * @param demographicsRequest
	 * @param transaction 
	 * @return
	 * @throws HNWebException
	 */
	public GetDemographicsResponse getDemographics(GetDemographicsRequest demographicsRequest, Transaction transaction)
	      throws HNWebException {
		
		HL7Serializer hl7Serializer = new HL7Serializer(new HL7Config());
		UserInfo userInfo = SecurityUtil.loadUserInfo();
		MessageMetaData mmd = new MessageMetaData(userInfo.getUsername(), SOURCE_SYSTEM_OVERRIDE, ORGANIZATION, transaction.getTransactionId().toString());
		
		//Serialize request object
		Object formattedRequest = hl7Serializer.toXml(demographicsRequest, mmd);
		//Create soap wrapper
		String xmlString = V3MessageUtil.wrap(formattedRequest.toString());
		logger.debug("Get Demographics wrapped xml request[{}]", xmlString);
		  
		messageSent(transaction, mmd.getMessageIdExt());
	    ResponseEntity<String> response = postHcimRequest(xmlString, transaction.getTransactionId().toString()); 
	    logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
	    
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		//De-Serialize demographics response
		GetDemographicsResponse getDemographicsResponse = hl7Serializer.fromXml(response.getBody(), GetDemographicsResponse.class);
    	messageReceived(transaction, getDemographicsResponse.getMessageIdExtension());
	    
	    return getDemographicsResponse;
	
	 
	 }
	
	 /**
	 * Finds the candidates details by sending a V3 message to external endpoint.
	 * Calls HL7Serializer api to serialize and deserialize request/response
	 * @param findCandidatesRequest
	 * @param transaction 
	 * @return
	 * @throws HNWebException
	 */
	public FindCandidatesResponse findCandidates(FindCandidatesRequest findCandidatesRequest, Transaction transaction)
	      throws HNWebException {
		
		HL7Serializer hl7Serializer = new HL7Serializer(new HL7Config());
		UserInfo userInfo = SecurityUtil.loadUserInfo();
		MessageMetaData mmd = new MessageMetaData(userInfo.getUsername(), SOURCE_SYSTEM_OVERRIDE, ORGANIZATION, transaction.getTransactionId().toString());
		
		//Serialize request object
		Object formattedRequest = hl7Serializer.toXml(findCandidatesRequest, mmd);
		//Create soap wrapper
		String xmlString = V3MessageUtil.wrap(formattedRequest.toString());
		logger.debug("Get Name Search wrapped xml request[{}]", xmlString);
		  
		messageSent(transaction, mmd.getMessageIdExt());
	    ResponseEntity<String> response = postHcimRequest(xmlString, transaction.getTransactionId().toString()); 
	    logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
	    
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		//De-Serialize findCandidate response
		FindCandidatesResponse findCandidatesResponse = hl7Serializer.fromXml(response.getBody(), FindCandidatesResponse.class);
    	messageReceived(transaction, findCandidatesResponse.getMessageIdExtension());
	    
	    return findCandidatesResponse;
	
	 
	 }
	  
	private ResponseEntity<String> postHcimRequest(String data, String transactionId) {
		return hcimWebClient
	             .post()
	             .contentType(MediaType.TEXT_XML)
	             .header(TRANSACTION_ID, transactionId)
	             .bodyValue(data)
	             .retrieve()
	             .toEntity(String.class)
	             .block();
	    	
	}
	
	private ResponseEntity<String> postHibcRequest(String path, String username, String password, String data) {
        return hibcWebClient
                .post()
                .uri(path)
                .contentType(MediaType.TEXT_PLAIN)
                .header(TRANSACTION_ID, UUID.randomUUID().toString())
                .headers(header -> header.setBasicAuth(username, password))
                .bodyValue(data)
                .retrieve()
                .toEntity(String.class)
                .block();
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
	private String formatMessage(R50 r50) throws HL7Exception  {
		
		// Encode the message using the default HAPI parser and return it as a String
    	String encodedMessage = parser.encode(r50);
    	logger.debug("R50 Encoded Message:\n{}", encodedMessage);
    	
		return  encodedMessage.replaceFirst("\r", "||\r");
	}
	

	
}
