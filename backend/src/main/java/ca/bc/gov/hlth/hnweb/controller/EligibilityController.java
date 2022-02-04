package ca.bc.gov.hlth.hnweb.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPPE0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPPL0Converter;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPL0;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.CheckEligibilityRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.CheckMspCoverageStatusRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.CheckMspCoverageStatusResponse;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.InquirePhnRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.InquirePhnResponse;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.LookupPhnRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.LookupPhnResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.bc.gov.hlth.hnweb.model.v2.message.R15;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.EligibilityService;
import ca.uhn.hl7v2.model.Message;

/**
 * Handle requests related to Eligibility
 *
 */
@RequestMapping("/eligibility")
@RestController
public class EligibilityController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(EligibilityController.class);

	@Autowired
	private EligibilityService eligibilityService;
	
	@Autowired
	private MSHDefaults mshDefaults;
	
	/**
	 * Checks eligibility for the PHN for the specified date.
	 * Maps to the legacy R15.
	 *  
	 * @param checkEligibilityRequest
	 * @return The result of the query
	 */
	@PostMapping("/check-eligibility")
	public ResponseEntity<CheckEligibilityResponse> checkEligibility(@Valid @RequestBody CheckEligibilityRequest checkEligibilityRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.CHECK_ELIGIBILITY, checkEligibilityRequest.getPhn());

		try {
			R15Converter converter = new R15Converter(mshDefaults);
			R15 r15 = converter.convertRequest(checkEligibilityRequest, transaction.getTransactionId().toString());
			Message r15Response = eligibilityService.checkEligibility(r15, transaction);
			
			CheckEligibilityResponse checkEligibilityResponse = converter.convertResponse(r15Response);
			
			ResponseEntity<CheckEligibilityResponse> response = ResponseEntity.ok(checkEligibilityResponse);
			logger.info("checkEligibility response: {} ", checkEligibilityResponse);			

			transactionEnd(transaction, checkEligibilityResponse.getPhn());
			
			return response;	
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}	

	/**
	 * Inquiries on a list of PHNs.
	 * Maps to the legacy R41.
	 *  
	 * @param inquirePhnRequest
	 * @return The result of the query
	 */
	@PostMapping("/inquire-phn")
	public ResponseEntity<InquirePhnResponse> inquirePhn(@Valid @RequestBody InquirePhnRequest inquirePhnRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.CHECK_ELIGIBILITY, inquirePhnRequest.getPhns());

		try {
			RPBSPPE0Converter converter = new RPBSPPE0Converter();
			RPBSPPE0 ppe0Request = converter.convertRequest(inquirePhnRequest);
		
			RPBSPPE0 ppe0Response = eligibilityService.inquirePhn(ppe0Request);	
			
			InquirePhnResponse inquirePhnResponse = converter.convertResponse(ppe0Response);
					
			ResponseEntity<InquirePhnResponse> response = ResponseEntity.ok(inquirePhnResponse);

			logger.info("inquirePHN response: {} ", inquirePhnResponse);
			
			List<String> phns = inquirePhnResponse.getBeneficiaries().stream().map(beneficary -> beneficary.getPhn()).collect(Collectors.toList());
			transactionEnd(transaction, phns);
			return response;	
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}		
	}
	
	/**
	 * Lookup PHNs based on the contract number and group number.
	 * Maps to the legacy R42.
	 *  
	 * @param lookupPhnRequest
	 * @return The result of the query
	 */
	@PostMapping("/lookup-phn")
	public ResponseEntity<LookupPhnResponse> lookupPhn(@Valid @RequestBody LookupPhnRequest lookupPhnRequest) {

		try {
			RPBSPPL0Converter converter = new RPBSPPL0Converter();
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

	/**
	 * Checks coverage status for the PHN/birthDate on the specified date.
	 * Maps to the legacy E45.
	 *  
	 * @param checkMspCoverageStatusRequest
	 * @return The result of the query
	 */
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