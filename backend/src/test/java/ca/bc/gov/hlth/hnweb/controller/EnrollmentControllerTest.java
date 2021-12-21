package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.GetNameSearchRequest;
import ca.bc.gov.hlth.hnweb.model.GetNameSearchResponse;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.utils.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * JUnit test class for EnrollmentController
 *
 */
@SpringBootTest
public class EnrollmentControllerTest {

	private static final String ACK_ERROR = "MSH|^~\\&|RAIPRSN-NM-SRCH|BC00002041|HNWeb|moh_hnclient_dev|20211013124847.746-0700||ACK|71902|D|2.4\r\n" + 
			"MSA|AE|20191108082211|NHR529E^SEVERE SYSTEM ERROR\r\n" + 
			"ERR|^^^NHR529E";
	
	private static final String ACK_SUCCESS = "MSH|^~\\&|RAIPRSN-NM-SRCH|BC00002041|HNWeb|BC01000161|20210916104824||ACK|71902|D|2.4\r\n" + 
			"MSA|AA|20210506160152|HJMB001ISUCCESSFULLY COMPLETED\r\n" + 
			"ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED";
	
	private static final String ACK_SUCCESS_Z05 = "MSH|^~\\&|RAIENROL-EMP|BC00001013|HNWeb|HN-WEB|20211220180303|test|R50^Y00|20211220160240|D|2.4\r\n" + 
			"MSA|AA|20210506160152|HJMB001ISUCCESSFULLY COMPLETED\r\n" + 
			"PID||9873808694^^^BC^PH^MOH";
	
	private static final String ACK_ERROR_Z05 = "MSH|^~\\&|RAIENROL-EMP|BC00001013|HNWeb|HN-WEB|20211220180303|test|R50^Y00|20211220160240|D|2.4\r\n"+
			"MSA|AE|20211220160240|HRPB187ECOVERAGE MUST BE MORE THAN 2 MONTHS AFTER VISA ISSUE/RESIDENCE DATE\r\n" +
			"ERR|^^^HRPB187E&COVERAGE MUST BE MORE THAN 2 MONTHS AFTER VISA ISSUE/RESIDENCE DATE";
	
	private static final String MESSAGE = " No results were returned. Please refine your search criteria, and try again.";
	
	public static MockWebServer mockBackEnd;
	private static MockedStatic<SecurityUtil> mockStatic;
	

	@Autowired
	private EnrollmentController enrollmentController;
	
