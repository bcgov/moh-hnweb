package ca.bc.gov.hlth.hnweb.service;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;

public class BaseService {

	@Autowired
	private AuditService auditService;
	
	protected void messageSent(Transaction transaction) {
		messageSent(transaction, null);
	}
	
	protected void messageSent(Transaction transaction, String messageId) {
		auditService.createTransactionEvent(transaction, TransactionEventType.MESSAGE_SENT, messageId);
	}
	
	protected void messageReceived(Transaction transaction) {
		messageReceived(transaction, null);
	}
	protected void messageReceived(Transaction transaction, String messageId) {
		auditService.createTransactionEvent(transaction, TransactionEventType.MESSAGE_RECEIVED, messageId);
	}

}
