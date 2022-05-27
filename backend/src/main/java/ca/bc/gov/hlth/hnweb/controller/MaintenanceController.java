package ca.bc.gov.hlth.hnweb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPRE0Converter;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPRE0;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.MaintenanceService;

/**
 * Handles request related to Coverage Maintenance.
 */
@RequestMapping("/maintenance")
@RestController
public class MaintenanceController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MaintenanceController.class);

	@Autowired
	private MaintenanceService maintenanceService;

	@PostMapping("/reinstate-over-age-dependent")
	public ResponseEntity<ReinstateOverAgeDependentResponse> reinstateOverAgeDependent(@Valid @RequestBody ReinstateOverAgeDependentRequest reinstateRequest, HttpServletRequest request) {

		logger.info("Reinstate over age dependent request: {} ", reinstateRequest.getPhn());
		
		Transaction transaction = auditReinstateOverAgeDependentStart(reinstateRequest, request);

		try {
			RPBSPRE0Converter converter = new RPBSPRE0Converter();
			RPBSPRE0 pre0Request = converter.convertRequest(reinstateRequest);
		
			RPBSPRE0 pre0Response = maintenanceService.reinstateOverAgeDependent(pre0Request, transaction);	
			
			ReinstateOverAgeDependentResponse reinstateResponse = converter.convertResponse(pre0Response);
					
			ResponseEntity<ReinstateOverAgeDependentResponse> response = ResponseEntity.ok(reinstateResponse);
			logger.info("reinstateOverAgeDependent response: {} ", reinstateResponse);
	
			transactionComplete(transaction);
			//inquirePhnResponse.getBeneficiaries().forEach(beneficiary -> addAffectedParty(transaction, IdentifierType.PHN, beneficiary.getPhn(), AffectedPartyDirection.OUTBOUND));
	
			return response;	
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}
	
	private Transaction auditReinstateOverAgeDependentStart(ReinstateOverAgeDependentRequest reinstateRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.ADD_DEPENDENT);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, reinstateRequest.getGroupNumber(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, reinstateRequest.getPhn(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, reinstateRequest.getDependentPhn(), AffectedPartyDirection.INBOUND);
		return transaction;
	}


	private void auditGetNameSearchComplete(Transaction transaction, NameSearchResponse nameSearchResponse) {
		transactionComplete(transaction);
		if (nameSearchResponse.getCandidates() != null) {
			nameSearchResponse.getCandidates().forEach(candidate -> addAffectedParty(transaction, IdentifierType.PHN, candidate.getPhn(), AffectedPartyDirection.OUTBOUND));
		}
	}

}
