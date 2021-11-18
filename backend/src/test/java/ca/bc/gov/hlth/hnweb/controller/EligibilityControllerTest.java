package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.CheckMspCoverageStatusRequest;
import ca.bc.gov.hlth.hnweb.service.EligibilityService;
import ca.uhn.hl7v2.HL7Exception;

@SpringBootTest
public class EligibilityControllerTest {
	
	/**
	 * Inject the service which needs to be mocked out. @MockBean tells Spring to mock out this bean
	 * anytime it's injected
	 */
	@MockBean
	private EligibilityService eligibilityServiceMock;

	/**
	 * Inject the Controller being tested using the standard @Autowired annotation.
	 */
	@Autowired
	private EligibilityController eligibilityController;
	
	@Test
	public void testCheckEligibility_success() throws ParseException {
		// 1. Set up our test data
		String phn = "9890608412";
		Date eligibilityDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-01");
		String reason = "This is a mocked out response";
		
		// 2. Create a mock response object
		CheckEligibilityResponse mockResponse = new CheckEligibilityResponse();
		mockResponse.setPhn(phn);
		mockResponse.setReason("This is a mocked out response");
		
		// 3. Return the mock response object when the service is called
		// Note, the parameters need to match what is sent
		when(eligibilityServiceMock.checkEligibility(phn, eligibilityDate)).thenReturn(mockResponse);

		// 4. Perform assertions
		// These assertions aren't that interested since our service is returning the REST model directly
		// Once the service returns the HL7/Other business entity, the tests will be validating the conversion logic
		// in the controller
		ResponseEntity<CheckEligibilityResponse> response = eligibilityController.checkEligibility(phn, eligibilityDate);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		CheckEligibilityResponse checkEligibilityResponse = response.getBody();
		assertEquals(phn, checkEligibilityResponse.getPhn());
		assertEquals(reason, checkEligibilityResponse.getReason());
	}
	
	@Test
	public void testCheckEligibility_exception() throws ParseException {
		// 1. Set up our test data
		String phn = "9890608411";
		Date eligibilityDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-01");
		
		// 2. Create a mock response object
		CheckEligibilityResponse mockResponse = new CheckEligibilityResponse();
		mockResponse.setPhn(phn);
		mockResponse.setReason("This is a mocked out response");
		
		// 3. Return the mock response object when the service is called
		// Note, the parameters need to match what is sent
		// Force a failure on an invalid PHN
		when(eligibilityServiceMock.checkEligibility(phn, eligibilityDate)).thenThrow(new IllegalArgumentException());

		// 4. Perform assertions
		ResponseStatusException responseException = assertThrows(ResponseStatusException.class, () -> {
        	eligibilityController.checkEligibility(phn, eligibilityDate);
        });
		assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
		assertEquals("Bad /checkEligibility request", responseException.getReason());
	}

	@Test
	public void testCheckMspCoverageStatus_success() throws HL7Exception {

		CheckMspCoverageStatusRequest checkMspCoverageStatusRequest = createCheckMspCoverageStatusRequest("9873944324", LocalDate.of(1973, 8, 11), LocalDate.now(), true, false, null);
		
		//TODO (daveb-hni) The response is hard-coded currently so this this needs to be updated to Web Client test similar to EnrollmentControllerTest.testEnrollSubscriber_Error() once the endpoint connection is completed in EligibilityController.checkMspCoverageStatus 		
//		ResponseEntity<CheckMspCoverageStatusResponse> responseEntity = eligibilityController.checkMspCoverageStatus(checkMspCoverageStatusRequest);		
	}
	
	private CheckMspCoverageStatusRequest createCheckMspCoverageStatusRequest(String phn, LocalDate dateOfBirth, LocalDate dateOfService,
			Boolean checkSubsidyInsuredService, Boolean checkLastEyeExam, Boolean checkPatientRestriction) {
		
		CheckMspCoverageStatusRequest checkMspCoverageStatusRequest = new CheckMspCoverageStatusRequest();
		checkMspCoverageStatusRequest.setPhn(phn);
		checkMspCoverageStatusRequest.setDateOfBirth(dateOfBirth);
		checkMspCoverageStatusRequest.setDateOfService(dateOfService);
		checkMspCoverageStatusRequest.setCheckSubsidyInsuredService(checkSubsidyInsuredService);
		checkMspCoverageStatusRequest.setCheckLastEyeExam(checkLastEyeExam);
		checkMspCoverageStatusRequest.setCheckPatientRestriction(checkPatientRestriction);
		
		return checkMspCoverageStatusRequest;
	}
	
}
