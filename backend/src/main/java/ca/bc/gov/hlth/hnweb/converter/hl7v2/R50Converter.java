package ca.bc.gov.hlth.hnweb.converter.hl7v2;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIH;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIK;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

/**
 * Contains methods to facilitate converter an EnrollSubscriberRequest to and R50 Message
 * and from Message to an EnrollSubscriberResponse.
 *
 */
public class R50Converter extends BaseV2Converter {

	private static final Logger logger = LoggerFactory.getLogger(R50Converter.class);

	private static String MESSAGE_TYPE;
	private static final String MESSAGE_TYPE_TRIGGER_TYPE = "Y00";
	private static final String MESSAGE_TYPE_Z05  = "R50^Z05";
	private static final String MESSAGE_TYPE_Z06  = "R50^Z06";
	private static final String RECEIVING_APPLICATION = "RAIENROL-EMP";
	private static final String ZIH_COVERAGE_CAN_REASON = "D";
	private String phn;
	
	public enum AcknowledgementCode {
		AA, AE, AR;
	}
		
	public R50Converter(MSHDefaults mshDefaults) {
		super(mshDefaults);
	}
	
	public R50 convertRequest(EnrollSubscriberRequest request) throws HL7Exception {
		phn = request.getPhn();
		setMessageType();	
		
    	//Create a default R50 message with MSH-9 set to R50 Z05/Z06 
    	R50 r50 = new R50(); 
    	ZIA zia = r50.getZIA();
   	   	
     	populateMSH(r50.getMSH());
    	populateZHD(r50.getZHD());
    	populatePID(r50.getPID(), phn, request.getDateOfBirth(), request.getGender());    	    	
    	populateIN1(r50.getIN1(), request.getCoverageEffectiveDate(), request.getCoverageCancellationDate(), request.getGroupNumber(), request.getGroupMemberNumber(),request.getDepartmentNumber());
    	populateZIA(zia, request.getResidenceDate(), request.getSurname(), request.getGivenName(), request.getSecondName(), request.getTelephone(), request.getImmigrationCode(), request.getPriorResidenceCode());
    	populateZIAExtendedAddress1(zia, request.getAddress1(), request.getAddress2(),request.getAddress3(), request.getCity(), request.getProvince(), request.getPostalCode());
    	populateZIAExtendedAddress2(zia, request.getMailingAddress1(), request.getMailingAddress2(), request.getMailingAddress3(), request.getMailingAddressCity(), request.getMailingAddressProvince(), request.getMailingAddressPostalCode());
    	populateZIH(r50.getZIH()); 
    	populateZIK(r50.getZIK(), request.getVisaIssueDate(), request.getVisaExpiryDate());
    	logger.info("Request for enroll subscriber : {}", r50);
    	
		return r50;
	}
	
	public EnrollSubscriberResponse convertResponse(Message message) throws HL7Exception {
		EnrollSubscriberResponse enrollSubscriberResponse = new EnrollSubscriberResponse();
    	
    	// Uses a Terser to access the message info
    	Terser terser = new Terser(message);
    	logger.debug("Response message : {}", message.toString());
    	/* 
    	 * Retrieve required info by specifying the location
    	 */ 
    	String messageId = terser.get("/.MSH-10-1");
    	String triggerType = terser.get("/.MSH-9-2");
    	String acknowledgementCode = terser.get("/.MSA-1-1");
    	String acknowledgementMessage = terser.get("/.MSA-3-1");
    	
    	if (acknowledgementCode.contentEquals("AA") && !StringUtils.isEmpty(triggerType)
				&& triggerType.equals(MESSAGE_TYPE_TRIGGER_TYPE)) {
			String pid = terser.get("/.PID-2-1");
			enrollSubscriberResponse.setPhn(pid);
		}
    	
    	logger.debug("ACK message response code [{}] and message [{}]", acknowledgementCode, acknowledgementMessage);
    	
    	enrollSubscriberResponse.setMessageId(messageId);
    	enrollSubscriberResponse.setAcknowledgementCode(acknowledgementCode);
    	enrollSubscriberResponse.setAcknowledgementMessage(acknowledgementMessage);
    	
    	handleStatus(enrollSubscriberResponse);
    	
    	return enrollSubscriberResponse;
	}
	
	private void handleStatus(EnrollSubscriberResponse enrollSubscriberResponse ) {		
		String ackCode = enrollSubscriberResponse.getAcknowledgementCode();
		
		//Check the AcknowledgementCode and set the status 
		if(StringUtils.equals(ackCode, AcknowledgementCode.AE.name()) 
				|| StringUtils.equals(ackCode, AcknowledgementCode.AR.name())) {
			enrollSubscriberResponse.setStatus(StatusEnum.ERROR);
			enrollSubscriberResponse.setMessage(enrollSubscriberResponse.getAcknowledgementMessage());
		} else if (StringUtils.equals(ackCode, AcknowledgementCode.AA.name())) {
			enrollSubscriberResponse.setStatus(StatusEnum.SUCCESS);
			enrollSubscriberResponse.setMessage("");
		}

	}
	
	private void populateZIA(ZIA zia, LocalDate bcResidencyDate, String surname, String firstGivenName, String secondGivenName, String telephone, String immigrationOrVisaCode, String priorResidenceCode) throws HL7Exception {
		
		V2MessageUtil.setZiaValues(zia, dateOnlyFormatter.format(bcResidencyDate), surname, firstGivenName, secondGivenName, telephone, immigrationOrVisaCode, priorResidenceCode);
	}
	
	private void populateZIAExtendedAddress1(ZIA zia, String addressLine1, String addressLine2, String addressLine3, String city, String province, String postalCode) throws HL7Exception {
		
		V2MessageUtil.setZiaExtendedAddrees1(zia, addressLine1, addressLine2, addressLine3, city, province, postalCode);
	}
	
	private void populateZIAExtendedAddress2(ZIA zia, String addressLine1, String addressLine2, String addressLine3, String city, String province, String postalCode) throws HL7Exception {
		
		V2MessageUtil.setZiaExtendedAddrees2(zia, addressLine1, addressLine2, addressLine3, city, province, postalCode);
	}
	
	private void populateZIH(ZIH zih) throws HL7Exception {
		V2MessageUtil.setZihValues(zih, ZIH_COVERAGE_CAN_REASON);
	}
	
	private void populateZIK(ZIK zik, LocalDate documentArgumentValue1, LocalDate documentArgumentValue2) throws HL7Exception {
		V2MessageUtil.setZikValues(zik, dateOnlyFormatter.format(documentArgumentValue1), dateOnlyFormatter.format(documentArgumentValue2));
	}
	
	protected String getReceivingApplication() {
		return RECEIVING_APPLICATION;
	}
	
	protected String getMessageType() {
		return MESSAGE_TYPE;
	}

	protected void setMessageType() {		
		MESSAGE_TYPE = StringUtils.isEmpty(phn) ? MESSAGE_TYPE_Z05 : MESSAGE_TYPE_Z06;
	}
		

}
