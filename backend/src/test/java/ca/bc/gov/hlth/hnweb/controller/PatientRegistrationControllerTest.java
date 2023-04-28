package ca.bc.gov.hlth.hnweb.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegisterModel;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegistrationRequest;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegistrationResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PBFClinicPayee;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PBFClinicPayeeRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PatientRegisterRepository;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.utils.TestUtil;
import okhttp3.mockwebserver.MockResponse;

/**
 * JUnit test class for PatientRegistrationController
 *
 */
@Sql({ "classpath:scripts/user_payee_mapping.sql" })
public class PatientRegistrationControllerTest extends BaseControllerTest {

	@Autowired
	private PatientRegistrationController patientRegistrationController;

	@Autowired
	private PatientRegisterRepository patientRegisterRepository;

	@Autowired
	private PBFClinicPayeeRepository pbfClinicPayeeRepository;
	
	@Test
	public void testRegistrationHistory_success_nullCancelDate() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "924917e3-970a-482d-88b5-244be4c19d70", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("7363117303");
		viewPatientRegisterRequest.setPayee("X0053");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		
		// Check the additional message , status and number of valid records
		assertEquals(StatusEnum.SUCCESS, patientRegistrationResponse.getStatus());
		assertEquals(1, patientRegistrationHistory.size());
	}
	
	@Test
	public void testRegistrationHistory_success_payeeWithinGroup_DemoRecord() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "3c0fc3b5-45f8-4745-afa9-b7b04978023d", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("9879869673");
		viewPatientRegisterRequest.setPayee("T0055");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		
		// Check the additional message , status and number of valid records
		assertEquals(StatusEnum.SUCCESS, patientRegistrationResponse.getStatus());
		assertEquals(1, patientRegistrationHistory.size());
		
		GetPersonDetailsResponse personDetails = patientRegistrationResponse.getPersonDetail();

		assertEquals("19620624", personDetails.getDateOfBirth());
		assertEquals("20050605", personDetails.getDateOfDeath());
		assertEquals("M", personDetails.getGender());
		assertEquals("Robert", personDetails.getGivenName());
		assertEquals("9862716574", personDetails.getPhn());
		assertEquals("Smith", personDetails.getSurname());
		
		PatientRegisterModel patientRegister = patientRegistrationHistory.get(0);
		assertEquals("0", patientRegister.getAdministrativeCode());
		assertEquals("99991231", patientRegister.getCancelDate());
		assertEquals("Q", patientRegister.getCancelReasonCode());
		assertEquals("Registered", patientRegister.getCurrentStatus());
		assertEquals("N/A", patientRegister.getDeregistrationReasonCode());
		assertEquals("20210705", patientRegister.getEffectiveDate());
		assertEquals("T0055", patientRegister.getPayeeNumber());
		assertEquals("9879869673", patientRegister.getPhn());
		assertEquals("X2753", patientRegister.getRegisteredPractitionerNumber());
		assertEquals("John", patientRegister.getRegisteredPractitionerFirstName());
		assertEquals("W", patientRegister.getRegisteredPractitionerMiddleName());
		assertEquals("Smith", patientRegister.getRegisteredPractitionerSurname());
		assertEquals("SL", patientRegister.getRegistrationReasonCode());
	}

	@Test
	public void testRegistrationHistory_success_payeeWithinGroup_NoDemoRecord() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_Error.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "3c0fc3b5-45f8-4745-afa9-b7b04978023d", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("9879869673");
		viewPatientRegisterRequest.setPayee("T0055");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		// Check the additional message , status and number of valid records

		assertEquals(StatusEnum.SUCCESS, patientRegistrationResponse.getStatus());
		assertEquals(1, patientRegistrationHistory.size());
		assertEquals(StatusEnum.ERROR, patientRegistrationResponse.getPersonDetail().getStatus());
		assertEquals(null, patientRegistrationResponse.getPersonDetail().getGivenName());
	}

	@Test
	public void testRegistrationHistory_success_diffPayeeWithinGroup() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_Error.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "e4414a89-8974-4cff-9677-d9d2df6f9cfb", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("9879869673");
		viewPatientRegisterRequest.setPayee("T0053");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		
		// Check the additional message , status and number of valid records
		String additionalMessage = "Patient is registered with a different MSP Payee number within the reporting group";
		assertTrue(patientRegistrationResponse.getAdditionalInfoMessage().contains(additionalMessage));
		assertEquals(StatusEnum.SUCCESS, patientRegistrationResponse.getStatus());
		assertEquals(1, patientRegistrationHistory.size());
	}
	
	@Test
	public void testRegistrationHistory_success_newerPayeeOutsideGroup() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_Error.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "e4414a89-8974-4cff-9677-d9d2df6f9cfb", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("9879869673");
		viewPatientRegisterRequest.setPayee("T0053");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		
		// Check the additional message , status and number of valid records
		String additionalMessage = "Patient is registered with a different MSP Payee number within the reporting group";
		assertTrue(patientRegistrationResponse.getAdditionalInfoMessage().contains(additionalMessage));
		assertEquals(StatusEnum.SUCCESS, patientRegistrationResponse.getStatus());
		assertEquals(1, patientRegistrationHistory.size());
	}

	/**
	 * Tests that a warning message shows when the PHN exists within the reporting group BUT a newer record (via effective date)
	 * exists outside the reporting group.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRegistrationHistory_success_diffPayeeOutsideGroup() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "e4414a89-8974-4cff-9677-d9d2df6f9cfb", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

		PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("7363117304");
		viewPatientRegisterRequest.setPayee("T0053");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		
		// Check the additional message , status and number of valid records
		String additionalMessage = "Patient is registered with a different MSP Payee number outside of reporting group";
		assertTrue(patientRegistrationResponse.getAdditionalInfoMessage().contains(additionalMessage));
		assertEquals(StatusEnum.SUCCESS, patientRegistrationResponse.getStatus());
		assertEquals(1, patientRegistrationHistory.size());
	}

	@Test
	public void testRegistrationHistory_warning_NoPBFAndDemographicsRecord() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_Error.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "924917e3-970a-482d-88b5-244be4c19d70", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("7363117302");
		viewPatientRegisterRequest.setPayee("X0053");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		// Check the additional message , status and number of valid records
		assertEquals(StatusEnum.WARNING, patientRegistrationResponse.getStatus());
		assertEquals(0, patientRegistrationHistory.size());
	}

	@Test
	public void testRegistrationHistory_failure_no_payee_mapping_found() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		//Note, don't enqueue a response as it is never requested and so will remain queued for next test when they are run together.
		
		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "1b85225b-58cc-4430-9dc9-0199057afdff", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));
        
        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("9879869673");
		viewPatientRegisterRequest.setPayee("T0055");

		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> patientRegistrationController.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest()))
		.withMessage("400 BAD_REQUEST \"400 BAD_REQUEST \"No Payee Number mapping was found for the current user\"\"; nested exception is org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST \"No Payee Number mapping was found for the current user\"");
	}

	@Test
	public void testRegistrationHistory_failure_incorrect_payee_mapping_found() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		//Note, don't enqueue a response as it is never requested and so will remain queued for next test when they are run together.

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "3c0fc3b5-45f8-4745-afa9-b7b04978023d", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));
        
        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("9879869673");
		viewPatientRegisterRequest.setPayee("T0053");

		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> patientRegistrationController.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest()))
		.withMessage("400 BAD_REQUEST \"400 BAD_REQUEST \"Payee field value T0053 does not match the Payee Number mapped to this user\"\"; nested exception is org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST \"Payee field value T0053 does not match the Payee Number mapped to this user\"");
	}
	
    @Test
    public void testRegistrationHistory_failure_inactive_cancelled() throws Exception {
        createPBFClinicPayee();
        createPatientRegister();

        //Note, don't enqueue a response as it is never requested and so will remain queued for next test when they are run together.

        //Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "a9c3b536-4598-411a-bda2-4068d6b5cc20", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));
        
        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
        viewPatientRegisterRequest.setPhn("9879869673");
        viewPatientRegisterRequest.setPayee("00053");

        assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> patientRegistrationController.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest()))
        .withMessage("400 BAD_REQUEST \"400 BAD_REQUEST \"Payee 00053 is not active\"\"; nested exception is org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST \"Payee 00053 is not active\"");
    }
    
	@Test
	public void testRegistrationHistory_archived() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_Error.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "924917e3-970a-482d-88b5-244be4c19d70", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("7363117302");
		viewPatientRegisterRequest.setPayee("X0053");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		// Check the additional message , status and number of valid records
		assertEquals(StatusEnum.WARNING, patientRegistrationResponse.getStatus());
		assertEquals(0, patientRegistrationHistory.size());
	}
	
	@Test
	public void testRegistrationHistory_success_reasonDescriptions() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		//Override the base setup of the user to ensure we return the User with the User ID mapped to the this Payee Number 
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "924917e3-970a-482d-88b5-244be4c19d70", "00000010", "Ministry of Health", "hnweb-user", UUID.randomUUID().toString()));

        PatientRegistrationRequest viewPatientRegisterRequest = new PatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("7363117305");
		viewPatientRegisterRequest.setPayee("X0053");
		ResponseEntity<PatientRegistrationResponse> response = patientRegistrationController
				.getPatientRegistration(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		PatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse
				.getPatientRegistrationHistory();
		
		// Check the additional message , status and number of valid records
		assertEquals(StatusEnum.SUCCESS, patientRegistrationResponse.getStatus());
		assertEquals(1, patientRegistrationHistory.size());
		
		PatientRegisterModel patientRegister = patientRegistrationHistory.get(0);
		assertEquals("0", patientRegister.getAdministrativeCode());
		assertEquals("99991231", patientRegister.getCancelDate());
		assertEquals("A", patientRegister.getCancelReasonCode());
		assertEquals("CANCELLED", patientRegister.getCancelReasonDesc());
		assertEquals("Registered", patientRegister.getCurrentStatus());
		assertEquals("A0", patientRegister.getDeregistrationReasonCode());
		assertEquals("DECEASED", patientRegister.getDeregistrationReasonDesc());
		assertEquals("01", patientRegister.getRegistrationReasonCode());
		assertEquals("LIVES IN CATCHMENT AREA", patientRegister.getRegistrationReasonDesc());
	}

	private void createPatientRegister() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		
		Date cancelDate = format.parse("99991231");

		PatientRegister patientRegister1 = new PatientRegister();
		patientRegister1.setAdministrativeCode("0");
		patientRegister1.setEffectiveDate(format.parse("20210705"));
		patientRegister1.setCancelDate(cancelDate);
		patientRegister1.setCancelReasonCode("Q");
		patientRegister1.setRegistrationReasonCode("SL");
		patientRegister1.setPayeeNumber("T0055");
		patientRegister1.setRegisteredPractitionerNumber("X2753");
		patientRegister1.setRegisteredPractitionerFirstName("John");
		patientRegister1.setRegisteredPractitionerMiddleName("W");
		patientRegister1.setRegisteredPractitionerSurname("Smith");
		patientRegister1.setArchived(Boolean.FALSE);
		patientRegister1.setPhn("9879869673");

		patientRegisterRepository.save(patientRegister1);

		PatientRegister patientRegister2 = new PatientRegister();
		patientRegister2.setAdministrativeCode("0");
		patientRegister2.setEffectiveDate(format.parse("20210705"));
		patientRegister2.setCancelDate(cancelDate);
		patientRegister2.setRegistrationReasonCode("SL");
		patientRegister2.setPayeeNumber("T0053");
		patientRegister2.setRegisteredPractitionerNumber("X2753");
		patientRegister2.setRegisteredPractitionerFirstName("Wendy");
		patientRegister2.setRegisteredPractitionerMiddleName("B");
		patientRegister2.setRegisteredPractitionerSurname("Moore");
		patientRegister2.setArchived(Boolean.FALSE);
		patientRegister2.setPhn("7108641380");

		PatientRegister patientRegister3 = new PatientRegister();
		patientRegister3.setAdministrativeCode("0");
		patientRegister3.setEffectiveDate(format.parse("20210705"));
		patientRegister3.setCancelDate(cancelDate);
		patientRegister3.setRegistrationReasonCode("SL");
		patientRegister3.setPayeeNumber("T0053");
		patientRegister3.setRegisteredPractitionerNumber("X2753");
		patientRegister3.setRegisteredPractitionerFirstName("Mike");
		patientRegister3.setRegisteredPractitionerMiddleName("T");
		patientRegister3.setRegisteredPractitionerSurname("Jones");
		patientRegister3.setArchived(Boolean.FALSE);
		patientRegister3.setPhn("7980823201");

		PatientRegister patientRegister4 = new PatientRegister();
		patientRegister4.setAdministrativeCode("0");
		patientRegister4.setEffectiveDate(format.parse("20210705"));
		patientRegister4.setCancelDate(cancelDate);
		patientRegister4.setRegistrationReasonCode("SL");
		patientRegister4.setPayeeNumber("X0058");
		patientRegister4.setRegisteredPractitionerNumber("X2753");
		patientRegister4.setRegisteredPractitionerFirstName("Jack");
		patientRegister4.setRegisteredPractitionerMiddleName("J");
		patientRegister4.setRegisteredPractitionerSurname("Johnson");
		patientRegister4.setArchived(Boolean.FALSE);
		patientRegister4.setPhn("7363117301");

		PatientRegister patientRegister5 = new PatientRegister();
		patientRegister5.setAdministrativeCode("0");
		patientRegister5.setEffectiveDate(format.parse("20210705"));
		patientRegister5.setCancelDate(cancelDate);
		patientRegister5.setRegistrationReasonCode("SL");
		patientRegister5.setPayeeNumber("X0053");
		patientRegister5.setRegisteredPractitionerNumber("X2753");
		patientRegister5.setRegisteredPractitionerFirstName("Sam");
		patientRegister5.setRegisteredPractitionerMiddleName("E");
		patientRegister5.setRegisteredPractitionerSurname("Thomas");
		patientRegister5.setArchived(Boolean.TRUE);
		patientRegister5.setPhn("7363117302");
		
		PatientRegister patientRegister6 = new PatientRegister();
		patientRegister6.setAdministrativeCode("0");
		patientRegister6.setEffectiveDate(format.parse("20230101"));
		patientRegister6.setCancelDate(null);
		patientRegister6.setRegistrationReasonCode("SL");
		patientRegister6.setPayeeNumber("X0053");
		patientRegister6.setRegisteredPractitionerNumber("X2753");
		patientRegister6.setRegisteredPractitionerFirstName("Sam");
		patientRegister6.setRegisteredPractitionerMiddleName("E");
		patientRegister6.setRegisteredPractitionerSurname("Thomas");
		patientRegister6.setArchived(Boolean.FALSE);
		patientRegister6.setPhn("7363117303");
		
		PatientRegister patientRegister7 = new PatientRegister();
		patientRegister7.setAdministrativeCode("0");
		patientRegister7.setEffectiveDate(format.parse("20230101"));
		patientRegister7.setCancelDate(null);
		patientRegister7.setRegistrationReasonCode("SL");
		patientRegister7.setPayeeNumber("T0053");
		patientRegister7.setRegisteredPractitionerNumber("X2753");
		patientRegister7.setRegisteredPractitionerFirstName("Sam");
		patientRegister7.setRegisteredPractitionerMiddleName("E");
		patientRegister7.setRegisteredPractitionerSurname("Thomas");
		patientRegister7.setArchived(Boolean.FALSE);
		patientRegister7.setPhn("7363117304");
		
		PatientRegister patientRegister8 = new PatientRegister();
		patientRegister8.setAdministrativeCode("0");
		patientRegister8.setEffectiveDate(format.parse("20240101"));
		patientRegister8.setCancelDate(null);
		patientRegister8.setRegistrationReasonCode("SL");
		patientRegister8.setPayeeNumber("X0053");
		patientRegister8.setRegisteredPractitionerNumber("X2753");
		patientRegister8.setRegisteredPractitionerFirstName("Sam");
		patientRegister8.setRegisteredPractitionerMiddleName("E");
		patientRegister8.setRegisteredPractitionerSurname("Thomas");
		patientRegister8.setArchived(Boolean.FALSE);
		patientRegister8.setPhn("7363117304");
		
		// This record is somewhat artificial in that all the reason codes are set
		// It's just used for testing description mapping
		PatientRegister patientRegister9 = new PatientRegister();
		patientRegister9.setAdministrativeCode("0");
		patientRegister9.setEffectiveDate(format.parse("20230101"));
		patientRegister9.setCancelDate(cancelDate);
		patientRegister9.setCancelReasonCode("A");
		patientRegister9.setDeregistrationReasonCode("A0");
		patientRegister9.setRegistrationReasonCode("01");
		patientRegister9.setPayeeNumber("X0053");
		patientRegister9.setRegisteredPractitionerNumber("X2753");
		patientRegister9.setRegisteredPractitionerFirstName("Sam");
		patientRegister9.setRegisteredPractitionerMiddleName("E");
		patientRegister9.setRegisteredPractitionerSurname("Thomas");
		patientRegister9.setArchived(Boolean.FALSE);
		patientRegister9.setPhn("7363117305");

		patientRegisterRepository.save(patientRegister1);
		patientRegisterRepository.save(patientRegister2);
		patientRegisterRepository.save(patientRegister3);
		patientRegisterRepository.save(patientRegister4);
		patientRegisterRepository.save(patientRegister5);
		patientRegisterRepository.save(patientRegister6);
		patientRegisterRepository.save(patientRegister7);
		patientRegisterRepository.save(patientRegister8);
		patientRegisterRepository.save(patientRegister9);
	}

	private void createPBFClinicPayee() {
		PBFClinicPayee payee = new PBFClinicPayee();
		payee.setArchived(Boolean.FALSE);
		Date cancelDate = new GregorianCalendar(9999, 12, 31).getTime();
		payee.setCancelDate(cancelDate);
		payee.setPayeeNumber("T0053");
		Date effectiveDate = new GregorianCalendar(2021, 7, 5).getTime();
		payee.setEffectiveDate(effectiveDate);
		payee.setReportGroup("18579");

		pbfClinicPayeeRepository.save(payee);

		PBFClinicPayee payee1 = new PBFClinicPayee();
		payee1.setArchived(Boolean.FALSE);
		Date cancelDate1 = new GregorianCalendar(9999, 12, 31).getTime();
		payee1.setCancelDate(cancelDate1);
		payee1.setPayeeNumber("T0055");
		Date effectiveDate1 = new GregorianCalendar(2021, 7, 5).getTime();
		payee1.setEffectiveDate(effectiveDate1);
		payee1.setReportGroup("18579");

		PBFClinicPayee payee2 = new PBFClinicPayee();
		payee2.setArchived(Boolean.FALSE);
		Date cancelDate2 = new GregorianCalendar(9999, 12, 31).getTime();
		payee2.setCancelDate(cancelDate2);
		payee2.setPayeeNumber("X0058");
		Date effectiveDate2 = new GregorianCalendar(2021, 7, 5).getTime();
		payee2.setEffectiveDate(effectiveDate2);
		payee2.setReportGroup("28579");
		
		PBFClinicPayee payee3 = new PBFClinicPayee();
		payee3.setArchived(Boolean.FALSE);
		Date cancelDate3 = new GregorianCalendar(9999, 12, 31).getTime();
		payee3.setCancelDate(cancelDate3);
		payee3.setPayeeNumber("X0053");
		Date effectiveDate3 = new GregorianCalendar(2021, 7, 5).getTime();
		payee3.setEffectiveDate(effectiveDate3);
		payee3.setReportGroup("28579");

        PBFClinicPayee payee4 = new PBFClinicPayee();
        payee4.setArchived(Boolean.FALSE);
        Date cancelDate4 = new GregorianCalendar(2023, 01, 01).getTime();
        payee4.setCancelDate(cancelDate4);
        payee4.setPayeeNumber("00053");
        Date effectiveDate4 = new GregorianCalendar(2021, 7, 5).getTime();
        payee4.setEffectiveDate(effectiveDate4);
        payee4.setReportGroup("28579");

		pbfClinicPayeeRepository.save(payee);
		pbfClinicPayeeRepository.save(payee1);
		pbfClinicPayeeRepository.save(payee2);
		pbfClinicPayeeRepository.save(payee3);
        pbfClinicPayeeRepository.save(payee4);
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
		registry.add("hcim.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
	}

}
