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
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ChangeCancelDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ChangeCancelDateResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ExtendCancelDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ExtendCancelDateResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateCancelledGroupCoverageRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateCancelledGroupCoverageResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.RenewCancelledGroupCoverageRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.RenewCancelledGroupCoverageResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;


public class MaintenanceControllerTest extends BaseControllerTest {
	
	private static final String R43_SUCCESS = "        RPBSPRE000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  9873252394484190498732523872000-05-10Y2022-12";
	
	private static final String R43_INVALID_STUDENT_DATE_DATE = "        RPBSPRE000000010                                ERRORMSGRPBS0111STUDENT END DATE YEAR MUST BE GREATER THAN OR EQUAL TO CURRENT YEAR.    9332912486633710993292797331970-01-01Y2020-01";

	private static final String R43_NOT_ATTENDING_CANADIAN_SCHOOL = "        RPBSPRE000000010                                ERRORMSGRPBS0108STUDENT NOT ATTENDING SCHOOL IN CANADA, MUST FORWARD DOCUMENTS TO MSP.  9332912486633710993292797331970-01-01N2023-01";

	private static final String R43_NO_COVERAGE_FOUND = "        RPBSPRE000000010                                ERRORMSGRPBS0067NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP               9332912486633710993292797331970-01-01Y2023-01";
	
	private static final String R43_CANNOT_BE_REINSTANTED = "        RPBSPRE000000010                                ERRORMSGRPBS1054DEPENDENT CANNOT BE REINSTATED AS A STUDENT THIS TIME. PLS CONTACT MSP. 9387807484502802293190799262013-03-29Y2022-12";
	
	private static final String R46_NEW_EFFDATE_NOT_EQUQL_OLD = "        RPBSPAJ000000010                                ERRORMSGRPBS0137NEW EFFECTIVE CANNOT EQUAL OLD EFFECTIVE DATE                           633710993319269192022-06-022022-06-02";
	
	private static final String R46_INVALID_COVERAGE_EFF_DATE = "        RPBSPAJ000000010                                ERRORMSGRPBS1020COVERAGE EFFECTIVE DAY INVALID                                          633710993319269192014-06-012022-06-02";
	
	private static final String R46_SUBSCRIBER_NOT_COVERED = "        RPBSPAJ000000010                                ERRORMSGRPBS9109SUBSCRIBER NOT COVERED UNDER THIS GROUP                                 633710993319269192022-07-012022-09-01";
	
	private static final String R46_SUCCESS = "        RPBSPAJ000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  484190498732516932022-01-012022-03-01";
	
	private static final String R46_B_SUCCESS = "        RPBSPAG000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  484190498732516932022-07-312022-08-31                     ";
	
	private static final String R46_B_NEW_CANCELLATION_DATE_EQUALS_OLD = "        RPBSPAG000000010                                ERRORMSGRPBS0138NEW CANCEL DATE CANNOT EQUAL OLD CANCEL DATE                            484190498732516932022-08-312022-08-31                     ";
	
	private static final String R46_B_INVALID_COVERAGE_CANCELLATION_DATE = "        RPBSPAG000000010                                ERRORMSGRPBS1016COVERAGE CANCEL DAY INVALID                                             484190498732516932022-08-012022-08-02                     ";
	
	private static final String R46_B_SUBSCRIBER_NOT_COVERED = "        RPBSPAG000000010                                ERRORMSGRPBS9109SUBSCRIBER NOT COVERED UNDER THIS GROUP                                 484190493319269192022-07-312022-08-31                     ";
	
	private static final String R45_ACTIVE_COVERAGE_EXIST = "        RPBSPAI000000010                                ERRORMSGRPBS0117ACTIVE COVERAGE ALREADY EXISTS FOR THE PHN/GROUP NUMBER ENTERED.        98731026176257760";
	
	private static final String R45_COVERGE_EFFECTIVE_DAY_MUST_BE_01 = "        RPBSPAI000000010                                ERRORMSGRPBS0276COVERGE EFFECTIVE DAY MUST BE 01                                        987310261762577602022-10-02";
	
