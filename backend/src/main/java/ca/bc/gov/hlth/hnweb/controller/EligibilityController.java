package ca.bc.gov.hlth.hnweb.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.CheckMspCoverageStatusResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.bc.gov.hlth.hnweb.service.EligibilityService;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.*;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.ELD;
import ca.uhn.hl7v2.model.v24.segment.ERR;
import ca.uhn.hl7v2.util.Terser;

@RequestMapping("/eligibility")
@RestController
public class EligibilityController {

	private static final Logger logger = LoggerFactory.getLogger(EligibilityController.class);

	private static final String Y = "Y";

	private static final String DATE_FORMAT_yyyyMMdd = "yyyyMMdd";

	@Autowired
	private EligibilityService eligibilityService;
	
	@GetMapping("/check-eligibility")
	public ResponseEntity<CheckEligibilityResponse> checkEligibility(@RequestParam(name = "phn", required = true) String phn,
			@DateTimeFormat(iso = ISO.DATE) @RequestParam(name = "eligibilityDate", required = false) Date eligibilityDate) {
		logger.info("checkEligibility request - phn: {} date: {}", phn, eligibilityDate);

		try {
			CheckEligibilityResponse checkEligibilityResponse = eligibilityService.checkEligibility(phn, eligibilityDate);
			
			ResponseEntity<CheckEligibilityResponse> response = ResponseEntity.ok(checkEligibilityResponse);
		
			logger.info("checkEligibility response: {} ", checkEligibilityResponse);
			return response;	
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /checkEligibility request", e);
		}
		
	}

	@GetMapping("/check-msp-coverage-status")
	public ResponseEntity<CheckMspCoverageStatusResponse> checkMspCoverageStatus(
			@RequestParam(name = "phn", required = true) String phn,
			@DateTimeFormat(iso = ISO.DATE) @RequestParam(name = "dateOfBirth", required = true) Date dateOfBirth,
			@DateTimeFormat(iso = ISO.DATE) @RequestParam(name = "dateOfService", required = true) Date dateOfService, 
			@RequestParam(name = "checkSubsidyInsuredService") Boolean checkSubsidyInsuredService,
			@RequestParam(name = "checkSubsidyInsuredService") Boolean checkLastEyeExam, 
			@RequestParam(name = "checkSubsidyInsuredService") Boolean checkPatientRestriction) throws HL7Exception {
		
		logger.info("checkMspCoverageStatus request - phn: {}; dateOfBirth: {}; dateOfService: {}; "
				+ "checkSubsidyInsuredService: {}; checkLastEyeExam: {}; checkPatientRestriction: {}", 
				phn, dateOfBirth, dateOfService, checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction);
		
		
		E45 e45 = buildE45Message(phn, dateOfBirth, dateOfService, checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction);		
		String transactionId = UUID.randomUUID().toString();		
		Message message = eligibilityService.checkMspCoverageStatus(e45, transactionId);		
		CheckMspCoverageStatusResponse checkMspCoverageStatusResponse = buildEligibilityResponse(message, dateOfService);		
		ResponseEntity<CheckMspCoverageStatusResponse> response = ResponseEntity.ok(checkMspCoverageStatusResponse);
	
		logger.info("checkEligibility response: {} ", checkMspCoverageStatusResponse);
		
		return response;
	}

    private E45 buildE45Message(String phn, Date dateOfBirth, Date dateOfService, Boolean checkSubsidyInsuredService, Boolean checkLastEyeExam, Boolean checkPatientRestriction) throws HL7Exception {
    	
    	//Create a new E45 message and set it's values 
    	E45 e45 = new E45();
    	
    	setSegmentValues(e45, phn,
    			DateFormatUtils.format(dateOfBirth, DATE_FORMAT_yyyyMMdd), 
    			DateFormatUtils.format(dateOfService, DATE_FORMAT_yyyyMMdd), 
    			checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction);
    	
    	return e45;    	
    }
	
	private void setSegmentValues(E45 e45, String phn, String dateOfBirth, String dateOfService,
			Boolean checkSubsidyInsuredService, Boolean checkLastEyeExam, Boolean checkPatientRestriction) throws HL7Exception {
	   		
		V2MessageUtil.setMshValues(e45.getMSH(), "HNWeb", "BC01000030", "RAIENROL-EMP", "BC00001013", "20200529114230", "10-ANother", "E45", "20200529114230", "D", "2.4");
		V2MessageUtil.setHdrValues(e45.getHDR(), "TRAININGAdmin");
		V2MessageUtil.setSftValues(e45.getSFT(), "1.0", "testorg", "101", "MOH", "1.0", "barebones");
		V2MessageUtil.setQpdValues(e45.getQPD(), "E45^^HNET0003", "1", phn, dateOfBirth, dateOfService, checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction);		
		V2MessageUtil.setRcpValues(e45.getRCP(), "I");
	}

