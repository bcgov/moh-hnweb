package ca.bc.gov.hlth.hnweb.converter.hl7v2;

import static ca.bc.gov.hlth.hnweb.util.V2MessageUtil.SegmentType.ADJ;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.eligibility.CheckMspCoverageStatusRequest;
import ca.bc.gov.hlth.hnweb.model.eligibility.CheckMspCoverageStatusResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.bc.gov.hlth.hnweb.model.v2.segment.HDR;
import ca.bc.gov.hlth.hnweb.model.v2.segment.QPD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.SFT;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.segment.RCP;
import ca.uhn.hl7v2.util.Terser;

public class E45Converter extends BaseV2Converter {
	public static final Logger logger = LoggerFactory.getLogger(E45Converter.class);

	private static final String MESSAGE_TYPE = "E45";
	private static final String RECEIVING_APPLICATION = "RAIENROL-EMP";
	private static final String QPD_MESSAGE_QUERY_TYPE = "E45^^HNET0003";
	
	private static final String ADJ_ID_CCARD = "CCARD";
	private static final String ADJ_ID_CINST = "CINST";
	private static final String ADJ_ID_ENDRSN = "ENDRSN";
	private static final String ADJ_ID_EYE = "EYE";
	private static final String ADJ_ID_PRS = "PRS";
	private static final String ADJ_ID_PVC = "PVC";
		
	private LocalDate dateOfService;
	private String phn;
	
	public E45Converter(MSHDefaults mshDefaults) {
		super(mshDefaults);
	}
	
	public E45 convertRequest(CheckMspCoverageStatusRequest request) throws HL7Exception {
		phn = request.getPhn();
		dateOfService = request.getDateOfService();

		E45 e45 = new E45();

    	populateMSH(e45.getMSH());
    	populateHDR(e45.getHDR());
    	populateSFT(e45.getSFT());
    	populateQPD(e45.getQPD(), request.getDateOfBirth(), request.getDateOfService(), request.getCheckSubsidyInsuredService(), request.getCheckLastEyeExam(), request.getCheckPatientRestriction());
		populateRCP(e45.getRCP());

    	return e45;
	}
	
	public CheckMspCoverageStatusResponse convertResponse(Message message) throws HL7Exception {
		CheckMspCoverageStatusResponse coverageResponse = new CheckMspCoverageStatusResponse();
    	
    	// Uses a Terser to access the message info
    	Terser terser = new Terser(message);
    	
    	mapPersonValues(terser, coverageResponse, dateOfService);
    	mapAdjustmentValues(message, coverageResponse);    	
		mapErrorValues(terser, coverageResponse);

		return coverageResponse;
	}
	
	protected String getReceivingApplication() {
		return RECEIVING_APPLICATION;
	}
	
	protected String getMessageType() {
		return MESSAGE_TYPE;
	}

	private void populateHDR(HDR hdr) throws HL7Exception {
		V2MessageUtil.setHdrValues(hdr, userInfo.getRole());
	}

	private void populateRCP(RCP rcp) throws HL7Exception {
		V2MessageUtil.setRcpValues(rcp);
	}
	
	private void populateSFT(SFT sft) throws HL7Exception {
		V2MessageUtil.setSftValues(sft);
	}

	private void populateQPD(QPD qpd, LocalDate dateOfBirth, LocalDate dateOfService, Boolean checkSubsidyInsuredService, 
			Boolean checkLastEyeExam, Boolean checkPatientRestriction) throws HL7Exception {
		V2MessageUtil.setQpdValues(qpd, QPD_MESSAGE_QUERY_TYPE, phn, userInfo.getOrganization(), dateOnlyFormatter.format(dateOfBirth), dateOnlyFormatter.format(dateOfService), 
				checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction);		
	}
	
