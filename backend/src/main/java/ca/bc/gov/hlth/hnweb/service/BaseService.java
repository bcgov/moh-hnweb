package ca.bc.gov.hlth.hnweb.service;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;

public class BaseService {

	@Autowired
	private AuditService auditService;
	
	protected void messageSent(Transaction transaction) {
		auditService.createTransactionEvent(transaction, TransactionEventType.MESSAGE_SENT, transaction.getTransactionId().toString());
	}
	
	protected void messageReceived(Transaction transaction) {
		auditService.createTransactionEvent(transaction, TransactionEventType.MESSAGE_RECEIVED);
	}
	
	
}