	private CheckMspCoverageStatusResponse buildEligibilityResponse(Message message, Date dateOfService) throws HL7Exception {
		
    	CheckMspCoverageStatusResponse checkMspCoverageStatusResponse = new CheckMspCoverageStatusResponse();
    	
    	// Use a Terser to access the message info
    	Terser terser = new Terser(message);
    	
    	/* 
    	 * Retrieve required info from the response message by specifying the location based on Segment-FieldSequence-FieldSubSection
    	 */ 
		
    	//e.g. PID|||9873944324||TEST^ELIGIBILITY^MOH^^^^L||19780601|M
    	String phn = terser.get("/.PID-3-1");
    	String surname = terser.get("/.PID-5-1");
    	String givenName = terser.get("/.PID-5-2");
    	String secondName = terser.get("/.PID-5-3");
    	String dateOfBirth = terser.get("/.PID-7-1");
    	String gender = terser.get("/.PID-8-1");

		//Populate the response with extracted values
    	checkMspCoverageStatusResponse.setPhn(phn);
    	checkMspCoverageStatusResponse.setSurname(surname);
    	checkMspCoverageStatusResponse.setGivenName(givenName);
    	checkMspCoverageStatusResponse.setSecondName(secondName);
    	checkMspCoverageStatusResponse.setDateOfBirth(dateOfBirth);
    	checkMspCoverageStatusResponse.setGender(gender);
    	checkMspCoverageStatusResponse.setDateOfService(dateOfService);    	

    	//e.g. IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||Y
    	String planExpirationDate = terser.get("/.IN1-13-1");
    	String reportOfEligibilityFlag = terser.get("/.IN1-25-1");
    	    	
    	checkMspCoverageStatusResponse.setCoverageEndDate(planExpirationDate);
    	checkMspCoverageStatusResponse.setEligibleOnDateOfService(reportOfEligibilityFlag);

    	/* e.g. 
    	 * ADJ|1|IN|||PVC^^HNET9908|N 
    	 * ADJ|2|IN|||EYE^^HNET9908 
    	 * ADJ|3|IN|||PRS^^HNET9908|N
    	 */
    	String[] names = message.getNames();
    	if (Arrays.stream(names).anyMatch(n -> StringUtils.equals("ADJ", n))) {
			List<Segment> adjSegments = Arrays.stream(message.getAll("ADJ")).map(s -> {return (Segment)s;}).collect(Collectors.toList());
			
			adjSegments.forEach(as -> {			
				try {
					Varies reasonCodeVaries = (Varies)as.getField(5, 0);				
					CE ce = new CE(message);
					reasonCodeVaries.setData(ce);
					String identifier = ce.getCe1_Identifier().encode();
					String adjustmentDescription = as.getField(6,0).encode();
					
					switch (identifier) {
						case "ENDRSN": checkMspCoverageStatusResponse.setCoverageEndReason(adjustmentDescription); break;
						case "CCARD": checkMspCoverageStatusResponse.setCarecardWarning(adjustmentDescription); break;
						case "PVC": checkMspCoverageStatusResponse.setSubsidyInsuredService(adjustmentDescription); break;
						case "EYE": checkMspCoverageStatusResponse.setDateOfLastEyeExamination(adjustmentDescription); break;
						case "PRS": checkMspCoverageStatusResponse.setPatientRestriction(adjustmentDescription); break;
						default: logger.warn("Adjustment Identifier {} not recognised", identifier);
					}				
				} catch (HL7Exception e) {
					logger.error("Could not set Adjustment value"); 
				}
			});		
    	}
    	
		/**
		 * When checking for an error the MSA segment needs to be checked as even a success message has an ERR segmente.g.
		 * 
		 * MSA|AA||HJMB001ISUCCESSFULLY COMPLETED
		 * ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED
		 * 
		 * So it needs to checked if there was actually an error.
		 * e.g.
		 * 
		 * MSA|AE|20211108170321|ELIG0001DATE OF SERVICE EXCEEDS SYSTEM LIMITS. MUST BE WITHIN THE LAST 18 MONTHS. CONTACT MSP.
		 * ERR|^^^ELIG0001&DATE OF SERVICE EXCEEDS SYSTEM LIMITS. MUST BE WITHIN THE LAST 18 MONTHS. CONTACT MSP.
		 */
		String msaAcknowledgementCode = terser.get("/.MSA-1-1");
		if (!StringUtils.equals("AA", msaAcknowledgementCode)) {
			logger.warn("Error acknowledgement code {} received in the response.", msaAcknowledgementCode);
			Segment errSegment = (Segment) message.get("ERR");
			ERR err = (ERR) errSegment; err.getErr1_ErrorCodeAndLocation(0).getEld4_CodeIdentifyingError().getCe2_Text();
			ELD eld = err.getErr1_ErrorCodeAndLocation(0) ;
			CE eld4_CodeIdentifyingError = eld.getEld4_CodeIdentifyingError();
			String errorCode = eld4_CodeIdentifyingError.getCe1_Identifier().encode();
			String errorText = eld4_CodeIdentifyingError.getCe2_Text().encode();			
			logger.warn("Error returned in E45 response was Error Code: {} ; Error Message: {}", errorCode, errorText);
			checkMspCoverageStatusResponse.setErrorMessage(errorCode + " " + errorText);
		} else {
			logger.debug("Successful acknowledgement code {} received in the response.", msaAcknowledgementCode);
		}
		
		return checkMspCoverageStatusResponse;
	}

}

