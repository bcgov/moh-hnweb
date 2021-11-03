package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.hlth.hnweb.model.CatFact;
import ca.uhn.hl7v2.HL7Exception;

/**
 * TODO (weskubo-cgi) Remove this class. It's just for unit testing demonstration purposes.
 */
@SpringBootTest
public class CatControllerTest {

	/**
	 * Inject the restTemplate. This needs to be the same bean used by the actual Service in order
	 * for the mocking to work.
	 */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Inject the Controller being tested using the standard @Autowired annotation.
	 */
	@Autowired
	private CatController catController;

	private MockRestServiceServer mockServer;

	private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    /**
     * This test doesn't have the mockServer setup so it will hit the live server. Since the live server won't
     * return our unit test cat fact (we assume), this test will fail.
     * 
     * Remove the @Disabled annotation to run.
     * 
     * @throws HL7Exception
     * @throws IOException
     * @throws ParseException
     * @throws URISyntaxException
     */
	@Test
	@Disabled
	public void testGetFact_liveServer_fail() throws HL7Exception, IOException, ParseException, URISyntaxException {
		CatFact fact = new CatFact();
		fact.setFact("This is a unit test fact");
		
		ResponseEntity<CatFact> response = catController.getFact();
		assertEquals("This is a unit test fact", response.getBody().getFact());
	}
    
	/**
	 * Tests that contoller all the way through to a request to the mock server.
	 * 
	 * @throws HL7Exception
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetFact_mockServer_success_ok() throws HL7Exception, IOException, ParseException, URISyntaxException {
		// 1. Set up our test data
		CatFact fact = new CatFact();
		fact.setFact("This is a unit test fact");
		
		// 2. Define the behaviour and expected results on the mockServer
		mockServer.expect(ExpectedCount.once(), 
	          requestTo(new URI("https://catfact.ninja/fact")))
	          .andExpect(method(HttpMethod.GET))
	          .andRespond(withStatus(HttpStatus.OK)
	          .contentType(MediaType.APPLICATION_JSON)
	          .body(mapper.writeValueAsString(fact))
	        ); 
		

		ResponseEntity<CatFact> response = catController.getFact();
		
		// 3. Assert the server behaviour
		mockServer.verify();
		
		// 4. Assert the response		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("This is a unit test fact", response.getBody().getFact());
	}
	
	/**
	 * This test expects that the mockServer will receive a POST request. Since getFact is a GET request
	 * the test will fail.
	 * Remove the @Disabled annotation to run.
	 * 
	 * @throws HL7Exception
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	@Test
	@Disabled
	public void testGetFact_mockServer_wrong_http_method() throws HL7Exception, IOException, ParseException, URISyntaxException {
		// 1. Set up our test data
		CatFact fact = new CatFact();
		fact.setFact("This is a unit test fact");
		
		// 2. Define the behaviour and expected results on the mockServer
		mockServer.expect(ExpectedCount.once(), 
	          requestTo(new URI("https://catfact.ninja/fact")))
	          .andExpect(method(HttpMethod.POST))
	          .andRespond(withStatus(HttpStatus.OK)
	          .contentType(MediaType.APPLICATION_JSON)
	          .body(mapper.writeValueAsString(fact))
	        ); 
		

		ResponseEntity<CatFact> response = catController.getFact();
		
		// 3. Assert the server behaviour
		mockServer.verify();
		
		// 4. Assert the response		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("This is a unit test fact", response.getBody().getFact());
	}

}
