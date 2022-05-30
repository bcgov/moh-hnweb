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

import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPAJ0Converter;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAJ0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.MaintenanceService;

/**
 * Handles request related to coverage maintenance. These will include:
 * <ul>
 * <li>Z26
 * <li>Change Effective Date
 * <ul>
 *
 */
@RequestMapping("/maintenance")
@RestController
public class MaintenanceController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MaintenanceController.class);

	@Autowired
	private MaintenanceService maintenanceService;

	/**
	 * Changes coverage effective date for the group number. Maps to the legacy R46.
	 * 
	 * @param addDependentRequest
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
			addAffectedParty(transaction, IdentifierType.PHN, changeEffectiveDateResponse.getPhn(),
					AffectedPartyDirection.OUTBOUND);

			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	private Transaction auditChangeEffectiveDateStart(ChangeEffectiveDateRequest changeEffectiveDateRequest,
			HttpServletRequest request) {

		Transaction transaction = transactionStart(request, TransactionType.ENROLL_SUBSCRIBER);
		// Some requests do not contain the PHN e.g R50 z05 as it is Enroll subscriber
		// without PHN
		if (StringUtils.isNotBlank(changeEffectiveDateRequest.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, changeEffectiveDateRequest.getPhn(),
					AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}

	private void auditChangeEffectiveDateComplete(Transaction transaction,
			ChangeEffectiveDateResponse changeEffectiveDateResponse) {

		transactionComplete(transaction);
		// Some responses do not contain the PHN e.g. in the case of R50 z06 it is just
		// an ACK
		if (StringUtils.isNotBlank(changeEffectiveDateResponse.getPhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, changeEffectiveDateResponse.getPhn(),
					AffectedPartyDirection.OUTBOUND);
		}
	}

}
