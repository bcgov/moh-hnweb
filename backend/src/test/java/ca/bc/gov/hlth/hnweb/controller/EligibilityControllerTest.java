package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.CheckEligibilityRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.CheckMspCoverageStatusRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.CheckMspCoverageStatusResponse;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.InquirePhnBeneficiary;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.InquirePhnRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.InquirePhnResponse;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.LookupPhnBeneficiary;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.LookupPhnRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.LookupPhnResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEvent;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;
import ca.bc.gov.hlth.hnweb.persistence.repository.AffectedPartyRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.TransactionEventRepository;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class EligibilityControllerTest extends BaseControllerTest {
	
	private static final String R41_SUCCESS = "RPBSPGW0RPBSPPE000006412HN000002                        RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  9123456789                                                                                          2021-06-229123456789GFMSRHNISNAME                      GERHNIMODFNAME                               1970-09-09MRPBS9014TRANSACTION SUCCESSFUL                                                  Y                                                                                          N";
	
	private static final String R41_ERROR_NO_ELIGIBILITY_DATE = "RPBSPGW0RPBSPPE000006412HN000002                        ERRORMSGRPBS0057ELIGIBILITY DATE REQUIRED                                               9123456789           ";
	
	private static final String R41_WARNING_PHN_INVALID = "RPBSPGW0RPBSPPE000006412HN000002                        RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  9123456789                                                                                          2021-06-229123456789                                                                                           RPBS0073PHN INVALID";
	
	private static final String R41_WARNING_NOT_FOUND = "RPBSPGW0RPBSPPE000006412HN000002                        RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  91231231239456456456                                                                                2021-06-229123123123                                                                                           RPBS9145PHN NOT FOUND                                                                                                                                                       9456456456SMITH                              JENNIFER       JENNY                         2000-01-01FRPBS9014TRANSACTION SUCCESSFUL                                                  N                                                                                          N                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ";
	
	private static final String R42_SUCCESS = "RPBSPGW0RPBSPPL000000010HN000002                        RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  6803423266337109019123456789CHECKSETSNAME                      CHECSETFNAME   CHESERSNDNAME                 1970-01-01M                                                       ";

	private static final String R42_INVALID_GROUP_NUMBER = "RPBSPGW0RPBSPPL000000010HN000002                        ERRORMSGRPBS0041GROUP NUMBER INVALID                                                    6803423266337108";
	
	private static final String R42_INVALID_CONTRACT_NUMBER = "RPBSPGW0RPBSPPL000000010HN000002                        ERRORMSGRPBS0062CONTRACT NUMBER ENTERED IS INVALID                                      6703423266337109";
	
	private static final String R42_WARNING = "RPBSPGW0RPBSPPL000000010HN000002                        RESPONSERPBS0070MORE THAN 50 PERSONS. PLEASE CONTACT MSP.                               6110358416243109509332912486PROTOCTIST ORDERXD                 RYAN-CARTERXM  BRANDYNXD                     1956-08-25M9344911827PROTOCTIST ORDERXD                 GARNETXP       HARUNAOXJ                     2001-01-24F9306432103PROTOCTIST ORDERXD                 LASEYXH        KENDRENXD                     2001-02-06F9317310617PROTOCTIST ORDERXD                 MINGXQ         BENANXF                       2001-04-21F9378520641PROTOCTIST ORDERXD                 AYUUBXK        HEILYNXC                      2001-04-21M9397105575PROTOCTIST ORDERXD                 CADBYXL        BARKAATXB                     2001-07-07F9399900281PROTOCTIST ORDERXD                 BASHERXO       PARMAJITXC                    2002-06-19M9331926919PROTOCTIST ORDERXD                 JACK-LYALLXH   MO-CHARAXI                    2002-07-05F9340338122PROTOCTIST ORDERXD                 SCIPIOXH       WILFRIDXP                     2002-10-19M9329090895PROTOCTIST ORDERXD                 YARISXN        DILLINXP                      2002-11-04F9325719609PROTOCTIST ORDERXD                 JOHANNXB       SOURAVXL                      2002-11-25F9348175493PROTOCTIST ORDERXD                 SYCAMOREXC     SIMONIDESXD                   2002-12-23F9382771807PROTOCTIST ORDERXD                 HEINDRIKXI     KENETHXN                      2003-01-01F9303292543PROTOCTIST ORDERXD                 MUSSADAQXM     ORIANOXO                      2003-02-17F9329279733PROTOCTIST ORDERXD                 ALLEYNXO       IZAMXP                        2003-05-16M9307706834PROTOCTIST ORDERXD                 KEVENXG        DULLXJ                        2003-09-01M9318721811PROTOCTIST ORDERXD                 TENNYSONXF     CHUNXH                        2003-12-06M9874345713FEJO                               ERER                                         2004-01-01F9361354265PROTOCTIST ORDERXD                 DEDENISEOLUWAXAJYXD                          2004-01-25F9319815973PROTOCTIST ORDERXD                 EVYNXM         TOBIASXK                      2004-04-02F9332397268PROTOCTIST ORDERXD                 ADREENAXF      SUMANTHAXC                    2004-11-19F9386689958PROTOCTIST ORDERXD                 SOLASXF        WILLYXP                       2004-12-22F9362089793PROTOCTIST ORDERXD                 SANKARAXF      DAYLONXB                      2005-01-07M9318612745PROTOCTIST ORDERXD                 LUCKXM         SEBRINAXH                     2005-04-20F9318095634PROTOCTIST ORDERXD                 KEVINSXH       KORDELLXL                     2005-08-22F9328861469PROTOCTIST ORDERXD                 HADYXL         SIRACXD                       2005-09-11M9385445416PROTOCTIST ORDERXD                 JACQUELINEXB   TOMOSXM                       2006-02-02F9325033063PROTOCTIST ORDERXD                 MASAHIROXB     DIANXQ                        2006-05-10M9362985684PROTOCTIST ORDERXD                 SPARKLEXG      VINSONXF                      2007-01-10F9387987233PROTOCTIST ORDERXD                 ADALARDOXI     FINNIEXL                      2007-03-13M9330179682PROTOCTIST ORDERXD                 KAYMENXJ       BRANDYNXD                     2007-03-22M9361799467PROTOCTIST ORDERXD                 TCHUISSEUXB    DEAGTANXP                     2007-05-15M9342145297PROTOCTIST ORDERXD                 BASHAARXM      AISIAXK                       2007-06-08F9874346059ANOTHER CHILD                      FIRSTNAME                                    2008-01-01M9360350179PROTOCTIST ORDERXD                 WARLEYXJ       MOQSOODXM                     2009-01-10M9307927309PROTOCTIST ORDERXD                 KRISTAFERXG    HAITLEYXD                     2009-02-09M9323928739PROTOCTIST ORDERXD                 AKILANXO       HUDHAIFAXO                    2009-08-08M9383189035PROTOCTIST ORDERXD                 HAVELOCKXI     ULASXG                        2009-09-11M9310466174PROTOCTIST ORDERXD                 MOTAZXG        RISLEYXL                      2010-06-17F9336269991PROTOCTIST ORDERXD                 ETHENXQ        AYIDENXB                      2010-12-01F9329479845PROTOCTIST ORDERXD                 LAPXA          IZZABELLAXC                   2011-06-09F9399648695PROTOCTIST ORDERXD                 BOHEMONDXD     AIRDXH                        2012-03-04M9307498442PROTOCTIST ORDERXD                 JAMILAXE       TEANIEXB                      2012-03-05F9357569116PROTOCTIST ORDERXD                 AMANITXK       CLAUDIUXA                     2012-03-07F9337354556PROTOCTIST ORDERXD                 KAMXB          IMARAXJ                       2013-03-09M9359221805PROTOCTIST ORDERXD                 ZULIFKARXB     MAKSIMILJANXQ                 2014-04-07M9331126298PROTOCTIST ORDERXD                 ANDR'EXF       DEWEYXK                       2014-07-16F9304860417PROTOCTIST ORDERXD                 ERDEHANXL      ROLF-OLAVXH                   2014-08-24F9389431694PROTOCTIST ORDERXD                 RISLEYXL       MARTINUSXG                    2016-01-15F9349495455PROTOCTIST ORDERXD                 MARJUSXA       KORDELLXL                     2017-06-19F";

	private static final String E45_SUCCESS_INELIGIBLE = "MSH|^~\\&|RAIENROL-EMP|BC00001013|HNWeb|HN-WEB|20211208193349|wkubo@idir|E45|20211207153749|P|2.4\r\n"
			+ "MSA|AA|20211207153749|HJMB001ISUCCESSFULLY COMPLETED\r\n"
			+ "ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n"
			+ "QAK|1|AA|E45^^HNET0003\r\n"
			+ "QPD|E45^^HNET0003|1|^^00000010^^^CANBC^XX^MOH|^^00000010^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9395568139^^^CANBC^JHN^MOH||19710919||||||20211207||ENDRSN^^HNET9909~CCARD^^HNET9909~PVC^^HNET9909~EYE^^HNET9909~PRS^^HNET9909\r\n"
			+ "PID|||9395568139||PORTFOLIOXE^ROISHANNXD^ECKAXK^^^^L||19710919|F\r\n"
			+ "IN1|||00000754^^^CANBC^XX^MOH||||||||||20190731||||||||||||N\r\n"
			+ "ADJ|1|IN|||ENDRSN^^HNET9908|OOPM\r\n"
			+ "ADJ|2|IN|||CINST^^HNET9908|MSP'S RECORDS INDICATE THAT THIS PERSON HAS MOVED PERMANENTLY FROM BC. PLEASE CONFIRM RESIDENCE, OBTAIN AND UPDATE ADDRESS AND TELEPHONE INFORMATION AND ADVISE PERSON TO CONTACT MSP TO RE-ESTABLISH ELIGIBILITY.\r\n";
	
	private static final String E45_ERROR_DOB_MISMATCH = "MSH|^~\\&|RAIENROL-EMP|BC00001013|HNWeb|HN-WEB|20211209132747|wkubo@idir|E45|20211207153749|P|2.4\r\n"
			+ "MSA|AE|20211207153749|HJMB140EBIRTHDATES DO NOT MATCH\r\n"
			+ "ERR|^^^HJMB140E&BIRTHDATES DO NOT MATCH\r\n"
			+ "QAK|1|AE|E45^^HNET0003\r\n"
			+ "QPD|E45^^HNET0003|1|^^00000010^^^CANBC^XX^MOH|^^00000010^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9395568139^^^CANBC^JHN^MOH||19810919||||||20211207||ENDRSN^^HNET9909~CCARD^^HNET9909~PVC^^HNET9909~EYE^^HNET9909~PRS^^HNET9909\r\n"
			+ "PID|||9395568139||^^^^^^L||19810919|\r\n"
			+ "IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||\r\n"
			+ "";
	
	private static final String E45_ERROR_PHN_INVALID = "MSH|^~\\&|RAIENROL-EMP|BC00001013|HNWeb|HN-WEB|20211209132225|wkubo@idir|E45|20211207153749|P|2.4\r\n"
			+ "MSA|AE|20211207153749|HRPB059EPHN INVALID.\r\n"
			+ "ERR|^^^HRPB059E&PHN INVALID.\r\n"
			+ "QAK|1|AE|E45^^HNET0003\r\n"
			+ "QPD|E45^^HNET0003|1|^^00000010^^^CANBC^XX^MOH|^^00000010^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9123456789^^^CANBC^JHN^MOH||19710919||||||20211207||ENDRSN^^HNET9909~CCARD^^HNET9909~PVC^^HNET9909~EYE^^HNET9909~PRS^^HNET9909\r\n"
			+ "PID|||9123456789||^^^^^^L||19710919|\r\n"
			+ "IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||";
	
	private static final String E45_SUCCESS_ELIGIBLE = "MSH|^~\\&|RAIELG-CNFRM|BC00002041|TTUWEB|BC01000166|20211209134740|TESTUSER|E45|20080915133999|D|2.4\r\n"
			+ "MSA|AA|20080915133999|HJMB001ISUCCESSFULLY COMPLETED\r\n"
			+ "ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n"
			+ "QAK|1|AA|E45^^HNET0471\r\n"
			+ "QPD|E45^^HNET0471|1|^^00001424^^^CANBC^XX^MOH|^^00001424^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9347984074^^^CANBC^JHN^MOH||19850915||||||20210421||PVC^^HNET9909~EYE^^HNET9909~PRS^^HNET9909\r\n"
			+ "PID|||9347984074||GENUS ACRIDOTHERESXC^MOHAMMED-ALIMXB^ANJUMXI^^^^L||19850915|M\r\n"
			+ "IN1|||00000754^^^CANBC^XX^MOH||||||||||||||||||||||Y\r\n"
			+ "ADJ|1|IN|||PVC^^HNET9908|N\r\n"
			+ "ADJ|2|IN|||EYE^^HNET9908\r\n"
			+ "ADJ|3|IN|||PRS^^HNET9908|N";
	
	private static final String R15_SUCCESS_ELIGIBLE = "MSH|^~\\&|RAICHK-BNF-CVST|BC00004000|ADT1|BC01400020|20211210185941|SSOMAX|R15|20200128094905|P|2.3||\r\n"
			+ "MSA|AA|20200128094905|HJMB001ISUCCESSFULLY COMPLETED\r\n"
			+ "ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n"
			+ "ZTL|1^RD\r\n"
			+ "IN1|1||||||||||||||||||||||||Y\r\n"
			+ "ZIH||||||||||||||||||1";
	
	private static final String R15_SUCCESS_INELIGIBLE = "MSH|^~\\&|RAICHK-BNF-CVST|BC00004000|ADT1|BC01400020|20211210190504|SSOMAX|R15|20200421094901|P|2.3||\r\n"
			+ "MSA|AA|20200421094901|HJMB001ISUCCESSFULLY COMPLETED\r\n"
			+ "ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n"
			+ "ZTL|1^RD\r\n"
			+ "IN1|1||||||||||||20190731||||||||||||N\r\n"
			+ "ZIH|||||||||||||||OOPM||MSP'S RECORDS INDICATE THAT THIS PERSON HAS MOVED PERMANENTLY FROM BC. PLEASE CONFIRM RESIDENCE, OBTAIN AND UPDATE ADDRESS AND TELEPHONE INFORMATION AND ADVISE PERSON TO CONTACT MSP TO RE-ESTABLISH ELIGIBILITY.|1";

	private static final String R15_ERROR_PHN_INVALID = "MSH|^~\\&|RAICHK-BNF-CVST|BC00004000|ADT1|BC01400020|20211213155557|SSOMAX|R15|20200421094902|P|2.3||\r\n"
			+ "MSA|AE|20200421094902|HRPB059EPHN INVALID.\r\n"
			+ "ERR|^^^HRPB059E&PHN INVALID.\r\n"
			+ "ZTL|1^RD\r\n"
			+ "IN1|1||||||||||||||||||||||||\r\n"
			+ "ZIH||||||||||||||||||1";
	
	protected static DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
	
	@Autowired
	private EligibilityController eligibilityController;

	@Autowired
	private AffectedPartyRepository affectedPartyRepository;
	
	@Autowired
	private TransactionEventRepository transactionEventRepository;
   
	@Test
	public void testCheckEligibility_success_eligible() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R15_SUCCESS_ELIGIBLE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		CheckEligibilityRequest checkEligibilityRequest = new CheckEligibilityRequest();
		checkEligibilityRequest.setEligibilityDate(LocalDate.now());
		checkEligibilityRequest.setPhn("9347984074");
		
		ResponseEntity<CheckEligibilityResponse> response = eligibilityController.checkEligibility(checkEligibilityRequest, createHttpServletRequest());
		
		CheckEligibilityResponse checkEligibilityResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, checkEligibilityResponse.getStatus());
        assertEquals("SUCCESSFULLY COMPLETED", checkEligibilityResponse.getMessage());

        assertEquals("9347984074", checkEligibilityResponse.getPhn());
        assertEquals("Y", checkEligibilityResponse.getBeneficiaryOnDateChecked());
        assertNull(checkEligibilityResponse.getCoverageEndDate());
        assertNull(checkEligibilityResponse.getCoverageEndReason());
        assertNull(checkEligibilityResponse.getExclusionPeriodEndDate());
        assertNull(checkEligibilityResponse.getClientInstructions());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHECK_ELIGIBILITY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testCheckEligibility_success_ineligible() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R15_SUCCESS_INELIGIBLE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		CheckEligibilityRequest checkEligibilityRequest = new CheckEligibilityRequest();
		checkEligibilityRequest.setEligibilityDate(LocalDate.now());
		checkEligibilityRequest.setPhn("9347984074");
		
		ResponseEntity<CheckEligibilityResponse> response = eligibilityController.checkEligibility(checkEligibilityRequest, createHttpServletRequest());
		
		CheckEligibilityResponse checkEligibilityResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, checkEligibilityResponse.getStatus());
        assertEquals("SUCCESSFULLY COMPLETED", checkEligibilityResponse.getMessage());

        assertEquals("9347984074", checkEligibilityResponse.getPhn());
        assertEquals("N", checkEligibilityResponse.getBeneficiaryOnDateChecked());
        assertEquals("20190731", checkEligibilityResponse.getCoverageEndDate());
        assertEquals("OOPM",checkEligibilityResponse.getCoverageEndReason());
        assertNull(checkEligibilityResponse.getExclusionPeriodEndDate());
        assertEquals("MSP'S RECORDS INDICATE THAT THIS PERSON HAS MOVED PERMANENTLY FROM BC. PLEASE CONFIRM RESIDENCE, OBTAIN AND UPDATE ADDRESS AND TELEPHONE INFORMATION AND ADVISE PERSON TO CONTACT MSP TO RE-ESTABLISH ELIGIBILITY.", checkEligibilityResponse.getClientInstructions());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
      
        assertTransactionCreated(TransactionType.CHECK_ELIGIBILITY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}

	@Test
	public void testCheckEligibility_error_phnInvalid() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R15_ERROR_PHN_INVALID)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));

		CheckEligibilityRequest checkEligibilityRequest = new CheckEligibilityRequest();
		checkEligibilityRequest.setEligibilityDate(LocalDate.now());
		checkEligibilityRequest.setPhn("9123456789");
		
		ResponseEntity<CheckEligibilityResponse> response = eligibilityController.checkEligibility(checkEligibilityRequest, createHttpServletRequest());
		
		CheckEligibilityResponse checkEligibilityResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, checkEligibilityResponse.getStatus());
        assertEquals("PHN INVALID.", checkEligibilityResponse.getMessage());

        assertEquals("9123456789", checkEligibilityResponse.getPhn());
        assertNull(checkEligibilityResponse.getBeneficiaryOnDateChecked());
        assertNull(checkEligibilityResponse.getCoverageEndDate());
        assertNull(checkEligibilityResponse.getCoverageEndReason());
        assertNull(checkEligibilityResponse.getExclusionPeriodEndDate());
        assertNull(checkEligibilityResponse.getClientInstructions());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CHECK_ELIGIBILITY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}

	@Test
	public void testCheckMspCoverageStatus_success_ineligible() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(E45_SUCCESS_INELIGIBLE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
		
        LocalDate serviceDate = LocalDate.of(2021, 12, 01);
		CheckMspCoverageStatusRequest checkMspCoverageStatusRequest = createCheckMspCoverageStatusRequest("9395568139", LocalDate.of(1971, 9, 19), serviceDate, true, true, true);
		
		ResponseEntity<CheckMspCoverageStatusResponse> response = eligibilityController.checkMspCoverageStatus(checkMspCoverageStatusRequest, createHttpServletRequest());
		
		CheckMspCoverageStatusResponse checkCoverageResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, checkCoverageResponse.getStatus());
        assertEquals("SUCCESSFULLY COMPLETED", checkCoverageResponse.getMessage());
        assertEquals("9395568139", checkCoverageResponse.getPhn());
        assertEquals(dateOnlyFormatter.format(serviceDate), checkCoverageResponse.getDateOfService());
        assertEquals("20190731", checkCoverageResponse.getCoverageEndDate());
        assertEquals("OOPM", checkCoverageResponse.getCoverageEndReason());
        assertEquals("N", checkCoverageResponse.getEligibleOnDateOfService());
        assertEquals("PORTFOLIOXE", checkCoverageResponse.getSurname());
        assertEquals("ROISHANNXD", checkCoverageResponse.getGivenName());
        assertEquals("ECKAXK", checkCoverageResponse.getSecondName());
        assertEquals("F", checkCoverageResponse.getGender());
        assertEquals("MSP'S RECORDS INDICATE THAT THIS PERSON HAS MOVED PERMANENTLY FROM BC. PLEASE CONFIRM RESIDENCE, OBTAIN AND UPDATE ADDRESS AND TELEPHONE INFORMATION AND ADVISE PERSON TO CONTACT MSP TO RE-ESTABLISH ELIGIBILITY.", checkCoverageResponse.getClientInstructions());
        
        // The Patient Status Request fields are not populated for an ineligible record, even if requested
        assertNull(checkCoverageResponse.getSubsidyInsuredService());
        assertNull(checkCoverageResponse.getDateOfLastEyeExamination());
        assertNull(checkCoverageResponse.getPatientRestriction());
        assertNull(checkCoverageResponse.getCareCardWarning());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));		
        
        assertTransactionCreated(TransactionType.MSP_COVERAGE_STATUS_CHECK);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testCheckMspCoverageStatus_success_eligible() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(E45_SUCCESS_ELIGIBLE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
		
        LocalDate serviceDate = LocalDate.of(2021, 12, 01);
		CheckMspCoverageStatusRequest checkMspCoverageStatusRequest = createCheckMspCoverageStatusRequest("9347984074", LocalDate.of(1985, 9, 15), serviceDate, true, true, true);
		
		ResponseEntity<CheckMspCoverageStatusResponse> response = eligibilityController.checkMspCoverageStatus(checkMspCoverageStatusRequest, createHttpServletRequest());
		
		CheckMspCoverageStatusResponse checkCoverageResponse = response.getBody();
		assertEquals(StatusEnum.SUCCESS, checkCoverageResponse.getStatus());
        assertEquals("SUCCESSFULLY COMPLETED", checkCoverageResponse.getMessage());

        assertEquals("9347984074", checkCoverageResponse.getPhn());
        assertEquals(dateOnlyFormatter.format(serviceDate), checkCoverageResponse.getDateOfService());
        assertNull(checkCoverageResponse.getCoverageEndDate());
        assertNull(checkCoverageResponse.getCoverageEndReason());
        assertEquals("Y", checkCoverageResponse.getEligibleOnDateOfService());
        assertEquals("GENUS ACRIDOTHERESXC", checkCoverageResponse.getSurname());
        assertEquals("MOHAMMED-ALIMXB", checkCoverageResponse.getGivenName());
        assertEquals("ANJUMXI", checkCoverageResponse.getSecondName());
        assertEquals("M", checkCoverageResponse.getGender());
        assertNull(checkCoverageResponse.getClientInstructions());
        
        // The Patient Status Request fields are not populated for an ineligible record, even if requested
        assertEquals("N", checkCoverageResponse.getSubsidyInsuredService());
        assertEquals("N", checkCoverageResponse.getDateOfLastEyeExamination());
        assertEquals("N", checkCoverageResponse.getPatientRestriction());
        assertNull(checkCoverageResponse.getCareCardWarning());
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.MSP_COVERAGE_STATUS_CHECK);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testCheckMspCoverageStatus_error_phnInvalid() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(E45_ERROR_PHN_INVALID)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
		
        LocalDate serviceDate = LocalDate.of(2021, 12, 01);
		CheckMspCoverageStatusRequest checkMspCoverageStatusRequest = createCheckMspCoverageStatusRequest("9123456789", LocalDate.of(1971, 9, 19), LocalDate.of(2021, 12, 01), true, true, true);
		
		ResponseEntity<CheckMspCoverageStatusResponse> response = eligibilityController.checkMspCoverageStatus(checkMspCoverageStatusRequest, createHttpServletRequest());
		
		CheckMspCoverageStatusResponse checkCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, checkCoverageResponse.getStatus());
        assertEquals("PHN INVALID.", checkCoverageResponse.getMessage());

        assertEquals("9123456789", checkCoverageResponse.getPhn());
        assertEquals(dateOnlyFormatter.format(serviceDate), checkCoverageResponse.getDateOfService());
        assertNull(checkCoverageResponse.getCoverageEndDate());
        assertNull(checkCoverageResponse.getCoverageEndReason());
        assertNull(checkCoverageResponse.getEligibleOnDateOfService());
        assertNull(checkCoverageResponse.getGender());
        assertNull(checkCoverageResponse.getGivenName());
        assertNull(checkCoverageResponse.getSecondName());
        assertNull(checkCoverageResponse.getSurname());
        assertNull(checkCoverageResponse.getClientInstructions());
        assertNull(checkCoverageResponse.getSubsidyInsuredService());
        assertNull(checkCoverageResponse.getDateOfLastEyeExamination());
        assertNull(checkCoverageResponse.getPatientRestriction());
        assertNull(checkCoverageResponse.getCareCardWarning());

		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.MSP_COVERAGE_STATUS_CHECK);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testCheckMspCoverageStatus_error_dateOfBirthMismatch() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(E45_ERROR_DOB_MISMATCH)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LocalDate serviceDate = LocalDate.of(2021, 12, 01);
		CheckMspCoverageStatusRequest checkMspCoverageStatusRequest = createCheckMspCoverageStatusRequest("9395568139", LocalDate.of(1981, 9, 19),  LocalDate.of(2021, 12, 01), true, true, true);
		
		ResponseEntity<CheckMspCoverageStatusResponse> response = eligibilityController.checkMspCoverageStatus(checkMspCoverageStatusRequest, createHttpServletRequest());
		
		CheckMspCoverageStatusResponse checkCoverageResponse = response.getBody();
		assertEquals(StatusEnum.ERROR, checkCoverageResponse.getStatus());
        assertEquals("BIRTHDATES DO NOT MATCH", checkCoverageResponse.getMessage());

        assertEquals("9395568139", checkCoverageResponse.getPhn());
        assertEquals(dateOnlyFormatter.format(serviceDate), checkCoverageResponse.getDateOfService());
        assertNull(checkCoverageResponse.getCoverageEndDate());
        assertNull(checkCoverageResponse.getCoverageEndReason());
        assertNull(checkCoverageResponse.getEligibleOnDateOfService());
        assertNull(checkCoverageResponse.getGender());
        assertNull(checkCoverageResponse.getGivenName());
        assertNull(checkCoverageResponse.getSecondName());
        assertNull(checkCoverageResponse.getSurname());
        assertNull(checkCoverageResponse.getClientInstructions());
        assertNull(checkCoverageResponse.getSubsidyInsuredService());
        assertNull(checkCoverageResponse.getDateOfLastEyeExamination());
        assertNull(checkCoverageResponse.getPatientRestriction());
        assertNull(checkCoverageResponse.getCareCardWarning());

		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));	
        
        assertTransactionCreated(TransactionType.MSP_COVERAGE_STATUS_CHECK);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testInquirePhn_success() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R41_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        InquirePhnRequest inquirePhnRequest = new InquirePhnRequest();
        inquirePhnRequest.getPhns().add("9123456789");
        
        ResponseEntity<InquirePhnResponse> response = eligibilityController.inquirePhn(inquirePhnRequest, createHttpServletRequest());
        
        InquirePhnResponse inquirePhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.SUCCESS, inquirePhnResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", inquirePhnResponse.getMessage());

        List<InquirePhnBeneficiary> beneficiaries = inquirePhnResponse.getBeneficiaries();
        assertEquals(1, beneficiaries.size());
        
        InquirePhnBeneficiary beneficiary = beneficiaries.get(0);
        
        assertEquals("9123456789", beneficiary.getPhn());
        assertEquals("GFMSRHNISNAME", beneficiary.getLastName());
        assertEquals("GERHNIMODFNAME", beneficiary.getFirstName());
        assertEquals("19700909", beneficiary.getDateOfBirth());
        assertEquals("M", beneficiary.getGender());
        assertEquals("Y", beneficiary.getEligible());
        assertEquals("N", beneficiary.getStudent());        
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.PHN_INQUIRY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testInquirePhn_error_noEligibilityDate() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R41_ERROR_NO_ELIGIBILITY_DATE)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        InquirePhnRequest inquirePhnRequest = new InquirePhnRequest();
        inquirePhnRequest.getPhns().add("9123456789");
        
        ResponseEntity<InquirePhnResponse> response = eligibilityController.inquirePhn(inquirePhnRequest, createHttpServletRequest());
        
        InquirePhnResponse inquirePhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, inquirePhnResponse.getStatus());
        assertEquals("RPBS0057 ELIGIBILITY DATE REQUIRED", inquirePhnResponse.getMessage());

        List<InquirePhnBeneficiary> beneficiaries = inquirePhnResponse.getBeneficiaries();
        assertEquals(0, beneficiaries.size());   
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.PHN_INQUIRY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 0);
	}
	
	@Test
	public void testInquirePhn_warning_invalidPHN() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R41_WARNING_PHN_INVALID)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        InquirePhnRequest inquirePhnRequest = new InquirePhnRequest();
        inquirePhnRequest.getPhns().add("9123456789");
        
        ResponseEntity<InquirePhnResponse> response = eligibilityController.inquirePhn(inquirePhnRequest, createHttpServletRequest());
        
        InquirePhnResponse inquirePhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.WARNING, inquirePhnResponse.getStatus());
        assertEquals("RPBS0073 PHN INVALID (9123456789)", inquirePhnResponse.getMessage());

        List<InquirePhnBeneficiary> beneficiaries = inquirePhnResponse.getBeneficiaries();
        assertEquals(0, beneficiaries.size());   
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.PHN_INQUIRY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 0);
	}
	
	@Test
	public void testInquirePhn_warning_phnNotFound() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R41_WARNING_NOT_FOUND)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        // There are two PHNs, one which generates a warning and one which is successful
        InquirePhnRequest inquirePhnRequest = new InquirePhnRequest();
        inquirePhnRequest.getPhns().add("9123123123");
        inquirePhnRequest.getPhns().add("9456456456");
        
        ResponseEntity<InquirePhnResponse> response = eligibilityController.inquirePhn(inquirePhnRequest, createHttpServletRequest());
        
        InquirePhnResponse inquirePhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.WARNING, inquirePhnResponse.getStatus());
        assertEquals("RPBS9145 PHN NOT FOUND (9123123123)", inquirePhnResponse.getMessage());

        List<InquirePhnBeneficiary> beneficiaries = inquirePhnResponse.getBeneficiaries();
        assertEquals(1, beneficiaries.size());
        
        InquirePhnBeneficiary beneficiary = beneficiaries.get(0);
        
        assertEquals("9456456456", beneficiary.getPhn());
        assertEquals("SMITH", beneficiary.getLastName());
        assertEquals("JENNIFER       JENNY", beneficiary.getFirstName());
        assertEquals("20000101", beneficiary.getDateOfBirth());
        assertEquals("F", beneficiary.getGender());
        assertEquals("N", beneficiary.getEligible());
        assertEquals("N", beneficiary.getStudent());        
        
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.PHN_INQUIRY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	
	@Test
	public void testLookupPhn_success() {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R42_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LookupPhnRequest lookupPhnRequest = new LookupPhnRequest();
        lookupPhnRequest.setContractNumber("123456789");
        lookupPhnRequest.setGroupNumber("1234567");
        
        ResponseEntity<LookupPhnResponse> response = eligibilityController.lookupPhn(lookupPhnRequest, createHttpServletRequest());
        
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
        
		// The request should produce:
		// - Transaction
		// - 'Transaction Start' TransactionEvent
		// - 2 x Request AffectedParty
		// - 'Message Sent' TransactionEvent
		// - 'Message Received' TransactionEvent
		// - 'Transaction Complete' TransactionEvent
		// - 1 x Response AffectedParty

        Transaction transaction = assertTransactionCreated(TransactionType.PHN_LOOKUP);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
        
		assertEquals("00000010", transaction.getOrganization());
		assertNotNull(transaction.getServer());
		assertNotNull(transaction.getSessionId());
		assertEquals("1.1.1.1", transaction.getSourceIp());
		assertNotNull(transaction.getStartTime());
		assertNotNull(transaction.getTransactionId());
		assertEquals(TransactionType.PHN_LOOKUP.getValue(), transaction.getType());
		assertEquals("unittest", transaction.getUserId());
        
		List<TransactionEvent> transactionEvents = transactionEventRepository.findAll(Sort.by("eventTime"));
		assertEquals(4, transactionEvents.size());
		
		TransactionEvent transactionStartEvent = transactionEvents.get(0);
		assertNotNull(transactionStartEvent.getEventTime());
		assertNull(transactionStartEvent.getMessageId());
		assertEquals(transaction, transactionStartEvent.getTransaction());
		assertNotNull(transactionStartEvent.getTransactionEventId());
		assertEquals(TransactionEventType.TRANSACTION_START.getValue(), transactionStartEvent.getType());

		TransactionEvent messageSentEvent = transactionEvents.get(1);
		assertNotNull(messageSentEvent.getEventTime());
		assertNull(messageSentEvent.getMessageId());
		assertEquals(transaction, messageSentEvent.getTransaction());
		assertNotNull(messageSentEvent.getTransactionEventId());
		assertEquals(TransactionEventType.MESSAGE_SENT.getValue(), messageSentEvent.getType());
		
		TransactionEvent messageReceivedEvent = transactionEvents.get(2);
		assertNotNull(messageReceivedEvent.getEventTime());
		assertNull(messageReceivedEvent.getMessageId());
		assertEquals(transaction, messageReceivedEvent.getTransaction());
		assertNotNull(messageReceivedEvent.getTransactionEventId());
		assertEquals(TransactionEventType.MESSAGE_RECEIVED.getValue(), messageReceivedEvent.getType());
		
		TransactionEvent transactionCompleteEvent = transactionEvents.get(3);
		assertNotNull(transactionCompleteEvent.getEventTime());
		assertNull(transactionCompleteEvent.getMessageId());
		assertEquals(transaction, transactionCompleteEvent.getTransaction());
		assertNotNull(transactionCompleteEvent.getTransactionEventId());
		assertEquals(TransactionEventType.TRANSACTION_COMPLETE.getValue(), transactionCompleteEvent.getType());
		
		List<AffectedParty> affectedParties = affectedPartyRepository.findAll(Sort.by("identifierType"));
		assertEquals(3, affectedParties.size());
		
		AffectedParty contractNumber = affectedParties.get(0);		
		assertNotNull(contractNumber.getAffectedPartyId());
		assertEquals("123456789", contractNumber.getIdentifier());
		assertEquals(IdentifierType.CONTRACT_NUMBER.getValue(), contractNumber.getIdentifierType());
		assertEquals(AffectedPartyDirection.OUTBOUND.getValue(), contractNumber.getDirection());
		assertEquals(transaction, contractNumber.getTransaction());		
		
		AffectedParty groupNumber = affectedParties.get(1);
		assertNotNull(groupNumber.getAffectedPartyId());
		assertEquals("1234567", groupNumber.getIdentifier());
		assertEquals(IdentifierType.GROUP_NUMBER.getValue(), groupNumber.getIdentifierType());
		assertEquals(AffectedPartyDirection.OUTBOUND.getValue(), groupNumber.getDirection());
		assertEquals(transaction, groupNumber.getTransaction());
		
		AffectedParty phn = affectedParties.get(2);
		assertNotNull(phn.getAffectedPartyId());
		assertEquals("9123456789", phn.getIdentifier());
		assertEquals(IdentifierType.PHN.getValue(), phn.getIdentifierType());
		assertEquals(AffectedPartyDirection.INBOUND.getValue(), phn.getDirection());
		assertEquals(transaction, phn.getTransaction());
	}

	@Test
	public void testLookupPhn_warning() {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R42_WARNING)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LookupPhnRequest lookupPhnRequest = new LookupPhnRequest();
        lookupPhnRequest.setContractNumber("123456789");
        lookupPhnRequest.setGroupNumber("1234567");
        
        ResponseEntity<LookupPhnResponse> response = eligibilityController.lookupPhn(lookupPhnRequest, createHttpServletRequest());
        
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
        
        assertTransactionCreated(TransactionType.PHN_LOOKUP);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 50);
	}
	
	@Test
	public void testLookupPhn_error_invalidContractNumber() {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R42_INVALID_CONTRACT_NUMBER)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LookupPhnRequest lookupPhnRequest = new LookupPhnRequest();
        lookupPhnRequest.setContractNumber("123456789");
        lookupPhnRequest.setGroupNumber("1234567");
        
        ResponseEntity<LookupPhnResponse> response = eligibilityController.lookupPhn(lookupPhnRequest, createHttpServletRequest());
        
        LookupPhnResponse lookupPhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, lookupPhnResponse.getStatus());
        assertEquals("RPBS0062 CONTRACT NUMBER ENTERED IS INVALID", lookupPhnResponse.getMessage());
		
		// Check the client request is sent as expected
        List<LookupPhnBeneficiary> beneficiaries = lookupPhnResponse.getBeneficiaries();
        assertEquals(0, beneficiaries.size());
        
        assertTransactionCreated(TransactionType.PHN_LOOKUP);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 0);
	}
	
	@Test
	public void testLookupPhn_error_invalidGroupNumber() {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R42_INVALID_GROUP_NUMBER)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        LookupPhnRequest lookupPhnRequest = new LookupPhnRequest();
        lookupPhnRequest.setContractNumber("123456789");
        lookupPhnRequest.setGroupNumber("1234567");
        
        ResponseEntity<LookupPhnResponse> response = eligibilityController.lookupPhn(lookupPhnRequest, createHttpServletRequest());
        
        LookupPhnResponse lookupPhnResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, lookupPhnResponse.getStatus());
        assertEquals("RPBS0041 GROUP NUMBER INVALID", lookupPhnResponse.getMessage());
		
		// Check the client request is sent as expected
        List<LookupPhnBeneficiary> beneficiaries = lookupPhnResponse.getBeneficiaries();
        assertEquals(0, beneficiaries.size());
        
        assertTransactionCreated(TransactionType.PHN_LOOKUP);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 0);
	}

    /**
     * The URL property used by the mocked endpoint needs to be set after the MockWebServer starts as the port it uses is 
     * created dynamically on start up to ensure it uses an available port so it is not known before then. 
     * @param registry
     */
    @DynamicPropertySource
    static void registerMockUrlProperty(DynamicPropertyRegistry registry) {
        registry.add("rapid.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
        registry.add("hibc.url", () -> String.format("http://localhost:%s", mockBackEnd.getPort()));
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


