package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.Assert.assertThrows;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateNumberAndDeptRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateNumberAndDeptResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberDependentResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.MemberAddress;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@SpringBootTest
public class GroupMemberControllerTest {

	private static final String RPBSPED0_ERROR_PHN_HAS_NO_COVERAGE_IN_GROUP = "        RPBSPED000000010                                ERRORMSGRPBS9179PHN HAS NO COVERAGE IN GROUP                                            93479840746337109111111   ";
	private static final String RPBSPEE0_ERROR_PHN_HAS_NO_COVERAGE_IN_GROUP = "        RPBSPEE000000010                                ERRORMSGRPBS9179PHN HAS NO COVERAGE IN GROUP                                            93479840746337109111111   ";
	private static final String RPBSPED0_SUCCESS = "        RPBSPED000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  98738959276337109111111";
	private static final String RPBSPEE0_SUCCESS = "        RPBSPEE000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  98738959276337109000000001";
	
	private static final String RPBSPWC0_ERROR_CANCEL_DATE_MISSING = "        RPBSPWC000000010                                ERRORMSGRPBS0026COVERAGE CANCEL DATE MUST BE ENTERED                                    93479840746337109           ";
	private static final String RPBSPWC0_ERROR_CANCEL_DAY_INVALID = "        RPBSPWC000000010                                ERRORMSGRPBS1016COVERAGE CANCEL DAY INVALID                                             934798407463371092022-01-01 ";	
	private static final String RPBSPWC0_ERROR_INVALID_CANCEL_REASON = "        RPBSPWC000000010                                ERRORMSGRPBS0063INVALID CANCEL REASON FOR THIS TRANSACTION                              934798407463371092022-01-31A";	
	private static final String RPBSPWC0_ERROR_PHN_DOES_NOT_HAVE_COVERAGE = "        RPBSPWC000000010                                ERRORMSGRPBS0081PHN DOES NOT HAVE COVERAGE UNDER YOUR ORGANIZATION                      934798407463371092022-01-31K";
	private static final String RPBSPWC0_ERROR_FUTURE_CANCEL_DATE = "        RPBSPWC000000010                                ERRORMSGRPBS0048SUBSCRIBER HAS A FUTURE CANCEL DATE.PLS FORWARD DOCS TO MSP             987389592763371092022-12-31K";
	private static final String RPBSPWC0_SUCCESS = "        RPBSPWC000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  987389592763371092022-12-31K";
	
	private static final String RPBSPXP0_ERROR_COVERAGE_ALREADY_EXISTS = "        RPBSPXP000000010                                ERRORMSGRPBS0065COVERAGE ALREADY EXISTS FOR THE PHN/GROUP NUMBER SPECIFIED              63371091111     222   2022-01-01                                                                                                                                                                                                                                        6045551234                              9873895927                                                                                          9873895927";
	private static final String RPBSPXP0_SUCCESS = "        RPBSPXP000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  62431091111     222   2022-01-01123 main st                                                                                         V1V1V1                                                                                                                              6045551234                              9873895902                                                                                                    ";
	private static final String RPBSPWP0_SUCCESS = "        RPBSPWP000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  9873895927633710998828072772022-12-31P";
	private static final String RPBSPWP0_ERROR_SAME_DEPENDENT_PHN = "        RPBSPWP000000010                                ERRORMSGRPBS0097SUBSCRIBER AND DEPENDENT PHN MUST BE DIFFERENT.                         9882807277624310998828072772022-01-31P";
	private static final String RPBSPWP0_PHN_NOT_IN_GROUP = "        RPBSPWP000000010                                ERRORMSGRPBS0104DEPENDENT MUST HAVE COVERAGE UNDER SUBSCRIBER.                          9873895927633710998828072772022-01-31P";
	private static final String RPBSPWP0_DEPENDENT_HAS_NO_COVERAGE_UNDER_SUBSCRIBER= "        RPBSPWP000000010                                ERRORMSGRPBS0104DEPENDENT MUST HAVE COVERAGE UNDER SUBSCRIBER.                          9331926919633710998828072772022-01-31P";

