package ca.bc.gov.hlth.hnweb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.bc.gov.hlth.hnweb.exception.ExceptionType;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
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

@SpringBootTest
public class AuditServiceTest {
	
	@Autowired
	private AffectedPartyRepository affectedPartyRepository;
	
	@Autowired
	private EventMessageRepository eventMessageRepository;
	
	@Autowired
	private TransactionEventRepository transactionEventRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AuditService auditService;
	
	private static MockedStatic<SecurityUtil> mockStatic;
	
	@BeforeAll
    static void setUp() {
        mockStatic = Mockito.mockStatic(SecurityUtil.class);
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "14100f9b-7daa-4938-a833-c8c56a5988e9", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));
    }
	
    @AfterAll
    static void tearDown() {
        mockStatic.close();
    }
	
	@Test
	public void testCreateTransaction() {
		Transaction newTransaction = auditService.createTransaction("0:0:0:0:0:0:0:1", TransactionType.CHECK_ELIGIBILITY);

		Optional<Transaction> optional = transactionRepository.findById(newTransaction.getTransactionId());
		assertTrue(optional.isPresent());
		
		Transaction transaction = optional.get();
		assertEquals("00000010", transaction.getOrganization());
		assertNotNull(transaction.getServer());
		assertNotNull(transaction.getSessionId());
		assertEquals("0:0:0:0:0:0:0:1", transaction.getSourceIp());
		assertNotNull(transaction.getStartTime());
		assertNotNull(transaction.getTransactionId());
		assertEquals(TransactionType.CHECK_ELIGIBILITY.getValue(), transaction.getType());
		assertEquals("unittest", transaction.getUserId());
	}

	@Test
	public void testCreateTransactionEvent() {
		String messageId = StringUtils.substring(UUID.randomUUID().toString(), 0, 20);
		Transaction transaction = auditService.createTransaction("0:0:0:0:0:0:0:1", TransactionType.CHECK_ELIGIBILITY);

		TransactionEvent newTransactionEvent = auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_START, messageId);

		Optional<TransactionEvent> optional = transactionEventRepository.findById(newTransactionEvent.getTransactionEventId());
		assertTrue(optional.isPresent());
		
		TransactionEvent transactionEvent = optional.get();		
		assertNotNull(transactionEvent.getEventTime());
		assertEquals(messageId, transactionEvent.getMessageId());
		assertEquals(transaction, transactionEvent.getTransaction());
		assertNotNull(transactionEvent.getTransactionEventId());
		assertEquals(TransactionEventType.TRANSACTION_START.getValue(), transactionEvent.getType());
	}
	
	@Test
	public void testCreateAffectedParty() {
		Transaction transaction = auditService.createTransaction("0:0:0:0:0:0:0:1", TransactionType.CHECK_ELIGIBILITY);
		AffectedParty newAffectedParty = auditService.createAffectedParty(transaction, IdentifierType.GROUP_NUMBER, "6337109", AffectedPartyDirection.OUTBOUND);
		
		Optional<AffectedParty> optional = affectedPartyRepository.findById(newAffectedParty.getAffectedPartyId());
		assertTrue(optional.isPresent());
		
		AffectedParty affectedParty = optional.get();		
		assertNotNull(affectedParty.getAffectedPartyId());
		assertEquals("6337109", affectedParty.getIdentifier());
		assertEquals(IdentifierType.GROUP_NUMBER.getValue(), affectedParty.getIdentifierType());
		assertEquals(AffectedPartyDirection.OUTBOUND.getValue(), affectedParty.getDirection());
		assertEquals(transaction, affectedParty.getTransaction());
	}
	
	@Test
	public void testCreateEventMessage() {
		HNWebException exception = new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		Transaction transaction = auditService.createTransaction("0:0:0:0:0:0:0:1", TransactionType.CHECK_ELIGIBILITY);
		TransactionEvent transactionEvent = auditService.createTransactionEvent(transaction, TransactionEventType.TRANSACTION_START, UUID.randomUUID().toString());
		EventMessage newEventMessage = auditService.createEventMessage(transactionEvent, ErrorLevel.ERROR, HttpStatus.INTERNAL_SERVER_ERROR, exception);
		
		Optional<EventMessage> optional = eventMessageRepository.findById(newEventMessage.getEventMessageId());
		assertTrue(optional.isPresent());
		
		EventMessage eventMessage = optional.get();
		assertEquals(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), eventMessage.getErrorCode());
		assertEquals(ErrorLevel.ERROR, eventMessage.getErrorLevel());
		assertNotNull(eventMessage.getEventMessageId());
		assertEquals(ExceptionType.DOWNSTREAM_FAILURE.getMessage(), eventMessage.getMessageText());
		assertEquals(transactionEvent, eventMessage.getTransactionEvent());
	}

}
