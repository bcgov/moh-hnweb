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
import ca.bc.gov.hlth.hnweb.model.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
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
	private static final String dataEntererExt = "";
	private static final String sourceSystemOverride = "HOOPC";
	private static final String organization = "BCHCIM";
	private static final String mrn_source = "MOH_CRS";

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
	  public GetPersonDetailsResponse getDemographics(String phn, String transactionId)
	      throws HL7Exception, IOException {
		  
		logger.debug("Get Demographics details for PHN [{}]; Transaction ID [{}]", phn, transactionId);
		
		HL7Serializer hl7 = new HL7Serializer(new HL7Config());
		MessageMetaData mmd = new MessageMetaData(dataEntererExt, sourceSystemOverride, organization);
		GetDemographicsRequest queryObj = buildDemographicsRequest(phn);
		
	    Object formattedRequest = hl7.toXml(queryObj, mmd);
	    String historyRequest = formattedRequest.toString();
	
	    //ResponseEntity<String> getDemoResponse = postDemographicRequest(historyRequest, transactionId); 
	    String getDemoResponse = getDemoGraphicsResponse();
	    
	    //GetDemographicsResponse demographicsResponse = hl7.fromXml(getDemoResponse.getBody().toString(), GetDemographicsResponse.class);
	    GetDemographicsResponse demographicsResponse = hl7.fromXml(getDemoResponse, GetDemographicsResponse.class);

	     if (demographicsResponse.getResultCount() == 0) {
	    	 logger.debug("Error performing get demographics");
	    	 throw new HL7Exception("Error performing request for the phn : {}, phn");
	      }
	     
	    GetPersonDetailsResponse personDetails = buildPersonDetailsResponse(demographicsResponse);
	    logger.info("Person details: {}", personDetails.toString());
	     
	    return personDetails;
	 
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
    
    private GetDemographicsRequest buildDemographicsRequest(String phn) {
    	
    	GetDemographicsRequest getDemographics = new GetDemographicsRequest();
    	getDemographics.setPhn(phn);
    	getDemographics.setPhn(mrn_source);
    	logger.debug("Creating request for the phn : {}", getDemographics.getPhn());
    	
    	return getDemographics;
    	
    }
    
    private GetPersonDetailsResponse buildPersonDetailsResponse(GetDemographicsResponse respObj)  {
    	
    	GetPersonDetailsResponse personDetails = new GetPersonDetailsResponse();
    	personDetails.setPerson(respObj.getPerson());
    	personDetails.setMessage(respObj.getMessage());
    	
    	logger.info(personDetails.toString());
    	
    	return personDetails;
    	
    	
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
	
	private String getDemoGraphicsResponse() throws IOException {
		// our XML file for this example
		File xmlFile = new File("src\\main\\resources\\GetDemographicsResponse.xml");
		        
		 // Let's get XML file as String using BufferedReader
		 // FileReader uses platform's default character encoding
		 // if you need to specify a different encoding, 
		 // use InputStreamReader
		 Reader fileReader;
				
		 fileReader = new FileReader(xmlFile);
				
		 BufferedReader bufReader = new BufferedReader(fileReader);
		        
		 StringBuilder sb = new StringBuilder();
		 String line = bufReader.readLine();
		 while( line != null){
		      sb.append(line).append("\n");
		      line = bufReader.readLine();
		  }
		 String xml2String = sb.toString();
		 logger.info("XML to String using BufferedReader : ");
		 //logger.info(xml2String);
		        
		  bufReader.close();
		        
		  return xml2String;
	}
	
}
