package ca.bc.gov.hlth.hnweb.converter;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.R50Response;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.segment.PID;
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
	
	public enum AcknowledgementCode {
		AA, AE, AR;
	}
		
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
    	populatePID(r50.getPID(), phn, request.getDateOfBirth());    	    	
    	populateIN1(r50.getIN1(), request.getCoverageEffectiveDate());
    	populateZIA(zia, request.getResidenceDate(), request.getSurname(), request.getGivenName(), request.getSecondName(), request.getTelephone(), request.getImmigrationCode(), request.getPriorResidenceCode());
    	populateZIAExtendedAddress1(zia, request.getAddress1(), request.getAddress2(),request.getAddress3(), request.getCity(), request.getProvince(), request.getPostalCode());
    	populateZIAExtendedAddress2(zia, request.getMailingAddress1(),request.getMailingAddress2(),request.getMailingAddress3(), request.getMailingAddressCity(), request.getMailingAddressProvince(), request.getMailingAddressPostalCode());
    	populateZIH(r50.getZIH()); 
    	populateZIK(r50.getZIK(), request.getVisaIssueDate(), request.getVisaExpiryDate());
    	logger.info("Request for enroll subscriber : {}",r50);
    	
		return r50;
	}
	
	public R50Response convertResponse(Message message) throws HL7Exception {
    	R50Response r50Response = new R50Response();
    	
    	// Uses a Terser to access the message info
    	Terser terser = new Terser(message);
    	
    	/* 
    	 * Retrieve required info by specifying the location
    	 */ 
    	String messageId = terser.get("/.MSH-10-1");
    	String acknowledgementCode = terser.get("/.MSA-1-1");
    	String acknowledgementMessage = terser.get("/.MSA-3-1");
    	
    	logger.debug("ACK message response code [{}] and message [{}]", acknowledgementCode, acknowledgementMessage);
    	
    	r50Response.setMessageId(messageId);
    	r50Response.setAcknowledgementCode(acknowledgementCode);
    	r50Response.setAcknowledgementMessage(acknowledgementMessage);
    	
    	return r50Response;
	}
	
	public EnrollSubscriberResponse buildEnrollSubscribeResponse(R50Response r50Response ) {
		EnrollSubscriberResponse response = new EnrollSubscriberResponse();
		String ackCode = r50Response.getAcknowledgementCode();
		
		//Check the AcknowledgementCode and set the status 
		if(StringUtils.equals(ackCode, AcknowledgementCode.AE.name()) 
				|| StringUtils.equals(ackCode, AcknowledgementCode.AR.name())) {
			response.setStatus(StatusEnum.ERROR);
			response.setMessage(r50Response.getAcknowledgementMessage());
		} else if (StringUtils.equals(ackCode, AcknowledgementCode.AA.name())) {
			response.setStatus(StatusEnum.SUCCESS);
		}
		
		return response;
	}
	
	protected void populatePID(PID pid, String phn, LocalDate dateOfBirth ) throws HL7Exception {
		V2MessageUtil.setPidValues(pid, phn, PID_NAMESPACE_ID, PID_ID_TYPE_CODE, "", dateOnlyFormatter.format(dateOfBirth), "");
	}
	
	
	protected String getReceivingApplication() {
		return RECEIVING_APPLICATION;
	}
	
	protected String getMessageType() {
		return MESSAGE_TYPE;
	}
		

}
