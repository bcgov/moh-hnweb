package ca.bc.gov.hlth.hnweb.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.EventMessage;
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

	public Transaction createTransaction(String sourceIP, TransactionType type) {
		Transaction transaction = new Transaction();
		UserInfo userInfo = SecurityUtil.loadUserInfo();
		transaction.setOrganization(userInfo.getOrganization());
		transaction.setServer(getServer());
		transaction.setSessionId(userInfo.getSessionState());
		transaction.setSourceIp(sourceIP);
		transaction.setStartTime(new Date());
		transaction.setTransactionId(UUID.randomUUID());
		transaction.setType(type.getValue());
		transaction.setUserId(userInfo.getUsername());
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

	public TransactionEvent createTransactionEvent(Transaction transaction, TransactionEventType eventType) {
		return createTransactionEvent(transaction, eventType, null);
	}

	public TransactionEvent createTransactionEvent(Transaction transaction, TransactionEventType eventType, String messageId) {
		TransactionEvent transactionEvent = new TransactionEvent();
		transactionEvent.setEventTime(new Date());
		transactionEvent.setMessageId(messageId);
		transactionEvent.setTransaction(transaction);
		transactionEvent.setType(eventType.getValue());
		return transactionEventRepository.save(transactionEvent);
	}
	
	public void createEventMessage(EventMessage eventMessage) {
		eventMessageRepository.save(eventMessage);
	}
	
	public void createAffectedParty(Transaction transaction, String identifier) {
		//public static final String BCPHN = "BCPHN";

	    //public static final String STATUS_CODE_ACTIVE = "active";
		AffectedParty affectedParty = new AffectedParty();
		affectedParty.setIdentifier(identifier);
		// TODO (weskubo-cgi) What is identifier source?
		affectedParty.setIdentifierSource("BC");
		// TODO (weskubo-cgi) What is identifier type?
		affectedParty.setIdentifierType("BCPHN");
		affectedParty.setStatus("active");
		affectedParty.setTransaction(transaction);
		affectedPartyRepository.save(affectedParty);
	}
	
	
}
