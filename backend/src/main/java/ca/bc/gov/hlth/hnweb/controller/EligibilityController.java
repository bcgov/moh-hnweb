package ca.bc.gov.hlth.hnweb.controller;

import static ca.bc.gov.hlth.hnweb.util.V2MessageUtil.SegmentType.ADJ;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.converter.MSHDefaults;
import ca.bc.gov.hlth.hnweb.converter.R15Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.R41Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.R42Converter;
import ca.bc.gov.hlth.hnweb.model.CheckEligibilityRequest;
import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.CheckMspCoverageStatusRequest;
import ca.bc.gov.hlth.hnweb.model.CheckMspCoverageStatusResponse;
import ca.bc.gov.hlth.hnweb.model.InquirePhnMatch;
import ca.bc.gov.hlth.hnweb.model.InquirePhnRequest;
import ca.bc.gov.hlth.hnweb.model.InquirePhnResponse;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnRequest;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnResponse;
import ca.bc.gov.hlth.hnweb.model.rapid.RapidDefaults;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPL0;
import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.bc.gov.hlth.hnweb.model.v2.message.R15;
import ca.bc.gov.hlth.hnweb.service.EligibilityService;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.ELD;
import ca.uhn.hl7v2.model.v24.segment.ERR;
import ca.uhn.hl7v2.util.Terser;

/**
 * Handle requests related to Eligibility
 *
 */
@RequestMapping("/eligibility")
@RestController
public class EligibilityController {

	private static final Logger logger = LoggerFactory.getLogger(EligibilityController.class);

	@Deprecated
	private static final String DATE_FORMAT_yyyyMMdd = "yyyyMMdd";

	@Autowired
	private EligibilityService eligibilityService;

	@Autowired
	private RapidDefaults rapidDefaults;
	
	@Autowired
	private MSHDefaults mshDefaults;
	
	@PostMapping("/check-eligibility")
	public ResponseEntity<CheckEligibilityResponse> checkEligibility(@Valid @RequestBody CheckEligibilityRequest checkEligibilityRequest) {

		try {
			R15Converter converter = new R15Converter(mshDefaults);
			R15 r15 = converter.convertRequest(checkEligibilityRequest);
			Message r15Response = eligibilityService.checkEligibility(r15);
			
			CheckEligibilityResponse checkEligibilityResponse = converter.convertResponse(r15Response);
			
			ResponseEntity<CheckEligibilityResponse> response = ResponseEntity.ok(checkEligibilityResponse);

			logger.info("checkEligibility response: {} ", checkEligibilityResponse);
			return response;	
		} catch (Exception e) {
			// TODO (weskubo-cgi) Update this with more specific error handling once downstream services are integrated
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /check-eligibility request", e);
		}
		
	}	
	
	@PostMapping("/inquire-phn")
	public ResponseEntity<InquirePhnResponse> inquirePhn(@Valid @RequestBody InquirePhnRequest inquirePhnRequest) {

		try {
			R41Converter converter = new R41Converter(rapidDefaults);
			RPBSPPE0 r41Request = converter.convertRequest(inquirePhnRequest);
			
			RPBSPPE0 r41Response = eligibilityService.inquirePhn(r41Request);
			
			InquirePhnResponse inquirePhnResponse = converter.convertResponse(r41Response);
			// TOOD (weskubo-cgi) The error message is removed for now so the UI can still show the canned results
			// as all requests are returning an error
			//inquirePhnResponse.setErrorMessage(null);
			inquirePhnResponse.setMatches(generateDummyR41Data());			
			
			ResponseEntity<InquirePhnResponse> response = ResponseEntity.ok(inquirePhnResponse);

			logger.info("inquirePHN response: {} ", inquirePhnResponse);
			return response;	
		} catch (Exception e) {
			// TODO (weskubo-cgi) Update this with more specific error handling once downstream services are integrated
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /check-eligibility request", e);
		}
		
	}
	
