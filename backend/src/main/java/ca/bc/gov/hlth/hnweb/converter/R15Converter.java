package ca.bc.gov.hlth.hnweb.converter;

import org.springframework.stereotype.Component;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityRequest;
import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R15;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

@Component
public class R15Converter extends BaseConverter {
	private static final String MESSAGE_TYPE = "R15";
	private static final String RECEIVING_APPLICATION = "RAICHK-BNF-CVST";

	public R15 convertRequest(CheckEligibilityRequest request) throws HL7Exception {
    	R15 r15 = new R15();

    	populateMSH(r15.getMSH());
    	populateZHD(r15.getZHD());
    	populatePID(r15.getPID(), request.getPhn());
    	populateIN1(r15.getIN1(), request.getEligibilityDate());
    	
    	return r15;
	}
	
	public CheckEligibilityResponse convertResponse(CheckEligibilityRequest request, Message message) throws HL7Exception {
    	CheckEligibilityResponse checkEligibilityResponse = new CheckEligibilityResponse();
    	
    	// Uses a Terser to access the message info
    	Terser terser = new Terser(message);
    	
    	checkEligibilityResponse.setPhn(request.getPhn());
    	checkEligibilityResponse.setBeneficiaryOnDateChecked(terser.get("/.IN1-25-1"));
    	checkEligibilityResponse.setCoverageEndDate(terser.get("/.IN1-13-1"));
        checkEligibilityResponse.setCoverageEndReason(terser.get("/.ZIH-15"));
        checkEligibilityResponse.setExclusionPeriodEndDate(terser.get("/.ZIH-16-1"));    		

		return checkEligibilityResponse;
	}
	
	protected String getReceivingApplication() {
		return RECEIVING_APPLICATION;
	}
	
	protected String getMessageType() {
		return MESSAGE_TYPE;
	}

}
