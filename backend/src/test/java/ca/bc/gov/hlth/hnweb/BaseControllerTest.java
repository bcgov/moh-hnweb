package ca.bc.gov.hlth.hnweb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.repository.AffectedPartyRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.TransactionRepository;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Base Spring Boot Test class for all Controller Test classes to extend from.
 *
 */
@SpringBootTest
@Transactional
public class BaseControllerTest {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AffectedPartyRepository affectedPartyRepository;
	
	protected static MockWebServer mockBackEnd;
	
	private static MockedStatic<SecurityUtil> mockStatic;
	
	@BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(0);
        
        mockStatic = Mockito.mockStatic(SecurityUtil.class);
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "00000010", "hnweb-user", UUID.randomUUID().toString()));
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
        mockStatic.close();
    }

	protected MockHttpServletRequest createHttpServletRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRemoteAddr("1.1.1.1");
		return request;
	}
	
	/**
	 * Asserts the Transaction is created.
	 * 
	 * @param type The type of transaction.
	 * @return The Transaction to perform further assertions on.
	 */
	protected Transaction assertTransactionCreated(TransactionType type) {
		Transaction example = new Transaction();
		example.setType(type.getValue());
		Optional<Transaction> opt = transactionRepository.findOne(Example.of(example));
		assertTrue(opt.isPresent());
		return opt.get();
	}
	
	/**
	 * Asserts correct number of AffectedParty entries has been created.
	 * @param count 
	 * 
	 * @param direction the direction of the AffectedParty being logged.
	 * @count the expected number of AffectedPartys
	 * 
	 * @return The number of AffectedPartys found for the specified direction.
	 */
	protected int assertAffectedParyCount(AffectedPartyDirection direction, int count) {
		AffectedParty example = new AffectedParty();
		example.setAffectedPartyId(0);
		example.setDirection(direction.getValue());
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnorePaths("affectedPartyId"); // Remove affectedPartyId as its value would default to 0 because it's of type long so would not be ignored as null in the default matchingAll() matcher
		List<AffectedParty> affectedParties = affectedPartyRepository.findAll(Example.of(example, matcher));
		assertEquals(count, affectedParties.size());
		return affectedParties.size();
	}
	
}
