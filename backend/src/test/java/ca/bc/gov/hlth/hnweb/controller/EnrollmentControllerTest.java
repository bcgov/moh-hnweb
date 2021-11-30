package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * JUnit test class for EnrollmentController
 *
 */
@SpringBootTest
public class EnrollmentControllerTest {

	private static final String ACK = "MSH|^~\\&|RAIPRSN-NM-SRCH|BC00002041|HNWeb|moh_hnclient_dev|20211013124847.746-0700||ACK|71902|D|2.4\r\n" + 
			"MSA|AE|20191108082211|NHR529E^SEVERE SYSTEM ERROR\r\n" + 
			"ERR|^^^NHR529E";
	


	public static MockWebServer mockBackEnd;

	@Autowired
	private EnrollmentController enrollmentController;
	
	@BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(0);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }
    
    @Test
    void testEnrollSubscriber_Error() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(ACK)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();
		EnrollSubscriberResponse enrollSubscriberResponse = enrollmentController.enrollSubscriber(enrollSubscriberRequest);

		//Check the response
		assertEquals("AE", enrollSubscriberResponse.getAcknowledgementCode());
		assertEquals("NHR529E", enrollSubscriberResponse.getAcknowledgementMessage());
		
		//Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertEquals("/", recordedRequest.getPath());       
    }
    
    @Disabled
    @Test
    void testGetDemographicsDetails_Success() throws Exception {    	
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(convertXMLFileToString())
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

        GetPersonDetailsRequest getPersonQuery = new GetPersonDetailsRequest();
        getPersonQuery.setPhn("9862716574");
        
        GetPersonDetailsResponse response = enrollmentController.getDemographicDetails(getPersonQuery);
    	assertEquals("9862716574", response.getPerson().getPhn());	
    	assertEquals("Robert", response.getPerson().getDocumentedName().getFirstGivenName());
		
		//Check the client request is sent as expected
        //RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        //assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        //assertEquals(MediaType.TEXT_XML_VALUE.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        //assertEquals("/", recordedRequest.getPath());
    	
    	//mockBackEnd.close();
    }
    
    /**
     * The URL property used by the mocked endpoint needs to be set after the MockWebServer starts as the port it uses is 
     * created dynamically on start up to ensure it uses an available port so it is not known before then. 
     * @param registry
     */
    @DynamicPropertySource
    static void registerMockUrlProperty(DynamicPropertyRegistry registry) {
        registry.add("R03.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
        registry.add("R50.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
    }
    
    private EnrollSubscriberRequest createEnrollSubscriberRequest() {
		EnrollSubscriberRequest enrollSubscriberRequest = new EnrollSubscriberRequest();
		enrollSubscriberRequest.setPhn("123456789");
		enrollSubscriberRequest.setFullName("Test");
		enrollSubscriberRequest.setGender("M");
		enrollSubscriberRequest.setResidenceDate(LocalDate.now());
		enrollSubscriberRequest.setCoverageEffectiveDate(LocalDate.now());
		enrollSubscriberRequest.setVisaIssueDate(LocalDate.now());
		enrollSubscriberRequest.setVisaExpiryDate(LocalDate.now());
		enrollSubscriberRequest.setAddress("101 33A Street");
		enrollSubscriberRequest.setCity("Victoria");
		enrollSubscriberRequest.setProvince("BC");
		enrollSubscriberRequest.setPostalCode("T0T0X0");
		enrollSubscriberRequest.setAreaCode("250");
		enrollSubscriberRequest.setTelephone("8578974");
		enrollSubscriberRequest.setMailingAddress("101 33A Street");
		enrollSubscriberRequest.setMailingAddressCity("Victoria");
		enrollSubscriberRequest.setMailingAddressProvince("BC");
		enrollSubscriberRequest.setMailingAddressPostalCode("T0T0X0");
		enrollSubscriberRequest.setCountry("CA");
		
		return enrollSubscriberRequest;
	}
    
	 
	private String convertXMLFileToString() throws IOException
	{
	// our XML file for this example
	    File xmlFile = new File("src\\test\\resources\\GetDemographicsResponse.xml");
	 
	    Reader fileReader;			
		fileReader = new FileReader(xmlFile);
		BufferedReader bufReader = new BufferedReader(fileReader);
	        
	    StringBuilder sb = new StringBuilder();
	    String line = bufReader.readLine();
	    while( line != null){
	        sb.append(line).append("\n");
	        line = bufReader.readLine();
	    }
	    String xml2String = sb.toString();	        
	    bufReader.close();
	        
	    return xml2String;
	}
}
