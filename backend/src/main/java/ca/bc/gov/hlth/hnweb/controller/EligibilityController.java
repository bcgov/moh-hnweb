package ca.bc.gov.hlth.hnweb.controller;

import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityRequest;
import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.CheckMspCoverageStatusResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.bc.gov.hlth.hnweb.service.EligibilityService;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

@RequestMapping("/eligibility")
@RestController
public class EligibilityController {

	private static final Logger logger = LoggerFactory.getLogger(EligibilityController.class);

	private static final String Y = "Y";

	@Autowired
	private EligibilityService eligibilityService;
	
	@PostMapping("/check-eligibility")
	public ResponseEntity<CheckEligibilityResponse> checkEligibility(@Valid @RequestBody CheckEligibilityRequest checkEligibilityRequest) {

		try {
			CheckEligibilityResponse checkEligibilityResponse = eligibilityService.checkEligibility(checkEligibilityRequest.getPhn(), checkEligibilityRequest.getEligibilityDate());
			
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
    	setSegmentValues(e45, phn, dateOfBirth, dateOfService, checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction);    	     	
    	return e45;
    	
    }
	
	private void setSegmentValues(E45 e45, String phn, Date dateOfBirth, Date dateOfService,
			Boolean checkSubsidyInsuredService, Boolean checkLastEyeExam, Boolean checkPatientRestriction) throws HL7Exception {
	   		
		V2MessageUtil.setMshValues(e45.getMSH(), "HNWeb", "BC01000030", "RAIENROL-EMP", "BC00001013", "20200529114230", "10-ANother", "E45", "20200529114230", "D", "2.4");
		V2MessageUtil.setHdrValues(e45.getHDR(), "TRAININGAdmin");
		V2MessageUtil.setSftValues(e45.getSFT(), "1.0", "testorg", "101", "MOH", "1.0", "barebones");
		V2MessageUtil.setQpdValues(e45.getQPD(), "E45^^HNET0003", "1", phn, dateOfBirth, dateOfService, checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction);		
		V2MessageUtil.setRcpValues(e45.getRCP(), "I");
	}

	private CheckMspCoverageStatusResponse buildEligibilityResponse(Message message, Date dateOfService) throws HL7Exception {
		
    	// Use a Terser to access the message info
    	Terser terser = new Terser(message); 
    	
    	/* 
    	 * Retrieve required info from the response message by specifying the location based on Segment-FieldSequence-FieldSubSection
    	 */ 
		
    	//PID|||9873944324||TEST^ELIGIBILITY^MOH^^^^L||19780601|M
    	String phn = terser.get("/.PID-3-1");
    	String surname = terser.get("/.PID-5-1");
    	String givenName = terser.get("/.PID-5-2");
    	String secondName = terser.get("/.PID-5-3");
    	String dateOfBirth = terser.get("/.PID-7-1");
    	String gender = terser.get("/.PID-8-1");

    	//IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||Y
    	String planExpirationDate = terser.get("/.IN1-13-1");
    	String reportOfEligibilityFlag = terser.get("/.IN1-25-1");
    	    	
    	//ADJ|1|IN|||PVC^^HNET9908|N
    	String adj1ReasonCode = terser.get("/.ADJ(0)-5-1");
		String adj2ReasonCode = terser.get("/.ADJ(1)-5-1");
		String adj3ReasonCode = terser.get("/.ADJ(2)-5-1");
    	
		//Populate the response with extracted values
    	CheckMspCoverageStatusResponse checkMspCoverageStatusResponse = new CheckMspCoverageStatusResponse();
    	checkMspCoverageStatusResponse.setPhn(phn);
    	checkMspCoverageStatusResponse.setSurname(surname);
    	checkMspCoverageStatusResponse.setGivenName(givenName);
    	checkMspCoverageStatusResponse.setSecondName(secondName);
    	checkMspCoverageStatusResponse.setDateOfBirth(dateOfBirth);
    	checkMspCoverageStatusResponse.setGender(gender);
    	checkMspCoverageStatusResponse.setDateOfService(dateOfService);    	
    	checkMspCoverageStatusResponse.setCoverageEndDate(planExpirationDate);
    	checkMspCoverageStatusResponse.setEligibleOnDateOfService(StringUtils.equals(Y, reportOfEligibilityFlag));
    	
		return checkMspCoverageStatusResponse;
	}

}

