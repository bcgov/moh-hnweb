package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityRequest;
import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.CheckMspCoverageStatusRequest;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnBeneficiary;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnRequest;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R15;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.uhn.hl7v2.HL7Exception;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest
public class EligibilityControllerTest {
	
	private static final String R42_SUCCESS = "RPBSPGW0RPBSPPL000000010HN000002                        RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  6803423266337109019123456789CHECKSETSNAME                      CHECSETFNAME   CHESERSNDNAME                 1970-01-01M                                                       ";

	private static final String R42_INVALID_GROUP_NUMBER = "RPBSPGW0RPBSPPL000000010HN000002                        ERRORMSGRPBS0041GROUP NUMBER INVALID                                                    6803423266337108";
	
	private static final String R42_INVALID_CONTRACT_NUMBER = "RPBSPGW0RPBSPPL000000010HN000002                        ERRORMSGRPBS0062CONTRACT NUMBER ENTERED IS INVALID                                      6703423266337109";
	
	private static final String R42_WARNING = "RPBSPGW0RPBSPPL000000010HN000002                        RESPONSERPBS0070MORE THAN 50 PERSONS. PLEASE CONTACT MSP.                               6110358416243109509332912486PROTOCTIST ORDERXD                 RYAN-CARTERXM  BRANDYNXD                     1956-08-25M9344911827PROTOCTIST ORDERXD                 GARNETXP       HARUNAOXJ                     2001-01-24F9306432103PROTOCTIST ORDERXD                 LASEYXH        KENDRENXD                     2001-02-06F9317310617PROTOCTIST ORDERXD                 MINGXQ         BENANXF                       2001-04-21F9378520641PROTOCTIST ORDERXD                 AYUUBXK        HEILYNXC                      2001-04-21M9397105575PROTOCTIST ORDERXD                 CADBYXL        BARKAATXB                     2001-07-07F9399900281PROTOCTIST ORDERXD                 BASHERXO       PARMAJITXC                    2002-06-19M9331926919PROTOCTIST ORDERXD                 JACK-LYALLXH   MO-CHARAXI                    2002-07-05F9340338122PROTOCTIST ORDERXD                 SCIPIOXH       WILFRIDXP                     2002-10-19M9329090895PROTOCTIST ORDERXD                 YARISXN        DILLINXP                      2002-11-04F9325719609PROTOCTIST ORDERXD                 JOHANNXB       SOURAVXL                      2002-11-25F9348175493PROTOCTIST ORDERXD                 SYCAMOREXC     SIMONIDESXD                   2002-12-23F9382771807PROTOCTIST ORDERXD                 HEINDRIKXI     KENETHXN                      2003-01-01F9303292543PROTOCTIST ORDERXD                 MUSSADAQXM     ORIANOXO                      2003-02-17F9329279733PROTOCTIST ORDERXD                 ALLEYNXO       IZAMXP                        2003-05-16M9307706834PROTOCTIST ORDERXD                 KEVENXG        DULLXJ                        2003-09-01M9318721811PROTOCTIST ORDERXD                 TENNYSONXF     CHUNXH                        2003-12-06M9874345713FEJO                               ERER                                         2004-01-01F9361354265PROTOCTIST ORDERXD                 DEDENISEOLUWAXAJYXD                          2004-01-25F9319815973PROTOCTIST ORDERXD                 EVYNXM         TOBIASXK                      2004-04-02F9332397268PROTOCTIST ORDERXD                 ADREENAXF      SUMANTHAXC                    2004-11-19F9386689958PROTOCTIST ORDERXD                 SOLASXF        WILLYXP                       2004-12-22F9362089793PROTOCTIST ORDERXD                 SANKARAXF      DAYLONXB                      2005-01-07M9318612745PROTOCTIST ORDERXD                 LUCKXM         SEBRINAXH                     2005-04-20F9318095634PROTOCTIST ORDERXD                 KEVINSXH       KORDELLXL                     2005-08-22F9328861469PROTOCTIST ORDERXD                 HADYXL         SIRACXD                       2005-09-11M9385445416PROTOCTIST ORDERXD                 JACQUELINEXB   TOMOSXM                       2006-02-02F9325033063PROTOCTIST ORDERXD                 MASAHIROXB     DIANXQ                        2006-05-10M9362985684PROTOCTIST ORDERXD                 SPARKLEXG      VINSONXF                      2007-01-10F9387987233PROTOCTIST ORDERXD                 ADALARDOXI     FINNIEXL                      2007-03-13M9330179682PROTOCTIST ORDERXD                 KAYMENXJ       BRANDYNXD                     2007-03-22M9361799467PROTOCTIST ORDERXD                 TCHUISSEUXB    DEAGTANXP                     2007-05-15M9342145297PROTOCTIST ORDERXD                 BASHAARXM      AISIAXK                       2007-06-08F9874346059ANOTHER CHILD                      FIRSTNAME                                    2008-01-01M9360350179PROTOCTIST ORDERXD                 WARLEYXJ       MOQSOODXM                     2009-01-10M9307927309PROTOCTIST ORDERXD                 KRISTAFERXG    HAITLEYXD                     2009-02-09M9323928739PROTOCTIST ORDERXD                 AKILANXO       HUDHAIFAXO                    2009-08-08M9383189035PROTOCTIST ORDERXD                 HAVELOCKXI     ULASXG                        2009-09-11M9310466174PROTOCTIST ORDERXD                 MOTAZXG        RISLEYXL                      2010-06-17F9336269991PROTOCTIST ORDERXD                 ETHENXQ        AYIDENXB                      2010-12-01F9329479845PROTOCTIST ORDERXD                 LAPXA          IZZABELLAXC                   2011-06-09F9399648695PROTOCTIST ORDERXD                 BOHEMONDXD     AIRDXH                        2012-03-04M9307498442PROTOCTIST ORDERXD                 JAMILAXE       TEANIEXB                      2012-03-05F9357569116PROTOCTIST ORDERXD                 AMANITXK       CLAUDIUXA                     2012-03-07F9337354556PROTOCTIST ORDERXD                 KAMXB          IMARAXJ                       2013-03-09M9359221805PROTOCTIST ORDERXD                 ZULIFKARXB     MAKSIMILJANXQ                 2014-04-07M9331126298PROTOCTIST ORDERXD                 ANDR'EXF       DEWEYXK                       2014-07-16F9304860417PROTOCTIST ORDERXD                 ERDEHANXL      ROLF-OLAVXH                   2014-08-24F9389431694PROTOCTIST ORDERXD                 RISLEYXL       MARTINUSXG                    2016-01-15F9349495455PROTOCTIST ORDERXD                 MARJUSXA       KORDELLXL                     2017-06-19F";

