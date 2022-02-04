package ca.bc.gov.hlth.hnweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
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
	
	protected void transactionEnd(Transaction transaction) {
		auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_COMPLETE);
	}
	
	protected void createTransactionEvent(Transaction transaction, TransactionEventType eventType) {
		auditService.createTransactionEvent(transaction, eventType);
	}

}
