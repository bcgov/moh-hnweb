package ca.bc.gov.hlth.hnweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.persistence.entity.ErrorLevel;
import ca.bc.gov.hlth.hnweb.persistence.entity.EventMessage;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEvent;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.AuditService;

public abstract class BaseController {
	
	@Autowired
	private AuditService auditService;
	
	protected Transaction transactionStart(HttpServletRequest request, TransactionType type, String identifier) {
		Transaction transaction = auditService.createTransaction(request.getRemoteAddr(), type);
		auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_START);
		auditService.createAffectedParty(transaction, identifier);
		return transaction;
	}
	
	protected Transaction transactionStart(HttpServletRequest request, TransactionType type, List<String> identifiers) {
		Transaction transaction = auditService.createTransaction(request.getRemoteAddr(), type);
		auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_START);
		identifiers.forEach(id -> {
			auditService.createAffectedParty(transaction, id);	
		});
		
		return transaction;
	}
	
	protected void transactionEnd(Transaction transaction, String identifier) {
		auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_COMPLETE);
		auditService.createAffectedParty(transaction, identifier);
	}
	
	protected void transactionEnd(Transaction transaction, List<String> identifiers) {
		auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_COMPLETE);
		identifiers.forEach(identifier -> {
			auditService.createAffectedParty(transaction, identifier);	
		});		
	}
	
	protected void createTransactionEvent(Transaction transaction, TransactionEventType eventType) {
		auditService.createTransactionEvent(transaction, eventType);
	}
	
	protected void transactionError(Transaction transaction, Throwable t) {
		TransactionEvent transactionEvent = auditService.createTransactionEvent(transaction, TransactionEventType.ERROR);
		EventMessage eventMessage = new EventMessage();
		// TODO (weskubo-cgi) Do we need an error code?
		eventMessage.setErrorCode(null);
		eventMessage.setErrorLevel(ErrorLevel.ERROR);
		eventMessage.setMessageText(t.getMessage());
		eventMessage.setTransactionEvent(transactionEvent);
		auditService.createEventMessage(eventMessage);
	}
	
	protected void handleException(Transaction transaction, Exception e) throws ResponseStatusException {
		transactionError(transaction, e);
		if (e instanceof HNWebException) {
			HNWebException hwe = (HNWebException)e;
			transactionError(transaction, hwe);
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);				
			}
		} else if (e instanceof WebClientException) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

}
