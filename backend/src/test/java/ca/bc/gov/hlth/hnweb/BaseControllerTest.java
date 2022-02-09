package ca.bc.gov.hlth.hnweb;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
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
	 * Asserts the the Transaction is created.
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
	
}
