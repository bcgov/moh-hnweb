package ca.bc.gov.hlth.hnweb;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Base Spring Boot Test class for all Controller Test classes to extend from.
 *
 */
@SpringBootTest
public class BaseControllerTest {

	public MockHttpServletRequest createHttpServletRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRemoteAddr("1.1.1.1");
		return request;
	}
	
}
