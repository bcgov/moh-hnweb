package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.utils.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * JUnit test class for EnrollmentController
 *
 */
public class EnrollmentControllerTest extends BaseControllerTest {

	private static final String Z06_ERROR = "MSH|^~\\&|RAIPRSN-NM-SRCH|BC00002041|HNWeb|moh_hnclient_dev|20211013124847.746-0700||ACK|71902|D|2.4\r\n" + 
			"MSA|AE|20191108082211|NHR529E^SEVERE SYSTEM ERROR\r\n" + 
			"ERR|^^^NHR529E&SYSTEM ERROR";
	
	private static final String Z06_SUCCESS = "MSH|^~\\&|RAIPRSN-NM-SRCH|BC00002041|HNWeb|BC01000161|20210916104824||ACK|71902|D|2.4\r\n" + 
			"MSA|AA|20210506160152|HJMB001ISUCCESSFULLY COMPLETED\r\n" + 
			"ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED";
	
	private static final String ZO5_SUCCESS = "MSH|^~\\&|RAIENROL-EMP|BC00001013|HNWeb|HN-WEB|20211220180303|test|R50^Y00|20211220160240|D|2.4\r\n" + 
			"MSA|AA|20210506160152|HJMB001ITRANSACTION SUCCESSFUL\r\n" + 
			"PID||9873808694^^^BC^PH^MOH\r\n" +
			"ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED";
			;
	
	private static final String Z05_ERROR = "MSH|^~\\&|RAIENROL-EMP|BC00001013|HNWeb|HN-WEB|20211220180303|test|R50^Y00|20211220160240|D|2.4\r\n"+
			"MSA|AE|20211220160240|HRPB187ECOVERAGE MUST BE MORE THAN 2 MONTHS AFTER VISA ISSUE/RESIDENCE DATE\r\n" +
			"ERR|^^^HRPB187E&COVERAGE MUST BE MORE THAN 2 MONTHS AFTER VISA ISSUE/RESIDENCE DATE";
	
	private static final String NO_RECORD_MESSAGE = " No results were returned. Please refine your search criteria, and try again.";
	
	private static final String WARNING_MESSAGE = " The maximum number of results were returned, and more may be available. Please refine your search criteria and try again.";
	
	private static final String ERROR_MESSAGE = " The HL7 message is invalid. Please correct the HL7 message, and resubmit it.Results from Schematron validation";
	
	@Autowired
	private EnrollmentController enrollmentController;
	
    @Test
    void testEnrollSubscriber_Z06_Error() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(Z06_ERROR)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();
		enrollSubscriberRequest.setPhn("123456789");
		ResponseEntity<EnrollSubscriberResponse> enrollSubscriber = enrollmentController.enrollSubscriber(enrollSubscriberRequest, createHttpServletRequest());

