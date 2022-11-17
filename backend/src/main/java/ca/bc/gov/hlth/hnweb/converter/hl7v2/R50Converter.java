package ca.bc.gov.hlth.hnweb.converter.hl7v2;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v2.segment.IN1;
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
	private static final String MESSAGE_TYPE_TRIGGER_TYPE = "Y00";
	private static final String MESSAGE_TYPE_Z05  = "R50^Z05";
	private static final String MESSAGE_TYPE_Z06  = "R50^Z06";
	private static final String RECEIVING_APPLICATION = "RAIENROL-EMP";
	private static final String ZIH_COVERAGE_CAN_REASON = "D";
	private String phn;
	private String messageType;
	
	public enum AcknowledgementCode {
		AA, AE, AR;
	}
		
	public R50Converter(MSHDefaults mshDefaults) {		
		super(mshDefaults);
	}
	
	public R50 convertRequest(EnrollSubscriberRequest request, String messageId) throws HL7Exception {
		phn = request.getPhn();
		messageType = StringUtils.isEmpty(phn) ? MESSAGE_TYPE_Z05 : MESSAGE_TYPE_Z06;
			
    	//Create a default R50 message with MSH-9 set to R50 Z05/Z06 
    	R50 r50 = new R50(); 
    	ZIA zia = r50.getZIA();
   	   	
     	populateMSH(r50.getMSH(), messageId);
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
	
    /**
     * Populate a customized IN1 segment.
     */
    private void populateIN1(IN1 in1, LocalDate planEffectiveDate, LocalDate planCancellationDate, String groupNumber, String groupMemberNumber, String departmentNumber) throws HL7Exception {
        setIn1Values(in1, groupNumber, groupMemberNumber, departmentNumber, dateOnlyFormatter.format(planEffectiveDate), dateOnlyFormatter.format(planCancellationDate));
    }

    /**
     *  Set the values of a customized IN1 segment.
     */
    private static void setIn1Values(IN1 in1, String groupNumber, String insuredSGroupEmpID, String insuredSGroupEmpNameIdNumber, 
                                    String planEffectiveDate, String planExpirationDate) throws HL7Exception {
        //e.g. IN1||||||||6337109||789446|^^ABC456|20190501|20201231^M
        
        in1.getIn18_GroupNumber().parse(groupNumber);
        in1.getIn110_InsuredSGroupEmpID(0).parse(insuredSGroupEmpID);
        in1.getIn111_InsuredSGroupEmpName(0).getIDNumber().parse(insuredSGroupEmpNameIdNumber);
        in1.getIn112_PlanEffectiveDate().parse(planEffectiveDate);
        in1.getIn113_PlanExpirationDate().parse(planExpirationDate);
    }

	public EnrollSubscriberResponse convertResponse(Message message) throws HL7Exception {
		EnrollSubscriberResponse enrollSubscriberResponse = new EnrollSubscriberResponse();
    	
		logger.debug("Response message : {}", message.toString());
    	// Uses a Terser to access the message info
    	Terser terser = new Terser(message);    
    	String triggerType = terser.get("/.MSH-9-2");
    	
    	mapErrorValues(terser, enrollSubscriberResponse);
    	//Set PHN from PID segment for message type Z05
    
    	if (StatusEnum.SUCCESS == enrollSubscriberResponse.getStatus() && StringUtils.isNotEmpty(triggerType)
				&& triggerType.equals(MESSAGE_TYPE_TRIGGER_TYPE)) {
			String pid = terser.get("/.PID-2-1");
			enrollSubscriberResponse.setPhn(pid);
		}
    	  	
    	logger.debug("ACK message response status [{}] and message [{}]", enrollSubscriberResponse.getStatus(), enrollSubscriberResponse.getMessage());
    	
    	return enrollSubscriberResponse;
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

	@Override
	protected String getMessageType() {
		return messageType;
	}	

}
