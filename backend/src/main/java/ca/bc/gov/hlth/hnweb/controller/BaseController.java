package ca.bc.gov.hlth.hnweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.ErrorLevel;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEvent;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.AuditService;

public abstract class BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	private AuditService auditService;

	/**
	 * Audits the Transaction Start event.
	 * 
	 * @param request The HTTP request
	 * @param type The type of transaction
	 * @return
	 */
	protected Transaction transactionStart(HttpServletRequest request, TransactionType type) {
		Transaction transaction = auditService.createTransaction(request.getRemoteAddr(), type);
		auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_START);
		return transaction;
	}
	
	/**
	 * Adds an AffectedParty to the Transaction.
	 * 
	 * @param transaction The transaction
	 * @param type The type of identifier. E.g. PHN
	 * @param identifier The value of the identifier
	 */
	protected void addAffectedParty(Transaction transaction, IdentifierType type, String identifier, AffectedPartyDirection direction) {
		if (StringUtils.isEmpty(identifier)) {
			logger.info("Unable to audit transaction for identifier type {} due to a possible {} failure", type, direction);
			return;
		}
		auditService.createAffectedParty(transaction, type, identifier, direction);
	}
	
	/**
	 * Audits the Transaction Complete event.
	 * 
	 * @param transaction The transaction
	 */
	protected void transactionComplete(Transaction transaction) {
		auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_COMPLETE);
	}
	
	/**
	 * Handles exceptions generated during handling the request.
	 * 
	 * @param transaction The request
	 * @param exception The exception
	 * @throws ResponseStatusException Used to generate the response to the client
	 */
	protected void handleException(Transaction transaction, Exception exception) throws ResponseStatusException {
		logger.error("handleException: {}", exception.getMessage());

		HttpStatus status;
		if (exception instanceof HNWebException) {
			HNWebException hwe = (HNWebException)exception;
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
			default:
				status = HttpStatus.BAD_REQUEST;		
			}
		} else if (exception instanceof WebClientException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		transactionError(transaction, status, exception);
		transactionComplete(transaction);
		throw new ResponseStatusException(status, exception.getMessage(), exception);
	}

	/**
	 * Audits an Error event.
	 * 
	 * @param transaction The transaction
	 * @param status The HTTP status code
	 * @param exception The exception
	 */
	private void transactionError(Transaction transaction, HttpStatus status, Exception exception) {
		TransactionEvent transactionEvent = auditService.createTransactionEvent(transaction, TransactionEventType.ERROR);
		auditService.createEventMessage(transactionEvent, ErrorLevel.ERROR, status, exception);
	}
	
}