	@Autowired
	private EligibilityController eligibilityController;
	
	public static MockWebServer mockBackEnd;
	
	private static MockedStatic<SecurityUtil> mockStatic;
	
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
	
	/**
	 * TODO (weskubo-cgi) Update this test once downstream service is integrated using WebClient.
	 * @throws ParseException
	 */
	@Disabled
	@Test
	public void testCheckEligibility_success() throws ParseException {
		// 1. Set up our test data
		String phn = "9890608412";
		LocalDate eligibilityDate = LocalDate.now();
		String reason = "This is a mocked out response";
		
		// 2. Create a mock response object
		CheckEligibilityResponse mockResponse = new CheckEligibilityResponse();
		mockResponse.setPhn(phn);
		mockResponse.setCoverageEndReason("This is a mocked out response");
		
		R15 r15 = new R15();
		//r15.getPID().
		
		// 3. Return the mock response object when the service is called
		// Note, the parameters need to match what is sent
		//when(eligibilityServiceMock.checkEligibility(r15)).thenReturn(mockResponse);

		// 4. Perform assertions
		// These assertions aren't that interested since our service is returning the REST model directly
		// Once the service returns the HL7/Other business entity, the tests will be validating the conversion logic
		// in the controller
		CheckEligibilityRequest request = new CheckEligibilityRequest();
		request.setPhn(phn);
		request.setEligibilityDate(eligibilityDate);
	
		ResponseEntity<CheckEligibilityResponse> response = eligibilityController.checkEligibility(request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		CheckEligibilityResponse checkEligibilityResponse = response.getBody();
		assertEquals(phn, checkEligibilityResponse.getPhn());
		assertEquals(reason, checkEligibilityResponse.getCoverageEndReason());
	}
	
	@Disabled
	@Test
	public void testCheckEligibility_exception() throws ParseException, HL7Exception {
		// 1. Set up our test data
		String phn = "9890608411";
		LocalDate eligibilityDate = LocalDate.now();
		
		// 2. Create a mock response object
		CheckEligibilityResponse mockResponse = new CheckEligibilityResponse();
		mockResponse.setPhn(phn);
		mockResponse.setCoverageEndReason("This is a mocked out response");
		
		// 3. Return the mock response object when the service is called
		// Note, the parameters need to match what is sent
		// Force a failure on an invalid PHN
		R15 r15 = new R15();
		//when(eligibilityServiceMock.checkEligibility(r15)).thenThrow(new IllegalArgumentException());

		CheckEligibilityRequest request = new CheckEligibilityRequest();
		request.setPhn(phn);
		request.setEligibilityDate(eligibilityDate);
		
		// 4. Perform assertions
		ResponseStatusException responseException = assertThrows(ResponseStatusException.class, () -> {
        	eligibilityController.checkEligibility(request);
        });
		assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
		assertEquals("Bad /check-eligibility request", responseException.getReason());
	}

	@Test
	public void testCheckMspCoverageStatus_success() throws HL7Exception {

		CheckMspCoverageStatusRequest checkMspCoverageStatusRequest = createCheckMspCoverageStatusRequest("9873944324", LocalDate.of(1973, 8, 11), LocalDate.now(), true, false, null);
		
		//TODO (daveb-hni) The response is hard-coded currently so this this needs to be updated to Web Client test similar to EnrollmentControllerTest.testEnrollSubscriber_Error() once the endpoint connection is completed in EligibilityController.checkMspCoverageStatus 		
//		ResponseEntity<CheckMspCoverageStatusResponse> responseEntity = eligibilityController.checkMspCoverageStatus(checkMspCoverageStatusRequest);		
	}
	
	@Test
	public void testLookupPhn_success() {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R42_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LookupPhnRequest lookupPhnRequest = new LookupPhnRequest();
        lookupPhnRequest.setContractNumber("123456789");
        lookupPhnRequest.setGroupNumber("1234567");
        
        ResponseEntity<LookupPhnResponse> response = eligibilityController.lookupPhn(lookupPhnRequest);
        
        LookupPhnResponse lookupPhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.SUCCESS, lookupPhnResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", lookupPhnResponse.getMessage());
		
		// Check the client request is sent as expected
        List<LookupPhnBeneficiary> beneficiaries = lookupPhnResponse.getBeneficiaries();
        assertEquals(1, beneficiaries.size());
        
        LookupPhnBeneficiary beneficiary = beneficiaries.get(0);
        
        assertEquals("9123456789", beneficiary.getPhn());
        assertEquals("CHECKSETSNAME", beneficiary.getLastName());
        assertEquals("CHECSETFNAME", beneficiary.getFirstName());
        assertEquals("CHESERSNDNAME", beneficiary.getSecondName());
        assertEquals("19700101", beneficiary.getDateOfBirth());
        assertEquals("M", beneficiary.getGender());
	}

	@Test
	public void testLookupPhn_warning() {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R42_WARNING)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LookupPhnRequest lookupPhnRequest = new LookupPhnRequest();
        lookupPhnRequest.setContractNumber("123456789");
        lookupPhnRequest.setGroupNumber("1234567");
        
        ResponseEntity<LookupPhnResponse> response = eligibilityController.lookupPhn(lookupPhnRequest);
        
        LookupPhnResponse lookupPhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.WARNING, lookupPhnResponse.getStatus());
        assertEquals("RPBS0070 MORE THAN 50 PERSONS. PLEASE CONTACT MSP.", lookupPhnResponse.getMessage());
		