	@PostMapping("/lookup-phn")
	public ResponseEntity<LookupPhnResponse> lookupPhn(@Valid @RequestBody LookupPhnRequest lookupPhnRequest) {

		try {
			R42Converter converter = new R42Converter(rapidDefaults);
			RPBSPPL0 r42Request = converter.convertRequest(lookupPhnRequest);
			
			RPBSPPL0 r42Response = eligibilityService.lookupPhn(r42Request);
			
			LookupPhnResponse lookupPhnResponse = converter.convertResponse(r42Response);
			// TOOD (weskubo-cgi) The error message is removed for now so the UI can still show the canned results
			// as all requests are returning an error
			//lookupPhnResponse.setMatches(generateDummyR41Data());			
			
			ResponseEntity<LookupPhnResponse> response = ResponseEntity.ok(lookupPhnResponse);

			logger.info("lookupPhn response: {} ", lookupPhnResponse);
			return response;	
		} catch (Exception e) {
			// TODO (weskubo-cgi) Update this with more specific error handling once downstream services are integrated
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /lookup-phn request", e);
		}
		
	}
	
	private List<InquirePhnMatch> generateDummyR41Data() {

		List<InquirePhnMatch> results = new ArrayList<InquirePhnMatch>();

		InquirePhnMatch result1 = new InquirePhnMatch();
		result1.setPhn("123456789");
		result1.setFirstName("Homer");
		result1.setLastName("Simpson");
		result1.setDateOfBirth("19600101");
		result1.setGender("M");
		result1.setEligible("N");
		result1.setStudent("N");
		results.add(result1);

		InquirePhnMatch result2 = new InquirePhnMatch();
		result2.setPhn("234567890");
		result2.setFirstName("Marge");
		result2.setLastName("Simpson");
		result2.setDateOfBirth("19650101");
		result2.setGender("F");
		result2.setEligible("Y");
		result2.setStudent("N");
		results.add(result2);

		InquirePhnMatch result3 = new InquirePhnMatch();
		result3.setPhn("345678901");
		result3.setFirstName("Lisa");
		result3.setLastName("Simpson");
		result3.setDateOfBirth("19900101");
		result3.setGender("F");
		result3.setEligible("N");
		result3.setStudent("Y");
		results.add(result3);

		InquirePhnMatch result4 = new InquirePhnMatch();
		result4.setPhn("456789012");
		result4.setFirstName("Bart");
		result4.setLastName("Simpson");
		result4.setDateOfBirth("19950101");
		result4.setGender("M");
		result4.setEligible("Y");
		result4.setStudent("Y");
		results.add(result4);
		
		return results;
	}

	@PostMapping("/check-msp-coverage-status")
	public ResponseEntity<CheckMspCoverageStatusResponse> checkMspCoverageStatus(@Valid @RequestBody CheckMspCoverageStatusRequest checkMspCoverageStatusRequest) throws HL7Exception {
		
		logger.info("checkMspCoverageStatus request params - dateOfBirth: {}; dateOfService: {}; "
				+ "checkSubsidyInsuredService: {}; checkLastEyeExam: {}; checkPatientRestriction: {}", 
				checkMspCoverageStatusRequest.getDateOfBirth(), checkMspCoverageStatusRequest.getDateOfService(), 
				checkMspCoverageStatusRequest.getCheckSubsidyInsuredService(), checkMspCoverageStatusRequest.getCheckLastEyeExam(), checkMspCoverageStatusRequest.getCheckPatientRestriction());
				
		E45 e45 = buildE45Message(checkMspCoverageStatusRequest.getPhn(), checkMspCoverageStatusRequest.getDateOfBirth(), checkMspCoverageStatusRequest.getDateOfService(), checkMspCoverageStatusRequest.getCheckSubsidyInsuredService(), checkMspCoverageStatusRequest.getCheckLastEyeExam(), checkMspCoverageStatusRequest.getCheckPatientRestriction());		
		String transactionId = UUID.randomUUID().toString();		
		Message message = eligibilityService.checkMspCoverageStatus(e45, transactionId);		
		CheckMspCoverageStatusResponse checkMspCoverageStatusResponse = buildEligibilityResponse(message, checkMspCoverageStatusRequest.getDateOfService());		
		ResponseEntity<CheckMspCoverageStatusResponse> response = ResponseEntity.ok(checkMspCoverageStatusResponse);
	
		logger.info("checkMspCoverageStatus response: {} ", checkMspCoverageStatusResponse);
		
		return response;
	}