	private static final String R45_SUBSCRIBER_HAS_FUTURE_COVERAGE = "        RPBSPAI000000010                                ERRORMSGRPBS0049SUBSCRIBER HAS FUTURE COVERAGE.  PLS FORWARD DOCS TO MSP                987310261760997332022-12-01";
	
	private static final String R45_SUBSCRIBER_NOT_COVERED_UNDER_THIS_GROUP = "        RPBSPAI000000010                                ERRORMSGRPBS9109SUBSCRIBER NOT COVERED UNDER THIS GROUP                                 987310261762014532022-12-01";
	
	private static final String R45_UNABLE_TO_PROCESS_AS_A_RENEWAL = "        RPBSPAI000000010                                ERRORMSGRPBS0118UNABLE TO PROCESS AS A RENEWAL.  PLEASE USE REINSTATE CONTRACT TRX.     987310261760997332022-10-01";
	
	private static final String R45_SUCCESS = "        RPBSPAI000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  987309854962674052021-12-019873098549                                                                                          6267405                                                                                                                         550 KP RD                VICTORIA BC                                                                V4R8U8";  
	
	private static final String R30_SUCCESS = "        RPBSPXP000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  6267405               2021-12-01                                                                                                          550 KP RD                VICTORIA BC                                                                V4R8U8                                                            9873098549";
	
	private static final String R51_SUCCESS = "        RPBSPAG000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  606855598730689262022-10-312022-11-30S2022-10-012023-05-31";
	
	private static final String R51_EXPIRY_DATE_MUST_BE_AFTER_ISSUE_DATE = "        RPBSPAG000000010                                ERRORMSGRPBS0210VISA EXPIRY DATE MUST BE AFTER VISA ISSUE DATE                          606855598730689262022-10-312023-10-31S2022-10-012022-05-31";
	
	private static final String R51_INVALID_IMMIGRATION_CODE= "        RPBSPAG000000010                                ERRORMSGRPBS0194IMMIGRATION CODE IS INVALID                                             633710993329124862022-09-302022-10-31nullnullnull";
	
	private static final String R51_VISA_DATE_CHANGE_REJECTED= "        RPBSPAG000000010                                ERRORMSGRPBS0172COVERAGE NOT CANCELLED D, VISA DATE CHANGE REJECTED                     633710993329124862022-09-302022-10-31S2022-10-012023-10-31";
	
	private static final String R44_SUCCESS = "        RPBSPRH000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  987304553162378872022-08-31";
	
	private static final String R44_SUBSCRIBER_NOT_COVERED = "        RPBSPRH000000010                                ERRORMSGRPBS9109SUBSCRIBER NOT COVERED UNDER THIS GROUP                                 933192691963371092022-08-31";
	
	private static final String R44_COVERAGE_ALREADY_EXISTS = "        RPBSPRH000000010                                ERRORMSGRPBS0065COVERAGE ALREADY EXISTS FOR THE PHN/GROUP NUMBER SPECIFIED              987304553162378872022-08-31";
	
	protected static DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
	
	@Autowired
	private MaintenanceController maintenanceController;
   