		// Check the client request is sent as expected
        List<LookupPhnBeneficiary> beneficiaries = lookupPhnResponse.getBeneficiaries();
        assertEquals(50, beneficiaries.size());
        
        LookupPhnBeneficiary beneficiary = beneficiaries.get(0);
        
        assertEquals("9332912486", beneficiary.getPhn());
        assertEquals("PROTOCTIST ORDERXD", beneficiary.getLastName());
        assertEquals("RYAN-CARTERXM", beneficiary.getFirstName());
        assertEquals("BRANDYNXD", beneficiary.getSecondName());
        assertEquals("19560825", beneficiary.getDateOfBirth());
        assertEquals("M", beneficiary.getGender());
	}
	
	@Test
	public void testLookupPhn_error_invalidContractNumber() {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R42_INVALID_CONTRACT_NUMBER)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LookupPhnRequest lookupPhnRequest = new LookupPhnRequest();
        lookupPhnRequest.setContractNumber("123456789");
        lookupPhnRequest.setGroupNumber("1234567");
        
        ResponseEntity<LookupPhnResponse> response = eligibilityController.lookupPhn(lookupPhnRequest);
        
        LookupPhnResponse lookupPhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, lookupPhnResponse.getStatus());
        assertEquals("RPBS0062 CONTRACT NUMBER ENTERED IS INVALID", lookupPhnResponse.getMessage());
		
		// Check the client request is sent as expected
        List<LookupPhnBeneficiary> beneficiaries = lookupPhnResponse.getBeneficiaries();
        assertEquals(0, beneficiaries.size());
	}
	
	@Test
	public void testLookupPhn_error_invalidGroupNumber() {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R42_INVALID_GROUP_NUMBER)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LookupPhnRequest lookupPhnRequest = new LookupPhnRequest();
        lookupPhnRequest.setContractNumber("123456789");
        lookupPhnRequest.setGroupNumber("1234567");
        
        ResponseEntity<LookupPhnResponse> response = eligibilityController.lookupPhn(lookupPhnRequest);
        
        LookupPhnResponse lookupPhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, lookupPhnResponse.getStatus());
        assertEquals("RPBS0041 GROUP NUMBER INVALID", lookupPhnResponse.getMessage());
		
		// Check the client request is sent as expected
        List<LookupPhnBeneficiary> beneficiaries = lookupPhnResponse.getBeneficiaries();
        assertEquals(0, beneficiaries.size());
	}

    /**
     * The URL property used by the mocked endpoint needs to be set after the MockWebServer starts as the port it uses is 
     * created dynamically on start up to ensure it uses an available port so it is not known before then. 
     * @param registry
     */
    @DynamicPropertySource
    static void registerMockUrlProperty(DynamicPropertyRegistry registry) {
        registry.add("rapid.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
        registry.add("rapid.r42Path", () -> "/");
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