		//Check the response
		assertEquals(StatusEnum.ERROR, enrollSubscriber.getBody().getStatus());
		assertEquals("NHR529E SYSTEM ERROR", enrollSubscriber.getBody().getMessage());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.ENROLL_SUBSCRIBER);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 0);
    }
    
    @Test
    void testEnrollSubscriber_Z05_Error() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(Z05_ERROR)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();		
		ResponseEntity<EnrollSubscriberResponse> enrollSubscriber = enrollmentController.enrollSubscriber(enrollSubscriberRequest, createHttpServletRequest());

		//Check the response
		assertEquals(StatusEnum.ERROR, enrollSubscriber.getBody().getStatus());
		assertEquals("HRPB187E COVERAGE MUST BE MORE THAN 2 MONTHS AFTER VISA ISSUE/RESIDENCE DATE", enrollSubscriber.getBody().getMessage());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.ENROLL_SUBSCRIBER);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 0);
    }
    
    @Test
    void testEnrollSubscriber_Z06_Success() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(Z06_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();
		enrollSubscriberRequest.setPhn("123456789");
		ResponseEntity<EnrollSubscriberResponse> enrollSubscriber = enrollmentController.enrollSubscriber(enrollSubscriberRequest, createHttpServletRequest());

		//Check the response
		assertEquals(StatusEnum.SUCCESS, enrollSubscriber.getBody().getStatus());
		
		assertEquals("HJMB001I SUCCESSFULLY COMPLETED", enrollSubscriber.getBody().getMessage());
		
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());       
        
        assertTransactionCreated(TransactionType.ENROLL_SUBSCRIBER);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 0);
    }
    
    @Test
    void testEnrollSubscriber_Z05_Success() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(ZO5_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();
		ResponseEntity<EnrollSubscriberResponse> enrollSubscriber = enrollmentController.enrollSubscriber(enrollSubscriberRequest, createHttpServletRequest());

		//Check the response
		assertEquals(StatusEnum.SUCCESS, enrollSubscriber.getBody().getStatus());
		assertEquals("HJMB001I SUCCESSFULLY COMPLETED", enrollSubscriber.getBody().getMessage());
		assertEquals("9873808694", enrollSubscriber.getBody().getPhn());
		
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());       
        
        assertTransactionCreated(TransactionType.ENROLL_SUBSCRIBER);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
    }
    
    
    @Test
    void testGetDemographicsDetails_Success() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery, createHttpServletRequest());
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
    	assertEquals("9862716574",  getPersonDetailsResponse.getPhn());	
    	assertEquals("Robert", getPersonDetailsResponse.getGivenName());
    	assertEquals("M", getPersonDetailsResponse.getGender());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.GET_PERSON_DETAILS);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
    }
    
    @Test
    void testGetDemographicsDetails_Warning() throws Exception { 
    	String expectedMessageText = " The identifier you used in the query has been merged. The surviving identifier was returned.";
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_NonSurvivor.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery, createHttpServletRequest());
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
        assertEquals(StatusEnum.WARNING, getPersonDetailsResponse.getStatus());
        assertEquals(expectedMessageText, getPersonDetailsResponse.getMessage());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.GET_PERSON_DETAILS);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
    }
    
    @Test
    void testGetDemographicsDetails_Error() throws Exception { 	
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_Error.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery, createHttpServletRequest());
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
        assertEquals(StatusEnum.ERROR, getPersonDetailsResponse.getStatus());
        assertEquals(ERROR_MESSAGE, getPersonDetailsResponse.getMessage());
        assertEquals(null, getPersonDetailsResponse.getGivenName());
        assertEquals(null, getPersonDetailsResponse.getSurname());
        assertEquals(null, getPersonDetailsResponse.getDateOfBirth());
        assertEquals(null, getPersonDetailsResponse.getGender());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.GET_PERSON_DETAILS);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 0);
    }
    
    @Test
    void testGetNameSearch_MultiRecords() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/FindCandidatesResponse_Multiples.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));
        
        NameSearchRequest nameSearchRequest = new NameSearchRequest();
        nameSearchRequest.setGivenName("TestGiven");
        nameSearchRequest.setSurname("TestSurname");
        nameSearchRequest.setGender("M");
        nameSearchRequest.setDateOfBirth(LocalDate.of(1973, 8, 11));
        	       
        ResponseEntity<NameSearchResponse> response = enrollmentController.getNameSearch(nameSearchRequest, createHttpServletRequest());
        NameSearchResponse nameSearchResponse = response.getBody();
        assertEquals(3, nameSearchResponse.getCandidates().size());
        assertEquals(new BigDecimal(31), nameSearchResponse.getCandidates().get(0).getScore());
        assertEquals(new BigDecimal(-53), nameSearchResponse.getCandidates().get(1).getScore());
        assertEquals(new BigDecimal(-56), nameSearchResponse.getCandidates().get(2).getScore());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 3);
    }
    
    
    @Test
    void testGetNameSearch_NoRecords() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/FindCandidatesResponse_NoMatches.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));
        
        NameSearchRequest nameSearchRequest = new NameSearchRequest();
        nameSearchRequest.setGivenName("TestGiven");
        nameSearchRequest.setSurname("TestSurname");
        nameSearchRequest.setGender("M");
        nameSearchRequest.setDateOfBirth(LocalDate.of(1973, 8, 11));
        	       
        ResponseEntity<NameSearchResponse> response = enrollmentController.getNameSearch(nameSearchRequest, createHttpServletRequest());
        NameSearchResponse nameSearchResponse = response.getBody();
        assertEquals(NO_RECORD_MESSAGE, nameSearchResponse.getMessage());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 0);
    }
    
    @Test
    void testGetNameSearch_Warnings() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/FindCandidatesResponse_Warning.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));
        
        NameSearchRequest nameSearchRequest = new NameSearchRequest();
        nameSearchRequest.setGivenName("HUMPTY");
        nameSearchRequest.setSurname("DUMPTY");
        nameSearchRequest.setGender("M");
        nameSearchRequest.setDateOfBirth(LocalDate.of(1973, 8, 11));
        	       
        ResponseEntity<NameSearchResponse> response = enrollmentController.getNameSearch(nameSearchRequest, createHttpServletRequest());
        NameSearchResponse nameSearchResponse = response.getBody();
        assertEquals(WARNING_MESSAGE, nameSearchResponse.getMessage());
        assertEquals(StatusEnum.WARNING,nameSearchResponse.getStatus());
        assertEquals(10, nameSearchResponse.getCandidates().size());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 10);
    }
    
    @Test
    void testGetNameSearch_Error() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/FindCandidatesResponse_Error.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));
        
        NameSearchRequest nameSearchRequest = new NameSearchRequest();
        nameSearchRequest.setGender("M");
        nameSearchRequest.setDateOfBirth(LocalDate.of(1973, 8, 11));
        	       
        ResponseEntity<NameSearchResponse> response = enrollmentController.getNameSearch(nameSearchRequest, createHttpServletRequest());
        NameSearchResponse nameSearchResponse = response.getBody();
        assertEquals(ERROR_MESSAGE, nameSearchResponse.getMessage());
        assertEquals(StatusEnum.ERROR,nameSearchResponse.getStatus());
        assertEquals(null, nameSearchResponse.getCandidates());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 0);
    }
    
    /**
     * The URL property used by the mocked endpoint needs to be set after the MockWebServer starts as the port it uses is 
     * created dynamically on start up to ensure it uses an available port so it is not known before then. 
     * @param registry
     */
    @DynamicPropertySource
    static void registerMockUrlProperty(DynamicPropertyRegistry registry) {
        registry.add("hcim.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
        registry.add("hibc.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
    }
    
    private EnrollSubscriberRequest createEnrollSubscriberRequest() {
		EnrollSubscriberRequest enrollSubscriberRequest = new EnrollSubscriberRequest();
		enrollSubscriberRequest.setGroupNumber("4567368");
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