	@Test
	public void testChangeEffectiveDate_error_NewDateNotEqualOld() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_NEW_EFFDATE_NOT_EQUQL_OLD)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeEffectiveDateRequest changeEffectiveDateRequest = createChangeEffectiveDate(LocalDate.of(2020, 1, 1),LocalDate.of(2021, 1, 1) );
		
        ResponseEntity<ChangeEffectiveDateResponse> response = maintenanceController.changeEffectiveDate(changeEffectiveDateRequest, createHttpServletRequest());
		
        ChangeEffectiveDateResponse changeEffectiveDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, changeEffectiveDateResponse.getStatus());
        assertEquals("RPBS0137 NEW EFFECTIVE CANNOT EQUAL OLD EFFECTIVE DATE", changeEffectiveDateResponse.getMessage());

        assertEquals("9331926919", changeEffectiveDateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHANGE_EFFECTIVE_DATE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);  
	}
	
	@Test
	public void testChangeEffectiveDate_error_InvalidCoverageEffeciveDate() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_INVALID_COVERAGE_EFF_DATE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeEffectiveDateRequest changeEffectiveDateRequest = createChangeEffectiveDate(LocalDate.of(2020, 1, 1),LocalDate.of(2021, 1, 1) );
		
        ResponseEntity<ChangeEffectiveDateResponse> response = maintenanceController.changeEffectiveDate(changeEffectiveDateRequest, createHttpServletRequest());
		
        ChangeEffectiveDateResponse changeEffectiveDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, changeEffectiveDateResponse.getStatus());
        assertEquals("RPBS1020 COVERAGE EFFECTIVE DAY INVALID", changeEffectiveDateResponse.getMessage());

        assertEquals("9331926919", changeEffectiveDateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHANGE_EFFECTIVE_DATE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);  
	}
	
	@Test
	public void testChangeEffectiveDate_error_SubscriberNotCovered() throws Exception {

        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_SUBSCRIBER_NOT_COVERED)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeEffectiveDateRequest changeEffectiveDateRequest = createChangeEffectiveDate(LocalDate.of(2020, 1, 1),LocalDate.of(2021, 1, 1) );
		
        ResponseEntity<ChangeEffectiveDateResponse> response = maintenanceController.changeEffectiveDate(changeEffectiveDateRequest, createHttpServletRequest());
		
        ChangeEffectiveDateResponse changeEffectiveDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, changeEffectiveDateResponse.getStatus());
        assertEquals("RPBS9109 SUBSCRIBER NOT COVERED UNDER THIS GROUP", changeEffectiveDateResponse.getMessage());

        assertEquals("9331926919", changeEffectiveDateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHANGE_EFFECTIVE_DATE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1); 
	}
	
	@Test
	public void testChangeEffectiveDate_success() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeEffectiveDateRequest changeEffectiveDateRequest = createChangeEffectiveDate(LocalDate.of(2022, 1, 1),LocalDate.of(2022, 1, 3) );
		
        ResponseEntity<ChangeEffectiveDateResponse> response = maintenanceController.changeEffectiveDate(changeEffectiveDateRequest, createHttpServletRequest());
		
        ChangeEffectiveDateResponse changeEffectiveDateResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, changeEffectiveDateResponse.getStatus());
        assertEquals("RPBS9014 TRANSACTION COMPLETED", changeEffectiveDateResponse.getMessage());

        assertEquals("9873251693", changeEffectiveDateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHANGE_EFFECTIVE_DATE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testChangeCancelDate_success() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_B_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeCancelDateRequest changeCancelDateRequest = createChangeCancelDateRequest("9873251693", LocalDate.of(2022, 7, 31),LocalDate.of(2022, 8, 31));
		
        ResponseEntity<ChangeCancelDateResponse> response = maintenanceController.changeCancelDate(changeCancelDateRequest, createHttpServletRequest());
		
        ChangeCancelDateResponse changeCancelDateResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, changeCancelDateResponse.getStatus());
        assertEquals("RPBS9014 TRANSACTION COMPLETED", changeCancelDateResponse.getMessage());

        assertEquals("9873251693", changeCancelDateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHANGE_CANCEL_DATE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testChangeCancelDate_error_NewDateCannotEqualOldDate() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_B_NEW_CANCELLATION_DATE_EQUALS_OLD)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeCancelDateRequest changeCancelDateRequest = createChangeCancelDateRequest("9873251693", LocalDate.of(2022, 7, 31),LocalDate.of(2022, 7, 31));
		
        ResponseEntity<ChangeCancelDateResponse> response = maintenanceController.changeCancelDate(changeCancelDateRequest, createHttpServletRequest());
		
        ChangeCancelDateResponse changeCancelDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, changeCancelDateResponse.getStatus());
        assertEquals("RPBS0138 NEW CANCEL DATE CANNOT EQUAL OLD CANCEL DATE", changeCancelDateResponse.getMessage());

        assertEquals("9873251693", changeCancelDateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHANGE_CANCEL_DATE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);  
	}
	
	@Test
	public void testChangeCancelDate_error_InvalidCoverageCancelDate() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_B_INVALID_COVERAGE_CANCELLATION_DATE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeCancelDateRequest changeCancelDateRequest = createChangeCancelDateRequest("9873251693", LocalDate.of(2021, 7, 1),LocalDate.of(2022, 8, 31));
		
        ResponseEntity<ChangeCancelDateResponse> response = maintenanceController.changeCancelDate(changeCancelDateRequest, createHttpServletRequest());
		
        ChangeCancelDateResponse changeCancelDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, changeCancelDateResponse.getStatus());
        assertEquals("RPBS1016 COVERAGE CANCEL DAY INVALID", changeCancelDateResponse.getMessage());

        assertEquals("9873251693", changeCancelDateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHANGE_CANCEL_DATE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);  
	}
	
	@Test
	public void testChangeCancelDate_error_SubscriberNotCovered() throws Exception {

        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_B_SUBSCRIBER_NOT_COVERED)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeCancelDateRequest changeCancelDateRequest = createChangeCancelDateRequest("9331926919", LocalDate.of(2022, 7, 31),LocalDate.of(2022, 8, 31));
		
        ResponseEntity<ChangeCancelDateResponse> response = maintenanceController.changeCancelDate(changeCancelDateRequest, createHttpServletRequest());
		
        ChangeCancelDateResponse changeCancelDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, changeCancelDateResponse.getStatus());
        assertEquals("RPBS9109 SUBSCRIBER NOT COVERED UNDER THIS GROUP", changeCancelDateResponse.getMessage());

        assertEquals("9331926919", changeCancelDateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHANGE_CANCEL_DATE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1); 
	}
	
	public void testReinstateOverAgeDependent_error_invalidStudentEndDate() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R43_INVALID_STUDENT_DATE_DATE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ReinstateOverAgeDependentRequest reinstateRequest = createReinstateOverAgeDependentRequest("9332912486", LocalDate.of(2020, 1, 1));
		
        ResponseEntity<ReinstateOverAgeDependentResponse> response = maintenanceController.reinstateOverAgeDependent(reinstateRequest, createHttpServletRequest());
		
        ReinstateOverAgeDependentResponse reinstateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, reinstateResponse.getStatus());
        assertEquals("RPBS0111 STUDENT END DATE YEAR MUST BE GREATER THAN OR EQUAL TO CURRENT YEAR.", reinstateResponse.getMessage());

        assertEquals("9329279733", reinstateResponse.getPhn());
        
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

        assertEquals("9329279733", reinstateResponse.getPhn());
        
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

        assertEquals("9329279733", reinstateResponse.getPhn());
        
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

        assertEquals("9319079926", reinstateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.REINSTATE_OVER_AGE_DEPENDENT);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 3);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testReinstateOverAgeDependent_success() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R43_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ReinstateOverAgeDependentRequest reinstateRequest = createReinstateOverAgeDependentRequest("9873252394", null);
		
        ResponseEntity<ReinstateOverAgeDependentResponse> response = maintenanceController.reinstateOverAgeDependent(reinstateRequest, createHttpServletRequest());
		
        ReinstateOverAgeDependentResponse reinstateResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, reinstateResponse.getStatus());
        assertEquals("RPBS9014 TRANSACTION COMPLETED", reinstateResponse.getMessage());

        assertEquals("9873252387", reinstateResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.REINSTATE_OVER_AGE_DEPENDENT);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 3);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testRenewCancelledCoverage_error_activeCoverageExist() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R45_ACTIVE_COVERAGE_EXIST)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        RenewCancelledGroupCoverageRequest renewCancelledGroupCoverageRequest = createRenewCancelledGroupCoverageRequest(LocalDate.parse("2022-11-01"));
		
        ResponseEntity<RenewCancelledGroupCoverageResponse> response = maintenanceController.renewCancelledGroupCoverage(renewCancelledGroupCoverageRequest, createHttpServletRequest());
		
        RenewCancelledGroupCoverageResponse renewCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, renewCancelledGroupCoverageResponse.getStatus());
        assertEquals("RPBS0117 ACTIVE COVERAGE ALREADY EXISTS FOR THE PHN/GROUP NUMBER ENTERED.", renewCancelledGroupCoverageResponse.getMessage());

        assertEquals("9873102617", renewCancelledGroupCoverageResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.RENEW_CANCELLED_COVERAGE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testRenewCancelledCoverage_error_invalidCoverageEffectiveDate() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R45_COVERGE_EFFECTIVE_DAY_MUST_BE_01)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        RenewCancelledGroupCoverageRequest renewCancelledGroupCoverageRequest = createRenewCancelledGroupCoverageRequest(LocalDate.parse("2022-10-02"));
		
        ResponseEntity<RenewCancelledGroupCoverageResponse> response = maintenanceController.renewCancelledGroupCoverage(renewCancelledGroupCoverageRequest, createHttpServletRequest());
		
        RenewCancelledGroupCoverageResponse renewCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, renewCancelledGroupCoverageResponse.getStatus());
        assertEquals("RPBS0276 COVERGE EFFECTIVE DAY MUST BE 01", renewCancelledGroupCoverageResponse.getMessage());

        assertEquals("9873102617", renewCancelledGroupCoverageResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.RENEW_CANCELLED_COVERAGE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testRenewCancelledCoverage_error_subscriberHasFutureCoverage() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R45_SUBSCRIBER_HAS_FUTURE_COVERAGE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        RenewCancelledGroupCoverageRequest renewCancelledGroupCoverageRequest = createRenewCancelledGroupCoverageRequest(LocalDate.parse("2022-12-01"));
		
        ResponseEntity<RenewCancelledGroupCoverageResponse> response = maintenanceController.renewCancelledGroupCoverage(renewCancelledGroupCoverageRequest, createHttpServletRequest());
		
        RenewCancelledGroupCoverageResponse renewCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, renewCancelledGroupCoverageResponse.getStatus());
        assertEquals("RPBS0049 SUBSCRIBER HAS FUTURE COVERAGE.  PLS FORWARD DOCS TO MSP", renewCancelledGroupCoverageResponse.getMessage());

        assertEquals("9873102617", renewCancelledGroupCoverageResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.RENEW_CANCELLED_COVERAGE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testRenewCancelledCoverage_error_subscriberNotCovered() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R45_SUBSCRIBER_NOT_COVERED_UNDER_THIS_GROUP)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        RenewCancelledGroupCoverageRequest renewCancelledGroupCoverageRequest = createRenewCancelledGroupCoverageRequest(LocalDate.parse("2022-12-01"));
		
        ResponseEntity<RenewCancelledGroupCoverageResponse> response = maintenanceController.renewCancelledGroupCoverage(renewCancelledGroupCoverageRequest, createHttpServletRequest());
		
        RenewCancelledGroupCoverageResponse renewCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, renewCancelledGroupCoverageResponse.getStatus());
        assertEquals("RPBS9109 SUBSCRIBER NOT COVERED UNDER THIS GROUP", renewCancelledGroupCoverageResponse.getMessage());

        assertEquals("9873102617", renewCancelledGroupCoverageResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.RENEW_CANCELLED_COVERAGE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testRenewCancelledCoverage_error_unableToProcessAsRenewel() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R45_UNABLE_TO_PROCESS_AS_A_RENEWAL)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        RenewCancelledGroupCoverageRequest renewCancelledGroupCoverageRequest = createRenewCancelledGroupCoverageRequest(LocalDate.parse("2022-12-01"));
		
        ResponseEntity<RenewCancelledGroupCoverageResponse> response = maintenanceController.renewCancelledGroupCoverage(renewCancelledGroupCoverageRequest, createHttpServletRequest());
		
        RenewCancelledGroupCoverageResponse renewCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, renewCancelledGroupCoverageResponse.getStatus());
        assertEquals("RPBS0118 UNABLE TO PROCESS AS A RENEWAL.  PLEASE USE REINSTATE CONTRACT TRX.", renewCancelledGroupCoverageResponse.getMessage());

        assertEquals("9873102617", renewCancelledGroupCoverageResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.RENEW_CANCELLED_COVERAGE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
		
	@Test
	public void testRenewCancelledCoverage_success() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R45_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R30_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        RenewCancelledGroupCoverageRequest renewCancelledGroupCoverageRequest = createRenewCancelledGroupCoverageRequest(LocalDate.parse("2022-11-01"));
		
        ResponseEntity<RenewCancelledGroupCoverageResponse> response = maintenanceController.renewCancelledGroupCoverage(renewCancelledGroupCoverageRequest, createHttpServletRequest());
		
        RenewCancelledGroupCoverageResponse renewCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, renewCancelledGroupCoverageResponse.getStatus());
        assertEquals("RPBS9014 TRANSACTION COMPLETED", renewCancelledGroupCoverageResponse.getMessage());

        assertEquals("9873098549", renewCancelledGroupCoverageResponse.getPhn());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.RENEW_CANCELLED_COVERAGE);
        assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
        assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testExtendCancelDate_success() throws Exception {
		mockBackEnd.enqueue(
				new MockResponse().setBody(R51_SUCCESS).addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		ExtendCancelDateRequest extendCancelDateRequest = createExtendCancelDateRequest("9873068926", "6068555",
				LocalDate.parse("2022-10-31"), "S", LocalDate.parse("2022-10-01"), LocalDate.parse("2023-10-31"));

		ResponseEntity<ExtendCancelDateResponse> response = maintenanceController
				.extendCancelDate(extendCancelDateRequest, createHttpServletRequest());

		ExtendCancelDateResponse extendCancelDateResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, extendCancelDateResponse.getStatus());
		assertEquals("RPBS9014 TRANSACTION COMPLETED", extendCancelDateResponse.getMessage());

		assertEquals("9873068926", extendCancelDateResponse.getPhn());

		// Check the client request is sent as expected
		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
		assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));

		assertTransactionCreated(TransactionType.EXTEND_CANCEL_DATE);
		assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
		assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}

	@Test
	public void testExtendCancelDate_error_invalidImmigrationCode() throws Exception {
		mockBackEnd.enqueue(new MockResponse().setBody(R51_INVALID_IMMIGRATION_CODE).addHeader(CONTENT_TYPE,
				MediaType.TEXT_PLAIN.toString()));

		ExtendCancelDateRequest extendCancelDateRequest = createExtendCancelDateRequest("9332912486", "6337109",
				LocalDate.parse("2022-10-31"), null, null, null);

		ResponseEntity<ExtendCancelDateResponse> response = maintenanceController
				.extendCancelDate(extendCancelDateRequest, createHttpServletRequest());

		ExtendCancelDateResponse extendCancelDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, extendCancelDateResponse.getStatus());
		assertEquals("RPBS0194 IMMIGRATION CODE IS INVALID", extendCancelDateResponse.getMessage());

		assertEquals("9332912486", extendCancelDateResponse.getPhn());

		// Check the client request is sent as expected
		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
		assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));

		assertTransactionCreated(TransactionType.EXTEND_CANCEL_DATE);
		assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
		assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}

	@Test
	public void testExtendCancelDate_error_expiryDateMustBeAfterIssueDate() throws Exception {
		mockBackEnd.enqueue(new MockResponse().setBody(R51_EXPIRY_DATE_MUST_BE_AFTER_ISSUE_DATE).addHeader(CONTENT_TYPE,
				MediaType.TEXT_PLAIN.toString()));

		ExtendCancelDateRequest extendCancelDateRequest = createExtendCancelDateRequest("9873068926", "6068555",
				LocalDate.parse("2022-10-31"), "S", LocalDate.parse("2023-10-01"), LocalDate.parse("2022-10-31"));

		ResponseEntity<ExtendCancelDateResponse> response = maintenanceController
				.extendCancelDate(extendCancelDateRequest, createHttpServletRequest());

		ExtendCancelDateResponse extendCancelDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, extendCancelDateResponse.getStatus());
		assertEquals("RPBS0210 VISA EXPIRY DATE MUST BE AFTER VISA ISSUE DATE", extendCancelDateResponse.getMessage());

		assertEquals("9873068926", extendCancelDateResponse.getPhn());

		// Check the client request is sent as expected
		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
		assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));

		assertTransactionCreated(TransactionType.EXTEND_CANCEL_DATE);
		assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
		assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}

	@Test
	public void testExtendCancelDate_error_visaDateChangeRejected() throws Exception {
		mockBackEnd.enqueue(new MockResponse().setBody(R51_VISA_DATE_CHANGE_REJECTED).addHeader(CONTENT_TYPE,
				MediaType.TEXT_PLAIN.toString()));

		ExtendCancelDateRequest extendCancelDateRequest = createExtendCancelDateRequest("9332912486", "6337109",
				LocalDate.parse("2022-10-31"), "S", LocalDate.parse("2022-10-01"), LocalDate.parse("2023-10-31"));

		ResponseEntity<ExtendCancelDateResponse> response = maintenanceController
				.extendCancelDate(extendCancelDateRequest, createHttpServletRequest());

		ExtendCancelDateResponse extendCancelDateResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, extendCancelDateResponse.getStatus());
		assertEquals("RPBS0172 COVERAGE NOT CANCELLED D, VISA DATE CHANGE REJECTED",
				extendCancelDateResponse.getMessage());

		assertEquals("9332912486", extendCancelDateResponse.getPhn());

		// Check the client request is sent as expected
		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
		assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));

		assertTransactionCreated(TransactionType.EXTEND_CANCEL_DATE);
		assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
		assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testReinstateCancelledGroupCoverage_error_coverageAlreadyExists() throws Exception {
		mockBackEnd.enqueue(new MockResponse().setBody(R44_COVERAGE_ALREADY_EXISTS).addHeader(CONTENT_TYPE,
				MediaType.TEXT_PLAIN.toString()));

		ReinstateCancelledGroupCoverageRequest reinstateCancelledGroupCoverageRequest = createReinstateCancelledGroupCoverageRequest("9873045531", "6237887",
				LocalDate.parse("2022-08-31"));

		ResponseEntity<ReinstateCancelledGroupCoverageResponse> response = maintenanceController
				.reinstateCancelledGroupCoverage(reinstateCancelledGroupCoverageRequest, createHttpServletRequest());

		ReinstateCancelledGroupCoverageResponse reinstateCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, reinstateCancelledGroupCoverageResponse.getStatus());
		assertEquals("RPBS0065 COVERAGE ALREADY EXISTS FOR THE PHN/GROUP NUMBER SPECIFIED",
				reinstateCancelledGroupCoverageResponse.getMessage());

		assertEquals("9873045531", reinstateCancelledGroupCoverageResponse.getPhn());

		// Check the client request is sent as expected
		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
		assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));

		assertTransactionCreated(TransactionType.REINSTATE_CANCELLED_COVERAGE);
		assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
		assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testReinstateCancelledGroupCoverage_error_subscriberNotCovered() throws Exception {
		mockBackEnd.enqueue(new MockResponse().setBody(R44_SUBSCRIBER_NOT_COVERED).addHeader(CONTENT_TYPE,
				MediaType.TEXT_PLAIN.toString()));

		ReinstateCancelledGroupCoverageRequest reinstateCancelledGroupCoverageRequest = createReinstateCancelledGroupCoverageRequest("9331926919", "6237887",
				LocalDate.parse("2022-08-31"));

		ResponseEntity<ReinstateCancelledGroupCoverageResponse> response = maintenanceController
				.reinstateCancelledGroupCoverage(reinstateCancelledGroupCoverageRequest, createHttpServletRequest());

		ReinstateCancelledGroupCoverageResponse reinstateCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, reinstateCancelledGroupCoverageResponse.getStatus());
		assertEquals("RPBS9109 SUBSCRIBER NOT COVERED UNDER THIS GROUP",
				reinstateCancelledGroupCoverageResponse.getMessage());

		assertEquals("9331926919", reinstateCancelledGroupCoverageResponse.getPhn());

		// Check the client request is sent as expected
		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
		assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));

		assertTransactionCreated(TransactionType.REINSTATE_CANCELLED_COVERAGE);
		assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
		assertAffectedPartyCount(AffectedPartyDirection.OUTBOUND, 1);
	}
	
	@Test
	public void testReinstateCancelledGroupCoverage_success() throws Exception {
		mockBackEnd.enqueue(new MockResponse().setBody(R44_SUCCESS).addHeader(CONTENT_TYPE,
				MediaType.TEXT_PLAIN.toString()));

		ReinstateCancelledGroupCoverageRequest reinstateCancelledGroupCoverageRequest = createReinstateCancelledGroupCoverageRequest("9331926919", "6237887",
				LocalDate.parse("2022-08-31"));

		ResponseEntity<ReinstateCancelledGroupCoverageResponse> response = maintenanceController
				.reinstateCancelledGroupCoverage(reinstateCancelledGroupCoverageRequest, createHttpServletRequest());

		ReinstateCancelledGroupCoverageResponse reinstateCancelledGroupCoverageResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, reinstateCancelledGroupCoverageResponse.getStatus());
		assertEquals("RPBS9014 TRANSACTION COMPLETED",
				reinstateCancelledGroupCoverageResponse.getMessage());

		assertEquals("9873045531", reinstateCancelledGroupCoverageResponse.getPhn());

		// Check the client request is sent as expected
		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
		assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));

		assertTransactionCreated(TransactionType.REINSTATE_CANCELLED_COVERAGE);
		assertAffectedPartyCount(AffectedPartyDirection.INBOUND, 2);
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
    
	private ChangeEffectiveDateRequest createChangeEffectiveDate(LocalDate newEffectiveDate, LocalDate oldEffectiveDate) {
		ChangeEffectiveDateRequest changeEffDateRequest = new ChangeEffectiveDateRequest();
		changeEffDateRequest.setGroupNumber("6337109");
		changeEffDateRequest.setPhn("9331926919");
		
		changeEffDateRequest.setExistingEffectiveDate(LocalDate.of(1990, 1, 1));
		changeEffDateRequest.setNewEffectiveDate(LocalDate.of(2001, 1, 1));
		
		return changeEffDateRequest;
	}
	
	private ChangeCancelDateRequest createChangeCancelDateRequest(String phn, LocalDate existingCancellationDate, LocalDate newCancellationDate) {
		ChangeCancelDateRequest changeCancelDateRequest = new ChangeCancelDateRequest();
		changeCancelDateRequest.setGroupNumber("6337109");
		changeCancelDateRequest.setPhn(phn);
		
		changeCancelDateRequest.setExistingCancellationDate(existingCancellationDate);
		changeCancelDateRequest.setNewCancellationDate(newCancellationDate);
		
		return changeCancelDateRequest;
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
	
	private ReinstateCancelledGroupCoverageRequest createReinstateCancelledGroupCoverageRequest(String phn, String groupNumber, LocalDate cancelDateToBeCovered) {
		ReinstateCancelledGroupCoverageRequest reinstateRequest = new ReinstateCancelledGroupCoverageRequest();
		reinstateRequest.setGroupNumber(groupNumber);
		reinstateRequest.setPhn(phn);
		reinstateRequest.setCancelDateToBeRemoved(cancelDateToBeCovered);
				
		return reinstateRequest;
	}
	
	private RenewCancelledGroupCoverageRequest createRenewCancelledGroupCoverageRequest(LocalDate newEffectiveDate) {
		RenewCancelledGroupCoverageRequest renewCancelledGroupCoverageRequest =new RenewCancelledGroupCoverageRequest();
		renewCancelledGroupCoverageRequest.setPhn("9873102617");
		renewCancelledGroupCoverageRequest.setGroupNumber("6099733");
		renewCancelledGroupCoverageRequest.setNewCoverageEffectiveDate(newEffectiveDate);
		return renewCancelledGroupCoverageRequest;
	}
	
	private ExtendCancelDateRequest createExtendCancelDateRequest(String phn, String groupNumber,
			LocalDate newCancelDate, String immigrationCode, LocalDate issueDate, LocalDate expiryDate) {
		ExtendCancelDateRequest extendCancelDateRequest = new ExtendCancelDateRequest();
		extendCancelDateRequest.setPhn(phn);
		extendCancelDateRequest.setGroupNumber(groupNumber);
		extendCancelDateRequest.setNewCancellationDate(newCancelDate);
		extendCancelDateRequest.setExistingCancellationDate(LocalDate.parse("2022-09-30"));
		extendCancelDateRequest.setPermitIssueDate(issueDate);
		extendCancelDateRequest.setPermitExpiryDate(expiryDate);
		extendCancelDateRequest.setImmigrationCode(immigrationCode);
		return extendCancelDateRequest;
	}
	
}
