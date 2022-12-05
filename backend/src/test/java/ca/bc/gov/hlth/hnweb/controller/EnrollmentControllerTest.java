package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
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
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchResult;
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
	
	private static final String NO_RECORD_MESSAGE = "BCHCIM.FC.0.0018.0  No results were returned. Please refine your search criteria, and try again.";
	
	private static final String FC_WARNING_MESSAGE = "BCHCIM.FC.0.0017  The maximum number of results were returned, and more may be available. Please refine your search criteria and try again.";
	
	private static final String FC_ERROR_MESSAGE = "BCHCIM.FC.2.0006 The HL7 message is invalid. Please correct the HL7 message, and resubmit it.Results from Schematron validation";
	
	private static final String FC_SUCCESS_MESSAGE = "BCHCIM.FC.0.0012  The search completed successfully.";
	
	private static final String GD_ERROR_MESSAGE = "BCHCIM.GD.2.0006 The HL7 message is invalid. Please correct the HL7 message, and resubmit it.Results from Schematron validation";
	
	private static final String GD_WARNING_MESSAGE = "BCHCIM.GD.0.0015  The identifier you used in the query has been merged. The surviving identifier was returned.";
	
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
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 3);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 0);
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
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 0);
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
		
		assertEquals("HJMB001I TRANSACTION COMPLETED", enrollSubscriber.getBody().getMessage());
		
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());     
        
        // Validate the IN1 segment
        String request = recordedRequest.getBody().readUtf8();
        String[] segments = StringUtils.split(request, "\r\n");
        String in1 = segments[4];
        
        recordedRequest.getBody().readUtf8Line();
        assertTrue(in1.startsWith("IN1||||||||4567368|||^^ABC123|"));
        
        assertTransactionCreated(TransactionType.ENROLL_SUBSCRIBER);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 3);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 0);
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
		assertEquals("HJMB001I TRANSACTION COMPLETED", enrollSubscriber.getBody().getMessage());
		assertEquals("9873808694", enrollSubscriber.getBody().getPhn());
		
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());       
        
        assertTransactionCreated(TransactionType.ENROLL_SUBSCRIBER);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
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
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
    }
    
    @Test
    void testGetDemographicsDetails_DocumentedName() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_DocumentedName.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery, createHttpServletRequest());
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
    	assertEquals("9862716574",  getPersonDetailsResponse.getPhn());
        // This is the documented name
    	assertEquals("Robert", getPersonDetailsResponse.getGivenName());
    	assertEquals("Smith", getPersonDetailsResponse.getSurname());
    	assertEquals("M", getPersonDetailsResponse.getGender());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.GET_PERSON_DETAILS);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
    }
    
    /**
     * Tests that a masked DOB doesn't cause an exception
     * @throws Exception 
     */
    @Test
    void testGetDemographicsDetails_maskedBirthDate() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_MaskedBirthDate.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery, createHttpServletRequest());
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
    	assertEquals("9862716574",  getPersonDetailsResponse.getPhn());
 
        // This is the documented name
    	assertEquals("Robert", getPersonDetailsResponse.getGivenName());
    	assertEquals("Smith", getPersonDetailsResponse.getSurname());
    	assertEquals("M", getPersonDetailsResponse.getGender());
    	assertNull(getPersonDetailsResponse.getDateOfBirth());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.GET_PERSON_DETAILS);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
    }
    
    @Test
    void testGetDemographicsDetails_physicalAndMailingAddress() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_Address.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9999999999");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery, createHttpServletRequest());
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
        assertEquals("1965 CYPRESS ST", getPersonDetailsResponse.getAddress1());
        assertEquals("ATTENTION - MULLER", getPersonDetailsResponse.getAddress2());
        assertEquals("VICTORIA", getPersonDetailsResponse.getCity());
        assertEquals("BC", getPersonDetailsResponse.getProvince());
        assertEquals("X5H2T8", getPersonDetailsResponse.getPostalCode());
             
        assertEquals("121 Main ST", getPersonDetailsResponse.getMailingAddress1());
        assertEquals("Churchil Road", getPersonDetailsResponse.getMailingAddress2());
        assertEquals("BAYSIDE", getPersonDetailsResponse.getMailingAddressCity());
        assertEquals("BC", getPersonDetailsResponse.getMailingAddressProvince());
        assertEquals("V8V8V8", getPersonDetailsResponse.getMailingAddressPostalCode());
      			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.GET_PERSON_DETAILS);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
    }
    
    @Test
    void testGetDemographicsDetails_Warning() throws Exception {      
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_NonSurvivor.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        ResponseEntity<GetPersonDetailsResponse> response = enrollmentController.getPersonDetails(getPersonQuery, createHttpServletRequest());
        GetPersonDetailsResponse getPersonDetailsResponse = response.getBody();
        assertEquals(StatusEnum.WARNING, getPersonDetailsResponse.getStatus());
        assertEquals(GD_WARNING_MESSAGE, getPersonDetailsResponse.getMessage());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.GET_PERSON_DETAILS);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
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
        assertEquals(GD_ERROR_MESSAGE, getPersonDetailsResponse.getMessage());
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
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 0);
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
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 3);
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
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 0);
    }
    
    @Test
    void testGetNameSearch_physicalAndMailingAddress() throws Exception {    	
        
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
        
        //Check if physical address is populated correctly
        assertNull(nameSearchResponse.getCandidates().get(0).getMailingAddress1());       
        assertEquals("666 YELLOW BRICK RD", nameSearchResponse.getCandidates().get(0).getAddress1());
        
        //Check mailing address not populated if same as physical address
        assertEquals("123 FRONT ST", nameSearchResponse.getCandidates().get(1).getAddress1());
        assertNull(nameSearchResponse.getCandidates().get(1).getMailingAddress1());
        
        //Check mailing address populated correctly if there and different than physical address
        assertNull(nameSearchResponse.getCandidates().get(2).getAddress1());
        assertEquals("PO BOX 5", nameSearchResponse.getCandidates().get(2).getMailingAddress1());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 3);
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
        assertEquals(FC_WARNING_MESSAGE, nameSearchResponse.getMessage());
        assertEquals(StatusEnum.WARNING,nameSearchResponse.getStatus());
        assertEquals(10, nameSearchResponse.getCandidates().size());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 10);
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
        assertEquals(FC_ERROR_MESSAGE, nameSearchResponse.getMessage());
        assertEquals(StatusEnum.ERROR,nameSearchResponse.getStatus());
        assertEquals(null, nameSearchResponse.getCandidates());
    			
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 0);
    }
    
    /**
     * Tests that the documentedName gets used instead of the declaredName when available.
     * @throws IOException 
     */
    @Test
    void testGetNameSearch_documentedName() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/FindCandidatesResponse_DocumentedName.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));
        
        NameSearchRequest nameSearchRequest = new NameSearchRequest();
        nameSearchRequest.setGender("M");
        nameSearchRequest.setDateOfBirth(LocalDate.of(1973, 8, 11));
        	       
        ResponseEntity<NameSearchResponse> response = enrollmentController.getNameSearch(nameSearchRequest, createHttpServletRequest());
        NameSearchResponse nameSearchResponse = response.getBody();
        assertEquals(StatusEnum.SUCCESS, nameSearchResponse.getStatus());
        assertEquals(FC_SUCCESS_MESSAGE, nameSearchResponse.getMessage());
        assertEquals(1, nameSearchResponse.getCandidates().size());
        
        NameSearchResult nameSearchResult = nameSearchResponse.getCandidates().get(0);
        // This is the documented name
        assertEquals("PURPLE DINO", nameSearchResult.getGivenName());
        assertEquals("BARNEY", nameSearchResult.getSurname());
    			
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
    }
    
    /**
     * Tests that a masked DOB doesn't cause an exception
     * @throws Exception 
     */
    @Test
    void testGetNameSearch_maskedDateOfBirth() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(TestUtil.convertXMLFileToString("src/test/resources/FindCandidatesResponse_MaskedBirthDate.xml"))
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));
        
        NameSearchRequest nameSearchRequest = new NameSearchRequest();
        nameSearchRequest.setGender("M");
        nameSearchRequest.setDateOfBirth(LocalDate.of(1973, 8, 11));
        	       
        ResponseEntity<NameSearchResponse> response = enrollmentController.getNameSearch(nameSearchRequest, createHttpServletRequest());
        NameSearchResponse nameSearchResponse = response.getBody();
        assertEquals(StatusEnum.SUCCESS, nameSearchResponse.getStatus());
        assertEquals(FC_SUCCESS_MESSAGE, nameSearchResponse.getMessage());
        assertEquals(1, nameSearchResponse.getCandidates().size());
        
        NameSearchResult nameSearchResult = nameSearchResponse.getCandidates().get(0);
        assertEquals("PURPLE DINO", nameSearchResult.getGivenName());
        assertEquals("BARNEY", nameSearchResult.getSurname());
        assertNull(nameSearchResult.getDateOfBirth());
    			
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_XML.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());
        
        assertTransactionCreated(TransactionType.NAME_SEARCH);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 0);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
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
		enrollSubscriberRequest.setDepartmentNumber("ABC123");
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