    private E45 buildE45Message(String phn, LocalDate dateOfBirth, LocalDate dateOfService, Boolean checkSubsidyInsuredService, Boolean checkLastEyeExam, Boolean checkPatientRestriction) throws HL7Exception {
    	
    	//Create a new E45 message and set it's values 
    	E45 e45 = new E45();
    	
    	DateTimeFormatter dateTimeFormatterYyyyMMdd = DateTimeFormatter.ofPattern(DATE_FORMAT_yyyyMMdd);
    	
		setSegmentValues(e45, phn,
    			dateOfBirth.format(dateTimeFormatterYyyyMMdd),
    			dateOfService.format(dateTimeFormatterYyyyMMdd), 
    			BooleanUtils.isTrue(checkSubsidyInsuredService), 
    			BooleanUtils.isTrue(checkLastEyeExam), 
    			BooleanUtils.isTrue(checkPatientRestriction));
    	
    	return e45;    	
    }

    //TODO (daveb-hni) Check if the below 6 methods can be refactored into separate class(es) for common use. Currently E45 only applies to this controller class.
    
	private void setSegmentValues(E45 e45, String phn, String dateOfBirth, String dateOfService,
			boolean checkSubsidyInsuredService, boolean checkLastEyeExam, boolean checkPatientRestriction) throws HL7Exception {
	   		
		V2MessageUtil.setMshValues(e45.getMSH(), "HNWeb", "BC01000030", "RAIENROL-EMP", "BC00001013", "20200529114230", "10-ANother", "E45", "20200529114230", "D");
		V2MessageUtil.setHdrValues(e45.getHDR(), "TRAININGAdmin");
		V2MessageUtil.setSftValues(e45.getSFT(), "1.0", "testorg", "101", "MOH", "1.0", "barebones");
		V2MessageUtil.setQpdValues(e45.getQPD(), "E45^^HNET0003", "1", phn, dateOfBirth, dateOfService, checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction);		
		V2MessageUtil.setRcpValues(e45.getRCP(), "I");
	}

	private CheckMspCoverageStatusResponse buildEligibilityResponse(Message message, LocalDate dateOfService) throws HL7Exception {
		
    	CheckMspCoverageStatusResponse checkMspCoverageStatusResponse = new CheckMspCoverageStatusResponse();
    	
    	// Uses a Terser to access the message info
    	Terser terser = new Terser(message);
    	
    	mapPersonValues(terser, checkMspCoverageStatusResponse, dateOfService);
    	mapAdjustmentValues(message, checkMspCoverageStatusResponse);    	
		mapErrorValues(terser, message, checkMspCoverageStatusResponse);
		
		return checkMspCoverageStatusResponse;
	}

	private void mapPersonValues(Terser terser, CheckMspCoverageStatusResponse checkMspCoverageStatusResponse, LocalDate dateOfService) throws HL7Exception {
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
	}

	private void mapAdjustmentValues(Message message, CheckMspCoverageStatusResponse checkMspCoverageStatusResponse) throws HL7Exception {
		/* 
		 * Iterate through the ADJ segments and extract the required information.
		 * e.g. 
    	 * ADJ|1|IN|||PVC^^HNET9908|N 
    	 * ADJ|2|IN|||EYE^^HNET9908 
    	 * ADJ|3|IN|||PRS^^HNET9908|N
    	 */
    	String[] names = message.getNames();
    	if (Arrays.stream(names).anyMatch(n -> StringUtils.equals(ADJ.name(), n))) {
			List<Segment> adjSegments = Arrays.stream(message.getAll(ADJ.name())).map(s -> (Segment)s).collect(Collectors.toList());
			
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
	}

	private void mapErrorValues(Terser terser, Message message, CheckMspCoverageStatusResponse checkMspCoverageStatusResponse) throws HL7Exception {
		/*
		 * When checking for an error the MSA segment needs to be checked as even a success message has an ERR segment e.g.
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
			ERR err = (ERR) errSegment; 
			ELD eld = err.getErr1_ErrorCodeAndLocation(0) ;
			CE eld4CodeIdentifyingError = eld.getEld4_CodeIdentifyingError();
			String errorCode = eld4CodeIdentifyingError.getCe1_Identifier().encode();
			String errorText = eld4CodeIdentifyingError.getCe2_Text().encode();			
			logger.warn("Error returned in E45 response was Error Code: {} ; Error Message: {}", errorCode, errorText);
			checkMspCoverageStatusResponse.setErrorMessage(errorText);
		} else {
			logger.debug("Successful acknowledgement code {} received in the response.", msaAcknowledgementCode);
		}
	}

}