	private static MockWebServer mockBackEnd;

	private static MockedStatic<SecurityUtil> mockStatic;

	@Autowired
	private GroupMemberController groupMemberController;
	
	@BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(0);
        
        mockStatic = Mockito.mockStatic(SecurityUtil.class);
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "00000010", "hnweb-user"));
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
        mockStatic.close();  
    }

    @Test
    public void testUpdateGroupMember_invalidRequest() {
    	
    	UpdateNumberAndDeptRequest updateNumberAndDeptRequest = new UpdateNumberAndDeptRequest();
    	updateNumberAndDeptRequest.setPhn("9347984074");
    	updateNumberAndDeptRequest.setGroupNumber("6337109");
    	ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
    		groupMemberController.updateNumberAndDept(updateNumberAndDeptRequest);
        });
    	assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    	assertEquals("Department Number or Group Number is required", exception.getReason());
    }

    @Test
    public void testUpdateGroupMember_phnHasNoCoverageInGroup() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPED0_ERROR_PHN_HAS_NO_COVERAGE_IN_GROUP)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPEE0_ERROR_PHN_HAS_NO_COVERAGE_IN_GROUP)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	UpdateNumberAndDeptRequest updateNumberAndDeptRequest = new UpdateNumberAndDeptRequest();
    	updateNumberAndDeptRequest.setPhn("9347984074");
    	updateNumberAndDeptRequest.setGroupNumber("6337109");
    	updateNumberAndDeptRequest.setDepartmentNumber("000001");
    	updateNumberAndDeptRequest.setGroupMemberNumber("000000001");
    	
		ResponseEntity<UpdateNumberAndDeptResponse> response = groupMemberController.updateNumberAndDept(updateNumberAndDeptRequest);
		
		UpdateNumberAndDeptResponse updateNumberAndDeptResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, updateNumberAndDeptResponse.getStatus());
        assertEquals("RPBS9179 PHN HAS NO COVERAGE IN GROUP", updateNumberAndDeptResponse.getMessage());
        assertEquals("9347984074", updateNumberAndDeptResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testUpdateGroupMember_success() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPED0_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPEE0_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	UpdateNumberAndDeptRequest updateNumberAndDeptRequest = new UpdateNumberAndDeptRequest();
    	updateNumberAndDeptRequest.setPhn("9873895927");
    	updateNumberAndDeptRequest.setGroupNumber("6337109");
    	updateNumberAndDeptRequest.setDepartmentNumber("000001");
    	updateNumberAndDeptRequest.setGroupMemberNumber("000000001");
    	
		ResponseEntity<UpdateNumberAndDeptResponse> response = groupMemberController.updateNumberAndDept(updateNumberAndDeptRequest);
		
		UpdateNumberAndDeptResponse updateNumberAndDeptResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, updateNumberAndDeptResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", updateNumberAndDeptResponse.getMessage());
        assertEquals("9873895927", updateNumberAndDeptResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testCancelGroupMember_cancelDateMissing() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWC0_ERROR_CANCEL_DATE_MISSING)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberRequest cancelGroupMemberRequest = new CancelGroupMemberRequest();
    	cancelGroupMemberRequest.setPhn("9347984074");
    	cancelGroupMemberRequest.setGroupNumber("6337109");
    	cancelGroupMemberRequest.setCoverageCancelDate(null);
    	cancelGroupMemberRequest.setCancelReason(null);
    	
		ResponseEntity<CancelGroupMemberResponse> response = groupMemberController.cancelGroupMember(cancelGroupMemberRequest);
		
		CancelGroupMemberResponse cancelGroupMemberResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, cancelGroupMemberResponse.getStatus());
        assertEquals("RPBS0026 COVERAGE CANCEL DATE MUST BE ENTERED", cancelGroupMemberResponse.getMessage());
        assertEquals("9347984074", cancelGroupMemberResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testCancelGroupMember_cancelDayInvalid() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWC0_ERROR_CANCEL_DAY_INVALID)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberRequest cancelGroupMemberRequest = new CancelGroupMemberRequest();
    	cancelGroupMemberRequest.setPhn("9347984074");
    	cancelGroupMemberRequest.setGroupNumber("6337109");
    	// The day must be the end of the month
    	cancelGroupMemberRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 01));
    	cancelGroupMemberRequest.setCancelReason(null);
    	
		ResponseEntity<CancelGroupMemberResponse> response = groupMemberController.cancelGroupMember(cancelGroupMemberRequest);
		
		CancelGroupMemberResponse cancelGroupMemberResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, cancelGroupMemberResponse.getStatus());
        assertEquals("RPBS1016 COVERAGE CANCEL DAY INVALID", cancelGroupMemberResponse.getMessage());
        assertEquals("9347984074", cancelGroupMemberResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testCancelGroupMember_cancelReasonInvalid() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWC0_ERROR_INVALID_CANCEL_REASON)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberRequest cancelGroupMemberRequest = new CancelGroupMemberRequest();
    	cancelGroupMemberRequest.setPhn("9347984074");
    	cancelGroupMemberRequest.setGroupNumber("6337109");
    	cancelGroupMemberRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 31));
    	// The reason must be K or E
    	cancelGroupMemberRequest.setCancelReason("A");
    	
		ResponseEntity<CancelGroupMemberResponse> response = groupMemberController.cancelGroupMember(cancelGroupMemberRequest);
		
		CancelGroupMemberResponse cancelGroupMemberResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, cancelGroupMemberResponse.getStatus());
        assertEquals("RPBS0063 INVALID CANCEL REASON FOR THIS TRANSACTION", cancelGroupMemberResponse.getMessage());
        assertEquals("9347984074", cancelGroupMemberResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }

    @Test
    public void testCancelGroupMember_phnDoesNotHaveCoverage() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWC0_ERROR_PHN_DOES_NOT_HAVE_COVERAGE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberRequest cancelGroupMemberRequest = new CancelGroupMemberRequest();
    	cancelGroupMemberRequest.setPhn("9347984074");
    	cancelGroupMemberRequest.setGroupNumber("6337109");
    	cancelGroupMemberRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 31));
    	cancelGroupMemberRequest.setCancelReason("K");
    	
		ResponseEntity<CancelGroupMemberResponse> response = groupMemberController.cancelGroupMember(cancelGroupMemberRequest);
		
		CancelGroupMemberResponse cancelGroupMemberResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, cancelGroupMemberResponse.getStatus());
        assertEquals("RPBS0081 PHN DOES NOT HAVE COVERAGE UNDER YOUR ORGANIZATION", cancelGroupMemberResponse.getMessage());
        assertEquals("9347984074", cancelGroupMemberResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testCancelGroupMember_futureCancelDate() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWC0_ERROR_FUTURE_CANCEL_DATE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberRequest cancelGroupMemberRequest = new CancelGroupMemberRequest();
    	cancelGroupMemberRequest.setPhn("9873895927");
    	cancelGroupMemberRequest.setGroupNumber("6337109");
    	cancelGroupMemberRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 31));
    	cancelGroupMemberRequest.setCancelReason("K");
    	
		ResponseEntity<CancelGroupMemberResponse> response = groupMemberController.cancelGroupMember(cancelGroupMemberRequest);
		
		CancelGroupMemberResponse cancelGroupMemberResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, cancelGroupMemberResponse.getStatus());
        assertEquals("RPBS0048 SUBSCRIBER HAS A FUTURE CANCEL DATE.PLS FORWARD DOCS TO MSP", cancelGroupMemberResponse.getMessage());
        assertEquals("9873895927", cancelGroupMemberResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testCancelGroupMember_success() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWC0_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberRequest cancelGroupMemberRequest = new CancelGroupMemberRequest();
    	cancelGroupMemberRequest.setPhn("9873895927");
    	cancelGroupMemberRequest.setGroupNumber("6337109");
    	cancelGroupMemberRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 31));
    	cancelGroupMemberRequest.setCancelReason("K");
    	
		ResponseEntity<CancelGroupMemberResponse> response = groupMemberController.cancelGroupMember(cancelGroupMemberRequest);
		
		CancelGroupMemberResponse cancelGroupMemberResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, cancelGroupMemberResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", cancelGroupMemberResponse.getMessage());
        assertEquals("9873895927", cancelGroupMemberResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testAddGroupMember_coverageAlreadyExists() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPXP0_ERROR_COVERAGE_ALREADY_EXISTS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	AddGroupMemberRequest addGroupMemberRequest = new AddGroupMemberRequest();
    	addGroupMemberRequest.setGroupNumber("6337109");
    	addGroupMemberRequest.setEffectiveDate(LocalDate.of(2022, 01, 31));
    	addGroupMemberRequest.setPhn("9873895927");
    	addGroupMemberRequest.setGroupMemberNumber("2222");
    	addGroupMemberRequest.setDepartmentNumber("111");
    	addGroupMemberRequest.setPhone("6045551234");
    	
    	MemberAddress homeAddress = new MemberAddress();
    	homeAddress.setAddressLine1("123 Main St");
    	homeAddress.setPostalCode("V1V1V1");
    	addGroupMemberRequest.setHomeAddress(homeAddress);
    	
		ResponseEntity<AddGroupMemberResponse> response = groupMemberController.addGroupMember(addGroupMemberRequest);
		
		AddGroupMemberResponse addGroupMemberResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, addGroupMemberResponse.getStatus());
        assertEquals("RPBS0065 9873895927 COVERAGE ALREADY EXISTS FOR THE PHN/GROUP NUMBER SPECIFIED", addGroupMemberResponse.getMessage());
        assertEquals("9873895927", addGroupMemberResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testAddGroupMember_success() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPXP0_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	AddGroupMemberRequest addGroupMemberRequest = new AddGroupMemberRequest();
    	addGroupMemberRequest.setGroupNumber("6243109");
    	addGroupMemberRequest.setEffectiveDate(LocalDate.of(2022, 01, 31));
    	addGroupMemberRequest.setPhn("9873895902");
    	addGroupMemberRequest.setGroupMemberNumber("2222");
    	addGroupMemberRequest.setDepartmentNumber("111");
    	addGroupMemberRequest.setPhone("6045551234");
    	
    	MemberAddress homeAddress = new MemberAddress();
    	homeAddress.setAddressLine1("123 Main St");
    	homeAddress.setPostalCode("V1V1V1");
    	addGroupMemberRequest.setHomeAddress(homeAddress);
    	
		ResponseEntity<AddGroupMemberResponse> response = groupMemberController.addGroupMember(addGroupMemberRequest);
		
		AddGroupMemberResponse addGroupMemberResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, addGroupMemberResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", addGroupMemberResponse.getMessage());
        assertEquals("9873895902", addGroupMemberResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    

    @Test
    public void testCancelGroupMemberDependent_DependentNotUnderSubcriberCoverage() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWP0_DEPENDENT_HAS_NO_COVERAGE_UNDER_SUBSCRIBER)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberDependentRequest cancelGroupMemberDependentRequest = new CancelGroupMemberDependentRequest();
    	cancelGroupMemberDependentRequest.setPhn("9331926919");
    	cancelGroupMemberDependentRequest.setGroupNumber("6337109");
    	cancelGroupMemberDependentRequest.setDependentPhn("9882807277");
    	cancelGroupMemberDependentRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 31));
    	cancelGroupMemberDependentRequest.setCancelReason("I");
    	
		ResponseEntity<CancelGroupMemberDependentResponse> response = groupMemberController.cancelGroupMemberDependent(cancelGroupMemberDependentRequest);
		
		CancelGroupMemberDependentResponse cancelGroupMemberDependentResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, cancelGroupMemberDependentResponse.getStatus());
        assertEquals("RPBS0104 DEPENDENT MUST HAVE COVERAGE UNDER SUBSCRIBER.", cancelGroupMemberDependentResponse.getMessage());
        assertEquals("9331926919", cancelGroupMemberDependentResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testCancelGroupMemberDependent_PhnNotInGroup() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody( RPBSPWP0_PHN_NOT_IN_GROUP)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberDependentRequest cancelGroupMemberDependentRequest = new CancelGroupMemberDependentRequest();
    	cancelGroupMemberDependentRequest.setPhn("9873895927");
    	cancelGroupMemberDependentRequest.setGroupNumber("6337109");
    	cancelGroupMemberDependentRequest.setDependentPhn("9882807277");
    	cancelGroupMemberDependentRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 31));
    	cancelGroupMemberDependentRequest.setCancelReason("I");
    	
		ResponseEntity<CancelGroupMemberDependentResponse> response = groupMemberController.cancelGroupMemberDependent(cancelGroupMemberDependentRequest);
		
		CancelGroupMemberDependentResponse cancelGroupMemberDependentResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, cancelGroupMemberDependentResponse.getStatus());
        assertEquals("RPBS0105 PHN MUST BE A SUBSCRIBER IN GROUP.", cancelGroupMemberDependentResponse.getMessage());
        assertEquals("9873895927", cancelGroupMemberDependentResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    @Test
    public void testCancelGroupMemberDependent_SameDependentPhn() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWP0_ERROR_SAME_DEPENDENT_PHN)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberDependentRequest cancelGroupMemberDependentRequest = new CancelGroupMemberDependentRequest();
    	cancelGroupMemberDependentRequest.setPhn("9882807277");
    	cancelGroupMemberDependentRequest.setGroupNumber("6337109");
    	cancelGroupMemberDependentRequest.setDependentPhn("9882807277");
    	cancelGroupMemberDependentRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 31));
    	cancelGroupMemberDependentRequest.setCancelReason("I");
    	
		ResponseEntity<CancelGroupMemberDependentResponse> response = groupMemberController.cancelGroupMemberDependent(cancelGroupMemberDependentRequest);
		
		CancelGroupMemberDependentResponse cancelGroupMemberDependentResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, cancelGroupMemberDependentResponse.getStatus());
        assertEquals("RPBS0097 SUBSCRIBER AND DEPENDENT PHN MUST BE DIFFERENT.", cancelGroupMemberDependentResponse.getMessage());
        assertEquals("9882807277", cancelGroupMemberDependentResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    }
    
    
    @Test
    public void testCancelGroupMemberDependent_success() throws InterruptedException {
    	mockBackEnd.enqueue(new MockResponse()
        		.setBody(RPBSPWP0_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
    	
    	CancelGroupMemberDependentRequest cancelGroupMemberDependentRequest = new CancelGroupMemberDependentRequest();
    	cancelGroupMemberDependentRequest.setPhn("9873895927");
    	cancelGroupMemberDependentRequest.setGroupNumber("6337109");
    	cancelGroupMemberDependentRequest.setDependentPhn("9397105575");
    	cancelGroupMemberDependentRequest.setCoverageCancelDate(LocalDate.of(2022, 01, 31));
    	cancelGroupMemberDependentRequest.setCancelReason("K");
    	
		ResponseEntity<CancelGroupMemberDependentResponse> response = groupMemberController.cancelGroupMemberDependent(cancelGroupMemberDependentRequest);
		
		CancelGroupMemberDependentResponse cancelGroupMemberDependentResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, cancelGroupMemberDependentResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", cancelGroupMemberDependentResponse.getMessage());
        assertEquals("9873895927", cancelGroupMemberDependentResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
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
	
}