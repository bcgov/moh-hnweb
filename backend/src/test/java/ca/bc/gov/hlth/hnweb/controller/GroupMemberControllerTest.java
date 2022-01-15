package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;

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