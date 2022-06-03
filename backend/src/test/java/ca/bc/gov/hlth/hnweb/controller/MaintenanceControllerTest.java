package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

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
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

class MaintenanceControllerTest extends BaseControllerTest {
	
	private static final String R46_NEW_EFFDATE_NOT_EQUQL_OLD = "        RPBSPAJ000000010                                ERRORMSGRPBS0137NEW EFFECTIVE CANNOT EQUAL OLD EFFECTIVE DATE                           633710993319269192022-06-022022-06-02";
	
	private static final String R46_INVALID_COVERAGE_EFF_DATE = "        RPBSPAJ000000010                                ERRORMSGRPBS1020COVERAGE EFFECTIVE DAY INVALID                                          633710993319269192014-06-012022-06-02";
	
	@Autowired
	private MaintenanceController maintenanceController;
   
	@Test
	public void testChangeEffectiveDate_error_NewDateNotEqualOld() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_NEW_EFFDATE_NOT_EQUQL_OLD)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeEffectiveDateRequest changeEffectiveDateRequest = changeEffectiveDate(LocalDate.of(2020, 1, 1),LocalDate.of(2021, 1, 1) );
		
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
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
   
	}
	
	@Test
	public void testChangeEffectiveDate_error_InvalidCoverageEffeciveDate() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R46_INVALID_COVERAGE_EFF_DATE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ChangeEffectiveDateRequest changeEffectiveDateRequest = changeEffectiveDate(LocalDate.of(2020, 1, 1),LocalDate.of(2021, 1, 1) );
		
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
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
   
	}


	/**
	 * The URL property used by the mocked endpoint needs to be set after the
	 * MockWebServer starts as the port it uses is created dynamically on start up
	 * to ensure it uses an available port so it is not known before then.
	 * 
	 * @param registry
	 */
	@DynamicPropertySource
	static void registerMockUrlProperty(DynamicPropertyRegistry registry) {
		registry.add("rapid.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
	}
	

	private ChangeEffectiveDateRequest changeEffectiveDate(LocalDate newEffectiveDate, LocalDate oldEffectiveDate) {
		ChangeEffectiveDateRequest changeEffDateRequest = new ChangeEffectiveDateRequest();
		changeEffDateRequest.setGroupNumber("6337109");
		changeEffDateRequest.setPhn("9331926919");
		
		changeEffDateRequest.setExistingEffectiveDate(LocalDate.of(1990, 1, 1));
		changeEffDateRequest.setNewEffectiveDate(LocalDate.of(2001, 1, 1));
		
		return changeEffDateRequest;
	}
}
