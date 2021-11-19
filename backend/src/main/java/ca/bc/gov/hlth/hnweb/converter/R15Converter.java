package ca.bc.gov.hlth.hnweb.converter;

import java.time.LocalDateTime;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityRequest;
import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R15;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

public class R15Converter extends BaseConverter {
	private String phn = null;
	
	public R15 convertRequest(CheckEligibilityRequest request) throws HL7Exception {
		phn = request.getPhn();

    	R15 r15 = new R15();
    	
    	String messageDateTime = LocalDateTime.now().format(dateTimeFormatter);

    	// TODO (weskubo-cgi) Verify the MSH values
    	V2MessageUtil.setMshValues(r15.getMSH(), "HNWeb", "BC01000030", "RAIENROL-EMP", "BC00001013", messageDateTime, "10-ANother", "R15", "20200529114230", "D");
    	
    	// TODO (weskubo-cgi) Verify the ZHD values
		V2MessageUtil.setZhdValues(r15.getZHD(), messageDateTime, "00000010", "HNAIADMINISTRATION", V2MessageUtil.DEFAULT_VERSION_ID);
		
    	V2MessageUtil.setPidValues(r15.getPID(), phn, "BC", "PH", "", "", "");
    	
    	V2MessageUtil.setIn1Values(r15.getIN1(), null, null, null, dateOnlyFormatter.format(request.getEligibilityDate()), null);
    	
    	return r15;
	}
	
	public CheckEligibilityResponse convertResponse(Message message) throws HL7Exception {
    	CheckEligibilityResponse checkEligibilityResponse = new CheckEligibilityResponse();
    	
    	// Uses a Terser to access the message info
    	Terser terser = new Terser(message);
    	
    	checkEligibilityResponse.setPhn(phn);
    	checkEligibilityResponse.setBeneficiaryOnDateChecked(terser.get("/.IN1-25-1"));
    	checkEligibilityResponse.setCoverageEndDate(terser.get("/.IN1-13-1"));
        checkEligibilityResponse.setCoverageEndReason(terser.get("/.ZIH-15"));
        checkEligibilityResponse.setExclusionPeriodEndDate(terser.get("/.ZIH-16-1"));    		

		return checkEligibilityResponse;
	}

}
