package ca.bc.gov.hlth.hnweb.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil.AddressType;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

/**
 * Contains methods to facilitate converter an EnrollSubscriberRequest to and R50 Message
 * and from Message to an EnrollSubscriberResponse.
 *
 */
public class R50Converter extends BaseConverter {

	private static final Logger logger = LoggerFactory.getLogger(R50Converter.class);

	private static final String MESSAGE_TYPE = "R50^Z06";
	private static final String RECEIVING_APPLICATION = "RAICHK-BNF-CVST";
	
	private String phn;
		
	public R50Converter(MSHDefaults mshDefaults) {
		super(mshDefaults);
	}
	
	public R50 convertRequest(EnrollSubscriberRequest request) throws HL7Exception {
		phn = request.getPhn();
						
    	//Create a default R50 message with MSH-9 set to R50 Z06 
    	R50 r50 = new R50(); 
    	ZIA zia = r50.getZIA();

    	populateMSH(r50.getMSH());
    	populateZHD(r50.getZHD());
    	populatePID(r50.getPID(), phn);    	    	
    	populateIN1(r50.getIN1(), request.getCoverageEffectiveDate());
    	populateZIA(zia, request.getResidenceDate(), request.getFullName(), request.getFullName(), request.getAreaCode(),request.getTelephone(), request.getImmigrationCode(), request.getPriorResidenceCode());
    	populateZIAExtendedAddress1(zia, request.getAddress(), null, null, null, request.getCity(), request.getProvince(), request.getPostalCode());
    	populateZIAExtendedAddress2(zia, request.getMailingAddress(), null, null, null, request.getMailingAddressCity(), request.getMailingAddressProvince(), request.getMailingAddressPostalCode());
    	populateZIH(r50.getZIH()); 
    	populateZIK(r50.getZIK(), request.getVisaIssueDate(), request.getVisaExpiryDate());
  
		return r50;
	}
	
	public EnrollSubscriberResponse convertResponse(Message message) throws HL7Exception {
    	EnrollSubscriberResponse enrollSubscriberResponse = new EnrollSubscriberResponse();
    	
    	// Uses a Terser to access the message info
    	Terser terser = new Terser(message);
    	
    	/* 
    	 * Retrieve required info by specifying the location
    	 */ 
    	String messageId = terser.get("/.MSH-10-1");
    	String acknowledgementCode = terser.get("/.MSA-1-1");
    	String acknowledgementMessage = terser.get("/.MSA-3-1");
    	
    	logger.debug("ACK message response code [{}] and message [{}]", acknowledgementCode, acknowledgementMessage);
    	
    	enrollSubscriberResponse.setMessageId(messageId);
    	enrollSubscriberResponse.setAcknowledgementCode(acknowledgementCode);
		enrollSubscriberResponse.setAcknowledgementMessage(acknowledgementMessage);
    	
    	return enrollSubscriberResponse;
	}
	
	protected String getReceivingApplication() {
		return RECEIVING_APPLICATION;
	}
	
	protected String getMessageType() {
		return MESSAGE_TYPE;
	}

	

}