	private void mapPersonValues(Terser terser, CheckMspCoverageStatusResponse checkMspCoverageStatusResponse, LocalDate dateOfService) throws HL7Exception {		
		// e.g. PID|||9873944324||TEST^ELIGIBILITY^MOH^^^^L||19780601|M
    	String phn = terser.get("/.PID-3-1");
    	String surname = terser.get("/.PID-5-1");
    	String givenName = terser.get("/.PID-5-2");
    	String secondName = terser.get("/.PID-5-3");
    	String dateOfBirth = terser.get("/.PID-7-1");
    	String gender = terser.get("/.PID-8-1");

		// Populate the response with extracted values
    	checkMspCoverageStatusResponse.setPhn(phn);
    	checkMspCoverageStatusResponse.setSurname(surname);
    	checkMspCoverageStatusResponse.setGivenName(givenName);
    	checkMspCoverageStatusResponse.setSecondName(secondName);
    	checkMspCoverageStatusResponse.setDateOfBirth(dateOfBirth);
    	checkMspCoverageStatusResponse.setGender(gender);
    	checkMspCoverageStatusResponse.setDateOfService(dateOnlyFormatter.format(dateOfService));    	

    	// e.g. IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||Y
    	String planExpirationDate = terser.get("/.IN1-13-1");
    	String reportOfEligibilityFlag = terser.get("/.IN1-25-1");
    	    	
    	checkMspCoverageStatusResponse.setCoverageEndDate(planExpirationDate);
    	checkMspCoverageStatusResponse.setEligibleOnDateOfService(reportOfEligibilityFlag);
	}
	
	private void mapAdjustmentValues(Message message, CheckMspCoverageStatusResponse checkMspCoverageStatusResponse) throws HL7Exception {
		/**
		 * Iterate through the ADJ segments and extract the required information.
		 * e.g. 
    	 * ADJ|1|IN|||PVC^^HNET9908|N 
    	 * ADJ|2|IN|||EYE^^HNET9908 
    	 * ADJ|3|IN|||PRS^^HNET9908|N
    	 */
    	String[] names = message.getNames();
    	if (Arrays.stream(names).noneMatch(n -> StringUtils.equals(ADJ.name(), n))) {
    		return;
    	}
  
		List<Segment> adjSegments = Arrays.stream(message.getAll(ADJ.name())).map(s -> (Segment)s).collect(Collectors.toList());
		
		adjSegments.forEach(as -> {			
			try {
				Varies reasonCodeVaries = (Varies)as.getField(5, 0);				
				CE ce = new CE(message);
				reasonCodeVaries.setData(ce);
				String identifier = ce.getCe1_Identifier().encode();
				String adjustmentDescription = as.getField(6,0).encode();
				
				switch (identifier) {
				case ADJ_ID_ENDRSN:
					checkMspCoverageStatusResponse.setCoverageEndReason(adjustmentDescription);
					break;
				case ADJ_ID_CCARD:
					checkMspCoverageStatusResponse.setCareCardWarning(adjustmentDescription);
					break;
				case ADJ_ID_CINST:
					checkMspCoverageStatusResponse.setClientInstructions(adjustmentDescription);
					break;
				case ADJ_ID_PVC:
					checkMspCoverageStatusResponse.setSubsidyInsuredService(adjustmentDescription);
					break;
				case ADJ_ID_EYE:
					// Unlike PVC and PRS, the V2 returns an empty segment if there is no date. That makes it difficult to determine if the value
					// was not requested or not provided in the response. Substitute "N" instead and handle in the UI
					checkMspCoverageStatusResponse.setDateOfLastEyeExamination(StringUtils.isNotBlank(adjustmentDescription) ? adjustmentDescription : "N");
					break;
				case ADJ_ID_PRS:
					checkMspCoverageStatusResponse.setPatientRestriction(adjustmentDescription);
					break;
				default:
					logger.warn("Adjustment Identifier {} not recognised", identifier);
				}				
			} catch (HL7Exception e) {
				logger.error("Could not set Adjustment value"); 
			}
		});	
	}

}
