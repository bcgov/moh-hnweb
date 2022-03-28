package ca.bc.gov.hlth.hnweb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.converter.hl7v2.E45Converter;
import ca.bc.gov.hlth.hnweb.converter.hl7v2.MSHDefaults;
import ca.bc.gov.hlth.hnweb.converter.hl7v2.R15Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPPE0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPPL0Converter;
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
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.EligibilityService;
import ca.uhn.hl7v2.model.Message;

/**
 * Handle requests related to Eligibility.
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
		Transaction transaction = transactionStart(request, TransactionType.CHECK_ELIGIBILITY);
		addAffectedParty(transaction, IdentifierType.PHN, checkEligibilityRequest.getPhn(), AffectedPartyDirection.OUTBOUND);

		try {
			R15Converter converter = new R15Converter(mshDefaults);
			R15 r15 = converter.convertRequest(checkEligibilityRequest, transaction.getTransactionId().toString());
			Message r15Response = eligibilityService.checkEligibility(r15, transaction);
			
			CheckEligibilityResponse checkEligibilityResponse = converter.convertResponse(r15Response);
			
			ResponseEntity<CheckEligibilityResponse> response = ResponseEntity.ok(checkEligibilityResponse);
			logger.info("checkEligibility response: {} ", checkEligibilityResponse);
			
			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, checkEligibilityResponse.getPhn(), AffectedPartyDirection.INBOUND);

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
		Transaction transaction = transactionStart(request, TransactionType.PHN_INQUIRY);
		inquirePhnRequest.getPhns().forEach(phn -> addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.OUTBOUND));		

		try {
			RPBSPPE0Converter converter = new RPBSPPE0Converter();
			RPBSPPE0 ppe0Request = converter.convertRequest(inquirePhnRequest);
		
			RPBSPPE0 ppe0Response = eligibilityService.inquirePhn(ppe0Request, transaction);	
			
			InquirePhnResponse inquirePhnResponse = converter.convertResponse(ppe0Response);
					
			ResponseEntity<InquirePhnResponse> response = ResponseEntity.ok(inquirePhnResponse);
			logger.info("inquirePHN response: {} ", inquirePhnResponse);

			transactionComplete(transaction);
			inquirePhnResponse.getBeneficiaries().forEach(beneficiary -> addAffectedParty(transaction, IdentifierType.PHN, beneficiary.getPhn(), AffectedPartyDirection.INBOUND));

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
	public ResponseEntity<LookupPhnResponse> lookupPhn(@Valid @RequestBody LookupPhnRequest lookupPhnRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.PHN_LOOKUP);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, lookupPhnRequest.getGroupNumber(), AffectedPartyDirection.OUTBOUND);
		addAffectedParty(transaction, IdentifierType.CONTRACT_NUMBER, lookupPhnRequest.getContractNumber(), AffectedPartyDirection.OUTBOUND);

		try {
			RPBSPPL0Converter converter = new RPBSPPL0Converter();
			RPBSPPL0 r42Request = converter.convertRequest(lookupPhnRequest);
			
			RPBSPPL0 r42Response = eligibilityService.lookupPhn(r42Request, transaction);
			
			LookupPhnResponse lookupPhnResponse = converter.convertResponse(r42Response);
			
			ResponseEntity<LookupPhnResponse> response = ResponseEntity.ok(lookupPhnResponse);
			logger.info("lookupPhn response: {} ", lookupPhnResponse);
			
			transactionComplete(transaction);
			lookupPhnResponse.getBeneficiaries().forEach(beneficiary -> addAffectedParty(transaction, IdentifierType.PHN, beneficiary.getPhn(), AffectedPartyDirection.INBOUND));
			
			return response;	
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
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
	public ResponseEntity<CheckMspCoverageStatusResponse> checkMspCoverageStatus(@Valid @RequestBody CheckMspCoverageStatusRequest checkMspCoverageStatusRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.MSP_COVERAGE_STATUS_CHECK);
		addAffectedParty(transaction, IdentifierType.PHN, checkMspCoverageStatusRequest.getPhn(), AffectedPartyDirection.OUTBOUND);

		try {
			E45Converter converter = new E45Converter(mshDefaults);
			E45 e45 = converter.convertRequest(checkMspCoverageStatusRequest);
			Message e45Response = eligibilityService.checkMspCoverageStatus(e45, transaction);
			
			CheckMspCoverageStatusResponse coverageResponse = converter.convertResponse(e45Response);

			ResponseEntity<CheckMspCoverageStatusResponse> response = ResponseEntity.ok(coverageResponse);
			logger.info("checkEligibility response: {} ", coverageResponse);
			
			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, coverageResponse.getPhn(), AffectedPartyDirection.INBOUND);
			
			return response;	
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

}