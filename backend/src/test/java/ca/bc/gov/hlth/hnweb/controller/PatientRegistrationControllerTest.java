package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.PatientRegisterModel;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.ViewPatientRegistrationRequest;
import ca.bc.gov.hlth.hnweb.model.rest.patientregistration.ViewPatientRegistrationResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PBFClinicPayee;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PBFClinicPayeeRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PatientRegisterRepository;
import ca.bc.gov.hlth.hnweb.utils.TestUtil;
import okhttp3.mockwebserver.MockResponse;

/**
 * JUnit test class for PBFController
 *
 */
public class PatientRegistrationControllerTest extends BaseControllerTest {
	
	@Autowired
	private PatientRegistrationController pbfController;

	@Autowired
	private PatientRegisterRepository patientRegisterRepository;

	@Autowired
	private PBFClinicPayeeRepository pbfClinicPayeeRepository;
   
    @Test
	public void testRegistrationHistory() throws Exception {
		createPBFClinicPayee();
		createPatientRegister();

		mockBackEnd.enqueue(new MockResponse()
				.setBody(TestUtil.convertXMLFileToString("src/test/resources/GetDemographicsResponse_Error.xml"))
				.addHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE.toString()));

		ViewPatientRegistrationRequest viewPatientRegisterRequest = new ViewPatientRegistrationRequest();
		viewPatientRegisterRequest.setPhn("9879869673");
		viewPatientRegisterRequest.setPayee("A0053");
		ResponseEntity<ViewPatientRegistrationResponse> response = pbfController
				.getPatientRegistrationHistory(viewPatientRegisterRequest, createHttpServletRequest());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		ViewPatientRegistrationResponse patientRegistrationResponse = response.getBody();
		List<PatientRegisterModel> patientRegistrationHistory = patientRegistrationResponse.getPatientRegistrationHistory();
		// Check the number of valid records
		System.out.println(patientRegistrationResponse.getAdditionalInfoMessage());
		System.out.println(patientRegistrationResponse.getMessage());
		assertEquals(1, patientRegistrationHistory.size());
	}

	private void createPatientRegister() {

		PatientRegister patientRegister1 = new PatientRegister();
		patientRegister1.setAdministrativeCode("0");
		Date effectiveDate1 = new GregorianCalendar(2021, 7, 5).getTime();
		patientRegister1.setEffectiveDate(effectiveDate1);
		Date cancelDate1 = new GregorianCalendar(9999, 12, 31).getTime();
		patientRegister1.setCancelDate(cancelDate1);
		patientRegister1.setRegistrationReasonCode("SL");
		patientRegister1.setPayeeNumber("A0055");
		patientRegister1.setRegisteredPractitionerNumber("X2753");
		patientRegister1.setArchived(Boolean.FALSE);
		patientRegister1.setPhn("9879869673");

		patientRegisterRepository.save(patientRegister1);

		PatientRegister patientRegister2 = new PatientRegister();
		patientRegister2.setAdministrativeCode("0");
		Date effectiveDate2 = new GregorianCalendar(2021, 7, 5).getTime();
		patientRegister2.setEffectiveDate(effectiveDate2);
		Date cancelDate2 = new GregorianCalendar(9999, 12, 31).getTime();
		patientRegister2.setCancelDate(cancelDate2);
		patientRegister2.setRegistrationReasonCode("SL");
		patientRegister2.setPayeeNumber("A0053");
		patientRegister2.setRegisteredPractitionerNumber("X2753");
		patientRegister2.setArchived(Boolean.FALSE);
		patientRegister2.setPhn("8108641380");

		patientRegisterRepository.save(patientRegister2);
		
		List<PatientRegister> findAll = patientRegisterRepository.findAll();
		System.out.println(findAll.size());

	}

	private void createPBFClinicPayee() {
		PBFClinicPayee payee = new PBFClinicPayee();
		payee.setArchived(Boolean.FALSE);
		Date cancelDate = new GregorianCalendar(9999, 12, 31).getTime();
		payee.setCancelDate(cancelDate);
		payee.setPayeeNumber("A0053");
		Date effectiveDate = new GregorianCalendar(2021, 7, 5).getTime();
		payee.setEffectiveDate(effectiveDate);
		payee.setReportGroup("18579");

		pbfClinicPayeeRepository.save(payee);
		
		PBFClinicPayee payee1 = new PBFClinicPayee();
		payee1.setArchived(Boolean.FALSE);
		Date cancelDate1 = new GregorianCalendar(9999, 12, 31).getTime();
		payee1.setCancelDate(cancelDate1);
		payee1.setPayeeNumber("A0053");
		Date effectiveDate1 = new GregorianCalendar(2021, 7, 5).getTime();
		payee1.setEffectiveDate(effectiveDate1);
		payee1.setReportGroup("18579");
		
		PBFClinicPayee payee2 = new PBFClinicPayee();
		payee1.setArchived(Boolean.FALSE);
		Date cancelDate2 = new GregorianCalendar(9999, 12, 31).getTime();
		payee1.setCancelDate(cancelDate2);
		payee1.setPayeeNumber("A0055");
		Date effectiveDate2 = new GregorianCalendar(2021, 7, 5).getTime();
		payee2.setEffectiveDate(effectiveDate2);
		payee2.setReportGroup("18579");

		pbfClinicPayeeRepository.save(payee1);
		List<PBFClinicPayee> findAll = pbfClinicPayeeRepository.findAll();
		System.out.println(findAll.size());

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

}