	@BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(0);
        mockStatic = Mockito.mockStatic(SecurityUtil.class);
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "00000010", "hnweb-user"));
    }

    @AfterAll
    static void tearDown() throws IOException {
    	mockStatic.close();
        mockBackEnd.shutdown();
       
    }
    
    @Test
    void testEnrollSubscriber_Z06_Error() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(ACK_ERROR)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();
		enrollSubscriberRequest.setPhn("123456789");
		ResponseEntity<EnrollSubscriberResponse> enrollSubscriber = enrollmentController.enrollSubscriber(enrollSubscriberRequest);

		//Check the response
		assertEquals(StatusEnum.ERROR, enrollSubscriber.getBody().getStatus());
		assertEquals("NHR529E", enrollSubscriber.getBody().getMessage());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());       
    }
    
    @Test
    void testEnrollSubscriber_Z05_Error() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(ACK_ERROR_Z05)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();		
		ResponseEntity<EnrollSubscriberResponse> enrollSubscriber = enrollmentController.enrollSubscriber(enrollSubscriberRequest);

		//Check the response
		assertEquals(StatusEnum.ERROR, enrollSubscriber.getBody().getStatus());
		assertEquals("HRPB187ECOVERAGE MUST BE MORE THAN 2 MONTHS AFTER VISA ISSUE/RESIDENCE DATE", enrollSubscriber.getBody().getMessage());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());       
    }
    
    @Test
    void testEnrollSubscriber_Z06_Success() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(ACK_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();
		enrollSubscriberRequest.setPhn("123456789");
		ResponseEntity<EnrollSubscriberResponse> enrollSubscriber = enrollmentController.enrollSubscriber(enrollSubscriberRequest);

		//Check the response
		assertEquals(StatusEnum.SUCCESS, enrollSubscriber.getBody().getStatus());
		assertEquals("AA", enrollSubscriber.getBody().getAcknowledgementCode());
		assertEquals("HJMB001ISUCCESSFULLY COMPLETED", enrollSubscriber.getBody().getAcknowledgementMessage());
		
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());       
    }
    
    @Test
    void testEnrollSubscriber_Z05_Success() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(ACK_SUCCESS_Z05)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();
		ResponseEntity<EnrollSubscriberResponse> enrollSubscriber = enrollmentController.enrollSubscriber(enrollSubscriberRequest);

		//Check the response
		assertEquals(StatusEnum.SUCCESS, enrollSubscriber.getBody().getStatus());
		assertEquals("AA", enrollSubscriber.getBody().getAcknowledgementCode());
		assertEquals("HJMB001ISUCCESSFULLY COMPLETED", enrollSubscriber.getBody().getAcknowledgementMessage());
		assertEquals("9873808694", enrollSubscriber.getBody().getPhn());
		
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());       
    }
    
    
    @Test
    void testGetDemographicsDetails_Success() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src\\test\\resources\\GetDemographicsResponse.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery);
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
    	assertEquals("9862716574",  getPersonDetailsResponse.getPhn());	
    	assertEquals("Robert", getPersonDetailsResponse.getGivenName());
    	assertEquals("M", getPersonDetailsResponse.getGender());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
    }
    
    @Test
    void testGetDemographicsDetails_Warning() throws Exception { 
    	String expectedMessageText = " Warning: The identifier you used in the query has been merged. The surviving identifier was returned.";
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src\\test\\resources\\GetDemographicsResponse_NonSurvivor.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery);
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
        assertEquals(StatusEnum.WARNING, getPersonDetailsResponse.getStatus());
        assertEquals(expectedMessageText, getPersonDetailsResponse.getMessage());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
    }
    
    @Test
    void testGetNameSearch_MultiRecords() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src\\test\\resources\\FindCandidatesResponse_Multiples.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));
        
        GetNameSearchRequest getNameSearchRequest = new GetNameSearchRequest();
        getNameSearchRequest.setGivenName("TestGiven");
        getNameSearchRequest.setSurname("TestSurname");
        getNameSearchRequest.setGender("M");
        getNameSearchRequest.setDateOfBirth("20200101");
        	       
        ResponseEntity<GetNameSearchResponse> response = enrollmentController.getNameSearch(getNameSearchRequest);
        GetNameSearchResponse getNameSearchResponse = response.getBody();
        assertEquals(3, getNameSearchResponse.getCandidates().size());
        assertEquals(31.0, getNameSearchResponse.getCandidates().get(0).getScore());
        assertEquals(-53.0, getNameSearchResponse.getCandidates().get(1).getScore());
        assertEquals(-56.0, getNameSearchResponse.getCandidates().get(2).getScore());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
    }
    
    
    @Test
    void testGetNameSearch_NoRecords() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src\\test\\resources\\FindCandidatesResponse_NoMatches.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));
        
        GetNameSearchRequest getNameSearchRequest = new GetNameSearchRequest();
        getNameSearchRequest.setGivenName("TestGiven");
        getNameSearchRequest.setSurname("TestSurname");
        getNameSearchRequest.setGender("M");
        getNameSearchRequest.setDateOfBirth("20200101");
        	       
        ResponseEntity<GetNameSearchResponse> response = enrollmentController.getNameSearch(getNameSearchRequest);
        GetNameSearchResponse getNameSearchResponse = response.getBody();
        assertEquals(MESSAGE, getNameSearchResponse.getMessage());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
    }
    
    /**
     * The URL property used by the mocked endpoint needs to be set after the MockWebServer starts as the port it uses is 
     * created dynamically on start up to ensure it uses an available port so it is not known before then. 
     * @param registry
     */
    @DynamicPropertySource
    static void registerMockUrlProperty(DynamicPropertyRegistry registry) {
        registry.add("hcim.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
        registry.add("R50.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
    }
    
    private EnrollSubscriberRequest createEnrollSubscriberRequest() {
		EnrollSubscriberRequest enrollSubscriberRequest = new EnrollSubscriberRequest();
		enrollSubscriberRequest.setGivenName("FirstName");
		enrollSubscriberRequest.setSecondName("SecondName");
		enrollSubscriberRequest.setSurname("FamilyName");
		enrollSubscriberRequest.setGender("M");
		enrollSubscriberRequest.setDateOfBirth(LocalDate.of(1973, 8, 11));
		enrollSubscriberRequest.setResidenceDate(LocalDate.now());
		enrollSubscriberRequest.setCoverageEffectiveDate(LocalDate.now());
		enrollSubscriberRequest.setCoverageCancellationDate(LocalDate.now());
		enrollSubscriberRequest.setVisaIssueDate(LocalDate.now());
		enrollSubscriberRequest.setVisaExpiryDate(LocalDate.now());
		enrollSubscriberRequest.setAddress1("101 33A Street");
		enrollSubscriberRequest.setCity("Victoria");
		enrollSubscriberRequest.setProvince("BC");
		enrollSubscriberRequest.setPostalCode("T0T0X0");
		enrollSubscriberRequest.setTelephone("2508578974");
		enrollSubscriberRequest.setMailingAddress1("101 33A Street");
		enrollSubscriberRequest.setMailingAddressCity("Victoria");
		enrollSubscriberRequest.setMailingAddressProvince("BC");
		enrollSubscriberRequest.setMailingAddressPostalCode("T0T0X0");
		enrollSubscriberRequest.setCountry("CA");
		
		return enrollSubscriberRequest;
	}
    

}
