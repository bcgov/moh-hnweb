package ca.bc.gov.hlth.hnweb.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.converter.hl7v2.E45Converter;
import ca.bc.gov.hlth.hnweb.converter.hl7v2.MSHDefaults;
import ca.bc.gov.hlth.hnweb.converter.hl7v2.R15Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.R42Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPPE0Converter;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.CheckEligibilityRequest;
import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.CheckMspCoverageStatusRequest;
import ca.bc.gov.hlth.hnweb.model.CheckMspCoverageStatusResponse;
import ca.bc.gov.hlth.hnweb.model.eligibility.InquirePhnRequest;
import ca.bc.gov.hlth.hnweb.model.eligibility.InquirePhnResponse;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnRequest;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnResponse;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPL0;
import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.bc.gov.hlth.hnweb.model.v2.message.R15;
import ca.bc.gov.hlth.hnweb.service.EligibilityService;
import ca.uhn.hl7v2.model.Message;

/**
 * Handle requests related to Eligibility
 *
 */
@RequestMapping("/eligibility")
@RestController
public class EligibilityController {

	private static final Logger logger = LoggerFactory.getLogger(EligibilityController.class);

	@Autowired
	private EligibilityService eligibilityService;
	
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
			RPBSPPE0Converter converter = new RPBSPPE0Converter();
			RPBSPPE0 ppe0Request = converter.convertRequest(inquirePhnRequest);
		
			RPBSPPE0 ppe0Response = eligibilityService.inquirePhn(ppe0Request);	
			
			InquirePhnResponse inquirePhnResponse = converter.convertResponse(ppe0Response);
					
			ResponseEntity<InquirePhnResponse> response = ResponseEntity.ok(inquirePhnResponse);

			logger.info("inquirePHN response: {} ", inquirePhnResponse);
			return response;	
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /inquire-phn request", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /inquire-phn request", e);
		}
		
	}
	
	@PostMapping("/lookup-phn")
	public ResponseEntity<LookupPhnResponse> lookupPhn(@Valid @RequestBody LookupPhnRequest lookupPhnRequest) {

		try {
			R42Converter converter = new R42Converter();
			RPBSPPL0 r42Request = converter.convertRequest(lookupPhnRequest);
			
			RPBSPPL0 r42Response = eligibilityService.lookupPhn(r42Request);
			
			LookupPhnResponse lookupPhnResponse = converter.convertResponse(r42Response);
			
			ResponseEntity<LookupPhnResponse> response = ResponseEntity.ok(lookupPhnResponse);

			logger.info("lookupPhn response: {} ", lookupPhnResponse);
			return response;	

		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /lookup-phn request", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /lookup-phn request", e);
		}		
	}

	@PostMapping("/check-msp-coverage-status")
	public ResponseEntity<CheckMspCoverageStatusResponse> checkMspCoverageStatus(@Valid @RequestBody CheckMspCoverageStatusRequest checkMspCoverageStatusRequest) {
		
		try {
			E45Converter converter = new E45Converter(mshDefaults);
			E45 e45 = converter.convertRequest(checkMspCoverageStatusRequest);
			Message e45Response = eligibilityService.checkMspCoverageStatus(e45);
			
			CheckMspCoverageStatusResponse coverageResponse = converter.convertResponse(e45Response);

			ResponseEntity<CheckMspCoverageStatusResponse> response = ResponseEntity.ok(coverageResponse);

			logger.info("checkEligibility response: {} ", coverageResponse);
			return response;	
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /check-msp-coverage-status", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /check-msp-coverage-status", e);
		}
	}

}