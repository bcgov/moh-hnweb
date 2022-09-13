package ca.bc.gov.hlth.hnweb.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.ErrorLevel;
import ca.bc.gov.hlth.hnweb.persistence.entity.EventMessage;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Organization;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEvent;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;
import ca.bc.gov.hlth.hnweb.persistence.repository.AffectedPartyPageableRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.AffectedPartyRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.EventMessageRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.OrganizationRepository;
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

	private static final String DEFAULT_SORT = "transaction.startTime";

	private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

	@Autowired
	private AffectedPartyRepository affectedPartyRepository;
	
	@Autowired
	private AffectedPartyPageableRepository affectedPartyPageableRepository;
	
	@Autowired
	private EventMessageRepository eventMessageRepository;

	@Autowired
	private TransactionEventRepository transactionEventRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
		
	@Autowired
	private OrganizationRepository organizationRepository;
	
	/** Maps simple sort names to their JPA equivalent */
	private static Map<String, String> sortMap = new HashMap<>();
	
	static {
		sortMap.put("affectedPartyId", "identifier");
		sortMap.put("affectedPartyType", "identifierType");
		sortMap.put("organization", "transaction.organization");
		sortMap.put("transactionStartTime", "transaction.startTime");
		sortMap.put("type", "transaction.type");
		sortMap.put("userId", "transaction.userId");
	}

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
	public EventMessage createEventMessage(TransactionEvent transactionEvent, ErrorLevel level, HttpStatus status) {
    	EventMessage eventMessage = new EventMessage();
    	eventMessage.setErrorCode(Integer.toString(status.value()));
    	eventMessage.setErrorLevel(level);
    	eventMessage.setMessageText(status.getReasonPhrase());
    	eventMessage.setTransactionEvent(transactionEvent);
		return eventMessageRepository.save(eventMessage);
	}

	/**
	 * Creates a new {@link EventMessage}.
	 * 
	 * @param transactionEvent The associated TransactionEvent
	 * @param level The level of the event (e.g.ERROR)
	 * @param exception The associated exception
	 * @param status The HTTP Status code related to the message
	 */
	public EventMessage createEventMessage(TransactionEvent transactionEvent, ErrorLevel level, HttpStatus status, Exception exception) {
    	EventMessage eventMessage = new EventMessage();
    	eventMessage.setErrorCode(Integer.toString(status.value()));
    	eventMessage.setErrorLevel(level);
    	eventMessage.setMessageText(exception.getMessage());
    	eventMessage.setTransactionEvent(transactionEvent);
		return eventMessageRepository.save(eventMessage);
	}

	/**
	 * Creates a new {@link AffectedParty}.
	 * 
	 * @param transaction The associated Transaction
	 * @param phn The phn of the affected party
	 * @param direction The value to indicate if the party is affected when the transaction is being sent or being received.
	 */
	public AffectedParty createAffectedParty(Transaction transaction, String phn, AffectedPartyDirection direction) {
		return createAffectedParty(transaction, IdentifierType.PHN, phn, direction);
	}
	
	/**
	 * Creates a new {@link AffectedParty}.
	 * 
	 * @param transaction The associated Transaction
	 * @param identifierType The type of identifier
	 * @param identifier The value of the identifier
	 */
	public AffectedParty createAffectedParty(Transaction transaction, IdentifierType identifierType, String identifier, AffectedPartyDirection direction) {
		AffectedParty affectedParty = new AffectedParty();
		affectedParty.setIdentifier(identifier);
		affectedParty.setIdentifierType(identifierType.getValue());
		affectedParty.setDirection(direction.getValue());
		affectedParty.setTransaction(transaction);
		return affectedPartyRepository.save(affectedParty);
	}
	
	/**
	 * Retrieves distinct organization for audit report.
	 * @return list of organization.
	 */
	public List<Organization> getOrganizations() {
		return organizationRepository.findAll(Sort.by("organization"));
	}

	/**
	 * Retrieves audit records for the given search parameters
	 * @param types Transaction types
	 * @param organizations
	 * @param userId
	 * @param direction
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Page<AffectedParty> getAffectedParties(List<String> types, List<String> organizations, String userId, LocalDate startDate,
			LocalDate endDate, int page, int rows, String sortField, String sortDirection) {
		logger.info("Querying page {} with {} rows", page, rows);
		try {
			Date formattedStartDate = convertLocalDateToDate(startDate);
			Date formattedEndDate = convertLocalDateToDate(endDate);			

			String property = sortMap.get(sortField);
			if (StringUtils.isBlank(property)) {
				property = DEFAULT_SORT;
			}
			Direction direction = StringUtils.isNotEmpty(sortDirection) ? Direction.valueOf(sortDirection) : Direction.DESC;
			Sort sort = Sort.by(direction, property);
			
			Pageable pageable = PageRequest.of(page, rows, sort);
			
			return affectedPartyPageableRepository.findByTransactionAndDirection(types, organizations, userId, AffectedPartyDirection.INBOUND.getValue(), formattedStartDate,
					formattedEndDate, pageable);
		} catch (ParseException e) {
			logger.error(e.getLocalizedMessage());
			return null;
		}		

	}

	private Date convertLocalDateToDate(LocalDate date) throws ParseException {
		return new SimpleDateFormat(LOCAL_DATE_FORMAT).parse(date.toString());
	}
	
}
