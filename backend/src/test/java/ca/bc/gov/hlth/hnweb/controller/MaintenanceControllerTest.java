package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class MaintenanceControllerTest extends BaseControllerTest {
	
	private static final String R43_SUCCESS = "";
	
	private static final String R43_INVALID_STUDENT_DATE_DATE = "        RPBSPRE000000010                                ERRORMSGRPBS0111STUDENT END DATE YEAR MUST BE GREATER THAN OR EQUAL TO CURRENT YEAR.    9332912486633710993292797331970-01-01Y2020-01";

	private static final String R43_NOT_ATTENDING_CANADIAN_SCHOOL = "        RPBSPRE000000010                                ERRORMSGRPBS0108STUDENT NOT ATTENDING SCHOOL IN CANADA, MUST FORWARD DOCUMENTS TO MSP.  9332912486633710993292797331970-01-01N2023-01";

	private static final String R43_NO_COVERAGE_FOUND = "        RPBSPRE000000010                                ERRORMSGRPBS0067NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP               9332912486633710993292797331970-01-01Y2023-01";
	
	private static final String R43_CANNOT_BE_REINSTANTED = "        RPBSPRE000000010                                ERRORMSGRPBS1054DEPENDENT CANNOT BE REINSTATED AS A STUDENT THIS TIME. PLS CONTACT MSP. 9387807484502802293190799262013-03-29Y2022-12";
	
	protected static DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
	
	@Autowired
	private MaintenanceController maintenanceController;
   
	@Test
	public void testReinstateOverAgeDependent_error_invalidStudentEndDate() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R43_INVALID_STUDENT_DATE_DATE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ReinstateOverAgeDependentRequest reinstateRequest = createReinstateOverAgeDependentRequest("9332912486", LocalDate.of(2020, 1, 1));
		
        ResponseEntity<ReinstateOverAgeDependentResponse> response = maintenanceController.reinstateOverAgeDependent(reinstateRequest, createHttpServletRequest());
		
        ReinstateOverAgeDependentResponse reinstateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, reinstateResponse.getStatus());
        assertEquals("RPBS0111 STUDENT END DATE YEAR MUST BE GREATER THAN OR EQUAL TO CURRENT YEAR.", reinstateResponse.getMessage());

        assertEquals("9332912486", reinstateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.REINSTATE_OVER_AGE_DEPENDENT);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 3);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testReinstateOverAgeDependent_error_noCoverageFound() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R43_NO_COVERAGE_FOUND)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ReinstateOverAgeDependentRequest reinstateRequest = createReinstateOverAgeDependentRequest("9332912486", null);
		
        ResponseEntity<ReinstateOverAgeDependentResponse> response = maintenanceController.reinstateOverAgeDependent(reinstateRequest, createHttpServletRequest());
		
        ReinstateOverAgeDependentResponse reinstateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, reinstateResponse.getStatus());
        assertEquals("RPBS0067 NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP", reinstateResponse.getMessage());

        assertEquals("9332912486", reinstateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.REINSTATE_OVER_AGE_DEPENDENT);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 3);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testReinstateOverAgeDependent_error_notAttendingCanadianSchool() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R43_NOT_ATTENDING_CANADIAN_SCHOOL)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ReinstateOverAgeDependentRequest reinstateRequest = createReinstateOverAgeDependentRequest("9332912486", null);
		
        ResponseEntity<ReinstateOverAgeDependentResponse> response = maintenanceController.reinstateOverAgeDependent(reinstateRequest, createHttpServletRequest());
		
        ReinstateOverAgeDependentResponse reinstateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, reinstateResponse.getStatus());
        assertEquals("RPBS0108 STUDENT NOT ATTENDING SCHOOL IN CANADA, MUST FORWARD DOCUMENTS TO MSP.", reinstateResponse.getMessage());

        assertEquals("9332912486", reinstateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.REINSTATE_OVER_AGE_DEPENDENT);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 3);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testReinstateOverAgeDependent_error_cannotBeReinstated() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R43_CANNOT_BE_REINSTANTED)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ReinstateOverAgeDependentRequest reinstateRequest = createReinstateOverAgeDependentRequest("9387807484", null);
		
        ResponseEntity<ReinstateOverAgeDependentResponse> response = maintenanceController.reinstateOverAgeDependent(reinstateRequest, createHttpServletRequest());
		
        ReinstateOverAgeDependentResponse reinstateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, reinstateResponse.getStatus());
        assertEquals("RPBS1054 DEPENDENT CANNOT BE REINSTATED AS A STUDENT THIS TIME. PLS CONTACT MSP.", reinstateResponse.getMessage());

        assertEquals("9387807484", reinstateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.REINSTATE_OVER_AGE_DEPENDENT);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 3);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
    /**
     * The URL property used by the mocked endpoint needs to be set after the MockWebServer starts as the port it uses is 
     * created dynamically on start up to ensure it uses an available port so it is not known before then. 
     * @param registry
     */
    @DynamicPropertySource
    static void registerMockUrlProperty(DynamicPropertyRegistry registry) {
        registry.add("rapid.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
    }
	
	private ReinstateOverAgeDependentRequest createReinstateOverAgeDependentRequest(String phn, LocalDate studentEndDate) {
		ReinstateOverAgeDependentRequest reinstateRequest = new ReinstateOverAgeDependentRequest();
		reinstateRequest.setGroupNumber("6337109");
		reinstateRequest.setPhn(phn);
		reinstateRequest.setDependentPhn("9329279733");
		reinstateRequest.setDependentDateOfBirth(LocalDate.of(1970, 1, 1));
		reinstateRequest.setStudentEndDate(studentEndDate);
		
		return reinstateRequest;
	}
	
}


