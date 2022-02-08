package ca.bc.gov.hlth.hnweb.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.ErrorLevel;
import ca.bc.gov.hlth.hnweb.persistence.entity.EventMessage;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEvent;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;
import ca.bc.gov.hlth.hnweb.persistence.repository.AffectedPartyRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.EventMessageRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.TransactionEventRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.TransactionRepository;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.security.UserInfo;

/**
 * Service for working with Auditing via Transaction tables.
 */
@Service
public class AuditService {
	private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
	
	@Autowired
	private AffectedPartyRepository affectedPartyRepository;
	
	@Autowired
	private EventMessageRepository eventMessageRepository;

	@Autowired
	private TransactionEventRepository transactionEventRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

	/**
	 * Creates a new {@link Transaction}.
	 * 
	 * @param sourceIP Source IP address
	 * @param type The type of transaction
	 * @return The persisted Transaction
	 */
	public Transaction createTransaction(String sourceIP, TransactionType type) {
		Transaction transaction = new Transaction();
		UserInfo userInfo = null;
		try {
			// This can throw an exception under certain auth failures
			// E.g. if an empty or invalid token is provided
			userInfo = SecurityUtil.loadUserInfo();	
		} catch (Exception e) {
			// Ignore
		}
		transaction.setOrganization(userInfo != null ? userInfo.getOrganization(): null);
		transaction.setServer(getServer());
		transaction.setSessionId(userInfo != null ? userInfo.getSessionState(): null);
		transaction.setSourceIp(sourceIP);
		transaction.setStartTime(new Date());
		transaction.setTransactionId(UUID.randomUUID());
		transaction.setType(type.getValue());
		transaction.setUserId(userInfo != null ? userInfo.getUsername() : null);
		return transactionRepository.save(transaction);
	}

	private String getServer() {		
		String hostname = "";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
			logger.debug("The hostname is {}", hostname);
        } catch (UnknownHostException e) {
			logger.warn("Could not get server name");
		}
		return hostname;
	}

	/**
	 * Creates a new {@link TransactionEvent}.
	 * 
	 * @param transaction The associated Transaction
	 * @param eventType The type of event
	 * @return The persisted TransactionEvent.
	 */
	public TransactionEvent createTransactionEvent(Transaction transaction, TransactionEventType eventType) {
		return createTransactionEvent(transaction, eventType, null);
	}

	/**
	 * Creates a new {@link TransactionEvent}.
	 * 
	 * @param transaction The associated Transaction
	 * @param eventType The type of event
	 * @param messageId The associated message ID
	 * @return The persisted TransactionEvent.
	 */
	public TransactionEvent createTransactionEvent(Transaction transaction, TransactionEventType eventType, String messageId) {
		TransactionEvent transactionEvent = new TransactionEvent();
		transactionEvent.setEventTime(new Date());
		transactionEvent.setMessageId(messageId);
		transactionEvent.setTransaction(transaction);
		transactionEvent.setType(eventType.getValue());
		return transactionEventRepository.save(transactionEvent);
	}
	
	/**
	 * Creates a new {@link EventMessage}.
	 * 
	 * @param transactionEvent The associated TransactionEvent
	 * @param level The level of the event (e.g.ERROR)
	 * @param status The HTTP Status code related to the message
	 */
	public void createEventMessage(TransactionEvent transactionEvent, ErrorLevel level, HttpStatus status) {
    	EventMessage eventMessage = new EventMessage();
    	eventMessage.setErrorCode(Integer.toString(status.value()));
    	eventMessage.setErrorLevel(level);
    	eventMessage.setMessageText(status.getReasonPhrase());
    	eventMessage.setTransactionEvent(transactionEvent);
		eventMessageRepository.save(eventMessage);
	}

	/**
	 * Creates a new {@link EventMessage}.
	 * 
	 * @param transactionEvent The associated TransactionEvent
	 * @param level The level of the event (e.g.ERROR)
	 * @param exception The associated exception
	 * @param status The HTTP Status code related to the message
	 */
	public void createEventMessage(TransactionEvent transactionEvent, ErrorLevel level, HttpStatus status, Exception exception) {
    	EventMessage eventMessage = new EventMessage();
    	eventMessage.setErrorCode(Integer.toString(status.value()));
    	eventMessage.setErrorLevel(level);
    	eventMessage.setMessageText(exception.getMessage());
    	eventMessage.setTransactionEvent(transactionEvent);
		eventMessageRepository.save(eventMessage);
	}

	/**
	 * Creates a new {@link AffectedParty}.
	 * 
	 * @param transaction The associated Transaction
	 * @param phn The phn of the affected party
	 */
	public void createAffectedParty(Transaction transaction, String phn) {
		createAffectedParty(transaction, IdentifierType.PHN, phn);
	}
	
	/**
	 * Creates a new {@link AffectedParty}.
	 * 
	 * @param transaction The associated Transaction
	 * @param identifierType The type of identifier
	 * @param identifier The value of the identifier
	 */
	public void createAffectedParty(Transaction transaction, IdentifierType identifierType, String identifier) {
		AffectedParty affectedParty = new AffectedParty();
		affectedParty.setIdentifier(identifier);
		affectedParty.setIdentifierType(identifierType.getValue());
		affectedParty.setTransaction(transaction);
		affectedPartyRepository.save(affectedParty);
	}
	
}
