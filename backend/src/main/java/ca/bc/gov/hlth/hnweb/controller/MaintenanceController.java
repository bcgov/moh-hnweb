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

import ca.bc.gov.hlth.hnweb.converter.rapid.BaseRapidConverter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPAG0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPAI0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPAJ0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPRE0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPRH0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPXP0Converter;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAG0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAI0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAJ0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPRE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPRH0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPXP0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ChangeCancelDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ChangeCancelDateResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ExtendCancelDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ExtendCancelDateResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateCancelledGroupCoverageRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateCancelledGroupCoverageResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.RenewCancelledGroupCoverageRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.RenewCancelledGroupCoverageResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.GroupMemberService;
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
	
	@Autowired
	private GroupMemberService groupMemberService;

	/**
	 * Changes coverage effective date for the group number. Maps to the legacy
	 * R46a.
	 * 
	 * @param changeEffectiveDateRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/change-effective-date")
	public ResponseEntity<ChangeEffectiveDateResponse> changeEffectiveDate(
			@Valid @RequestBody ChangeEffectiveDateRequest changeEffectiveDateRequest, HttpServletRequest request) {

		Transaction transaction = auditChangeEffectiveDateStart(changeEffectiveDateRequest, request);

		try {
			RPBSPAJ0Converter converter = new RPBSPAJ0Converter();
			RPBSPAJ0 rpbspaj0Request = converter.convertRequest(changeEffectiveDateRequest);
			RPBSPAJ0 rpbspaj0Response = maintenanceService.changeEffectiveDate(rpbspaj0Request, transaction);
			ChangeEffectiveDateResponse changeEffectiveDateResponse = converter.convertResponse(rpbspaj0Response);

			ResponseEntity<ChangeEffectiveDateResponse> response = ResponseEntity.ok(changeEffectiveDateResponse);

			logger.info("changeEffectiveDateResponse response: {} ", changeEffectiveDateResponse);

			auditChangeEffectiveDateComplete(transaction, changeEffectiveDateResponse);

			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	/**
	 * Change the coverage cancellation date of an employee. 
	 * Maps to the legacy R46b (z26).
	 * 
	 * @param changeCancelDateRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/change-cancel-date")
	public ResponseEntity<ChangeCancelDateResponse> changeCancelDate(
			@Valid @RequestBody ChangeCancelDateRequest changeCancelDateRequest, HttpServletRequest request) {

		Transaction transaction = auditChangeCancelDateStart(changeCancelDateRequest.getPhn(), request);

		try {
			RPBSPAG0Converter converter = new RPBSPAG0Converter();
			RPBSPAG0 rpbspag0Request = converter.convertRequest(changeCancelDateRequest);
			RPBSPAG0 rpbspag0Response = maintenanceService.changeCancelDate(rpbspag0Request, transaction);
			ChangeCancelDateResponse changeCancelDateResponse = converter.convertResponse(rpbspag0Response);

			ResponseEntity<ChangeCancelDateResponse> response = ResponseEntity.ok(changeCancelDateResponse);

			logger.info("changeCancelDateResponse response: {} ", changeCancelDateResponse);

			auditChangeCancelDateComplete(transaction, changeCancelDateResponse.getPhn());

			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}
	/**
	 * Extend the premium cancellation date of an employee. Maps to the legacy R51(z25).
	 * 
	 * @param extendCancelDateRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/extend-cancel-date")
	public ResponseEntity<ExtendCancelDateResponse> extendCancelDate(
			@Valid @RequestBody ExtendCancelDateRequest extendCancelDateRequest, HttpServletRequest request) {

		Transaction transaction = auditExtendCancelDateStart(extendCancelDateRequest.getPhn(), extendCancelDateRequest.getGroupNumber(), request);

		try {
			RPBSPAG0Converter converter = new RPBSPAG0Converter();
			RPBSPAG0 rpbspag0Request = converter.convertRequest(extendCancelDateRequest);
			RPBSPAG0 rpbspag0Response = maintenanceService.extendCancelDate(rpbspag0Request, transaction);
			ExtendCancelDateResponse extendCancelDateResponse = converter
					.convertExtendCancelDateResponse(rpbspag0Response);

			ResponseEntity<ExtendCancelDateResponse> response = ResponseEntity.ok(extendCancelDateResponse);

			logger.info("extendCancelDateResponse response: {} ", extendCancelDateResponse);

			auditExtendCancelDateComplete(transaction, extendCancelDateResponse.getPhn());

			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}
	
	/**
	 * Reinstate the cancelled group coverage. Maps to the legacy R44.
	 * @param reinstateCancelledGroupCoverageRequest
	 * @param request
	 * @return The result of the operation.
	 */
	@PostMapping("/reinstate-cancelled-group-coverage")
	public ResponseEntity<ReinstateCancelledGroupCoverageResponse> reinstateCancelledGroupCoverage(
			@Valid @RequestBody ReinstateCancelledGroupCoverageRequest reinstateCancelledGroupCoverageRequest, HttpServletRequest request) {

		Transaction transaction = auditReinstateCancelledCoverage(reinstateCancelledGroupCoverageRequest.getPhn(), reinstateCancelledGroupCoverageRequest.getGroupNumber(), request);

		try {
			RPBSPRH0Converter converter = new RPBSPRH0Converter();
			RPBSPRH0 rpbsprh0Request = converter.convertRequest(reinstateCancelledGroupCoverageRequest);
			RPBSPRH0 rpbsprh0Response = maintenanceService.reinstateCancelledCoverage(rpbsprh0Request, transaction);
			ReinstateCancelledGroupCoverageResponse reinstateCancelledGroupCoverageResponse = converter
					.convertResponse(rpbsprh0Response);

			ResponseEntity<ReinstateCancelledGroupCoverageResponse> response = ResponseEntity.ok(reinstateCancelledGroupCoverageResponse);

			logger.info("reinstateCancelledGroupCoverageResponse response: {} ", reinstateCancelledGroupCoverageResponse);

			auditReinstateCancelledCoverageComplete(transaction, reinstateCancelledGroupCoverageResponse.getPhn());

			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}
	
	/**
	 * Reinstates coverage for an coverage dependent. Maps to the legacy R43.
	 * 
	 * @param changeEffectiveDateRequest
	 * @return The result of the operation.
	 */
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
			addAffectedParty(transaction, IdentifierType.PHN, reinstateResponse.getPhn(), AffectedPartyDirection.OUTBOUND);
	
			return response;	
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}
	
	/**
	 * Renew the coverage effective date of an employee and spouse/dependents.
	 * Maps to the legacy R45 (Z27).
	 * 
	 * @param RenewCancelledGroupCoverageRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/renew-cancelled-group-coverage")
	public ResponseEntity<RenewCancelledGroupCoverageResponse> renewCancelledGroupCoverage(
			@Valid @RequestBody RenewCancelledGroupCoverageRequest renewCancelledGroupCoverageRequest,
			HttpServletRequest request) {

		Transaction transaction = auditRenewCancelledCoverageStart(renewCancelledGroupCoverageRequest.getPhn(),
				request);
		
		try {
			RPBSPAI0Converter converter = new RPBSPAI0Converter();
			RPBSPAI0 rpbspai0Request = converter.convertRequest(renewCancelledGroupCoverageRequest);
			RPBSPAI0 rpbspai0Response = maintenanceService.renewCoverageEffectiveDate(rpbspai0Request, transaction);
			RenewCancelledGroupCoverageResponse renewCancelledGroupCoverageResponse;
			
			// Execute if RPBSPAI0 returns successfully
			if (StringUtils.equals(rpbspai0Response.getRpbsHeader().getStatusCode(), BaseRapidConverter.STATUS_CODE_SUCCESS)) {
				RPBSPXP0Converter rpbspxp0Converter = new RPBSPXP0Converter();

				// Creates/Clones a new Coverage entry, not simply an update but an Add
				AddGroupMemberRequest addGroupMemberRequest = converter.buildAddGroupMemberRequest(rpbspai0Response);
				RPBSPXP0 rpbspxp0 = rpbspxp0Converter.convertRequest(addGroupMemberRequest);

				RPBSPXP0 rpbspxp0Response = groupMemberService.addGroupMember(rpbspxp0, transaction);
				renewCancelledGroupCoverageResponse = rpbspxp0Converter.convertResponseForRenewal(rpbspxp0Response);
			} else {
				renewCancelledGroupCoverageResponse = converter.convertResponse(rpbspai0Response);
			}

			ResponseEntity<RenewCancelledGroupCoverageResponse> response = ResponseEntity
					.ok(renewCancelledGroupCoverageResponse);

			logger.info("RenewCancelledGroupCoverageResponse response: {} ", response);

			auditRenewCancelledCoverageComplete(transaction, renewCancelledGroupCoverageResponse.getPhn());

			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	private Transaction auditChangeEffectiveDateStart(ChangeEffectiveDateRequest changeEffectiveDateRequest,
			HttpServletRequest request) {

		Transaction transaction = transactionStart(request, TransactionType.CHANGE_EFFECTIVE_DATE);

		if (StringUtils.isNotBlank(changeEffectiveDateRequest.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, changeEffectiveDateRequest.getPhn(),
					AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}

	private void auditChangeEffectiveDateComplete(Transaction transaction,
			ChangeEffectiveDateResponse changeEffectiveDateResponse) {

		transactionComplete(transaction);

		if (StringUtils.isNotBlank(changeEffectiveDateResponse.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, changeEffectiveDateResponse.getPhn(),
					AffectedPartyDirection.OUTBOUND);
		}
	}

	private Transaction auditExtendCancelDateStart(String phn, String groupNumber, HttpServletRequest request) {

		Transaction transaction = transactionStart(request, TransactionType.EXTEND_CANCEL_DATE);

		if (StringUtils.isNotBlank(phn)) {
			addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(groupNumber)) {
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, groupNumber, AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}

	private void auditExtendCancelDateComplete(Transaction transaction, String phn) {

		transactionComplete(transaction);

		if (StringUtils.isNotBlank(phn)) {
			addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.OUTBOUND);
		}
	}
	
	private Transaction auditReinstateCancelledCoverage(String phn, String groupNumber, HttpServletRequest request) {

		Transaction transaction = transactionStart(request, TransactionType.REINSTATE_CANCELLED_COVERAGE);

		if (StringUtils.isNotBlank(phn)) {
			addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(groupNumber)) {
			addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, groupNumber, AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}
	
	private void auditReinstateCancelledCoverageComplete(Transaction transaction, String phn) {

		transactionComplete(transaction);

		if (StringUtils.isNotBlank(phn)) {
			addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.OUTBOUND);
		}
	}
	
	private Transaction auditChangeCancelDateStart(String phn, HttpServletRequest request) {

		Transaction transaction = transactionStart(request, TransactionType.CHANGE_CANCEL_DATE);

		if (StringUtils.isNotBlank(phn)) {
			addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}

	private void auditChangeCancelDateComplete(Transaction transaction, String phn) {

		transactionComplete(transaction);

		if (StringUtils.isNotBlank(phn)) {
			addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.OUTBOUND);
		}
	}

	private Transaction auditReinstateOverAgeDependentStart(ReinstateOverAgeDependentRequest reinstateRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.REINSTATE_OVER_AGE_DEPENDENT);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, reinstateRequest.getGroupNumber(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, reinstateRequest.getPhn(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, reinstateRequest.getDependentPhn(), AffectedPartyDirection.INBOUND);
		return transaction;
	}
	
	private Transaction auditRenewCancelledCoverageStart(String phn, HttpServletRequest request) {

		Transaction transaction = transactionStart(request, TransactionType.RENEW_CANCELLED_COVERAGE);

		if (StringUtils.isNotBlank(phn)) {
			addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}
	
	private void auditRenewCancelledCoverageComplete(Transaction transaction, String phn) {

		transactionComplete(transaction);

		if (StringUtils.isNotBlank(phn)) {
			addAffectedParty(transaction, IdentifierType.PHN, phn, AffectedPartyDirection.OUTBOUND);
		}
	}

}
