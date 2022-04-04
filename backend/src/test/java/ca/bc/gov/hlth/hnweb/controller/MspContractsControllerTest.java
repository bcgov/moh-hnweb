package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.MemberAddress;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.BeneficiaryContractPeriod;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryBeneficiary;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryResponse;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.UpdateContractAddressRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.UpdateContractAddressResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class MspContractsControllerTest extends BaseControllerTest {
	
	private static final String R32_SUCCESS = "        RPBSPMC000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  98736722489873672248SPBIGDATASNAME                     SPBIGDATAFNAME                               1983-01-01F98736722550000001S2022-02-010000-00-00 98736722484044574C2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                         9873672255BIGDATASNAME                       BIGDATAFNAME                                 1983-09-09M98736722550000001C2022-02-010000-00-00 98736722484044574S2022-02-012022-02-28E                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              ";
	
	private static final String R32_WARNING_MORE_THAN_20_PERSONS_FOUND = "        RPBSPMC000000010                                INFO    RPBS0086MORE THAN 20 PERSONS FOUND - NOT ALL DISPLAYED                          98736722559873672255BIGDATASNAME                       BIGDATAFNAME                                 1983-09-09M98736722550000001C2022-02-010000-00-00 98736722484044574S2022-02-012022-02-28E                                                                                                                                                                                                                                                                                                                        9873672248SPBIGDATASNAME                     SPBIGDATAFNAME                               1983-01-01F98736722550000001S2022-02-010000-00-00 98736722484044574C2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                         9873671593CHSIXTNBIGDSNAME                   CHSIXTNBIGFNAME                              2018-01-16M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671601CHFRTENBDSNAME                     CHFRTNBDFNAME                                2018-01-14M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671769CHTRTBIGDTSNAME                    CHTRTBIGDATFNAM                              2018-01-13M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671776CHTWTREBIGDTSNAME                  CHTWTREBIGDTFNA                              2018-01-23M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671783CHTWNTTWBISNAME                    CHETWNTTWEBFNAM                              2018-01-21M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671809CHTWTONBIGSNAME                    CHTWTONBIGFNAME                              2018-01-21M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671816CHTNTYBIGDTSNAME                   CHNINTYBIGDTFNA                              2018-01-20M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671823CHNINTNBIGDTSNAME                  CHNINTBIGDAFNAM                              2018-01-19F98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671848CHEITNBIGDTSNAME                   CHDITNBIDTAFNAM                              2018-01-18M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671941CHSETNBIGDTSNAME                   CHSEVTNBDTFNAME                              2018-01-17M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671959CHFIFTNBIGDTSNAME                  CHFIFTNBIGDAFNA                              2018-01-15M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671966CHTLEBIGDTSNAME                    CHTLEBIGDTFNAME                              2018-12-12M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671973CHELNBIGDASNAME                    CHELNBIGDFNAME                               2018-11-11M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671998CHTENBIGSNAME                      CHTENBIGDFNAME                               2019-10-10M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873672001CHNINBIGDTSNAME                    CHNINBIGDATFNAM                              2019-09-09M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873672019CHETBIGDASNAME                     CHETBIGDTFNAME                               2018-08-08M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873672026CHSVNBIGDSNAME                     CHSVNBIGDFNAME                               2019-07-07M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873672033CHSIXBIGDTSNAME                    CHSIXBIGDTFNAME                              2018-06-06M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                ";
	
	private static final String R32_ERROR_PHN_NOT_FOUND = "        RPBSPMC000000010                                ERRORMSGRPBS9145PHN NOT FOUND                                                           9159869673		";
	
	private static final String R38_ERROR_PHN_NOT_FOUND = "        RPBSPMA000000010                                ERRORMSGRPBS9145PHN NOT FOUND                                                           915986967363371091551 39 street           NW                                                COLWOOD BC               V7V7V7";
	
	private static final String R38_SUCCESS = "        RPBSPMA000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  933291248662431095951 WDSOU YF            ZT5                      ZT5                      Armstrong                V4D7D75961 WDSOU ZF            ZT6                                               CRESTON BC               V8V8V8";
	
	private static final String R38_ERROR_ADDRESSESS_ALREADY_EXIST = "        RPBSPMA000000010                                ERRORMSGRPBS0102ADDRESSES ALREADY EXIST ON MSP. NO UPDATE DONE.                         93319269196337109TEST                     TEST                     TEST                     BC                       V8V8V8TEST1                    TEST1                    TEST1                    BC                       V7V7V7";
	
	private static final String R40_SUCCESS = "        RPBSPCI000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  93403381226337109                                                                                                                         TETS123                                                                                             V8V8V8                                                            9340338122PROTOCTIST ORDERXD                 SCIPIOXH       WILFRIDXP                     2002-10-19M2023-01-010000-00-00 NC9329090895PROTOCTIST ORDERXD                 YARISXN        DILLINXP                      2002-11-04F2023-01-012023-03-31ENS";
	
	private static final String R40_ERROR_PHN_NOT_FOUND = "        RPBSPCI000000010                                ERRORMSGRPBS9145PHN NOT FOUND                                                           91598696736337109";
	
	private static final String R40_WARNING_MORE_THAN_20_PERSONS_FOUND = "        RPBSPCI000000010                                RESPONSERPBS0059MORE THAN 20 PERSONS. PLEASE CONTACT MSP                                93329124866243109123456   1234565951 WDSOU YF            ZT 5                     CRESTON BC                                        V4D7N75951 WDSOU YF            ZT 5                     CRESTON BC                                        V4D7N7                    2506301086                              9332912486PROTOCTIST ORDERXD                 RYAN-CARTERXM  BRANDYNXD                     1956-08-25M2020-10-012022-12-31ENC9344911827PROTOCTIST ORDERXD                 GARNETXP       HARUNAOXJ                     2001-01-24F2020-10-012022-12-31EYD9306432103PROTOCTIST ORDERXD                 LASEYXH        KENDRENXD                     2001-02-06F2020-10-012022-12-31EYD9317310617PROTOCTIST ORDERXD                 MINGXQ         BENANXF                       2001-04-21F2020-10-012022-12-31EYD9378520641PROTOCTIST ORDERXD                 AYUUBXK        HEILYNXC                      2001-04-21M2020-10-012022-12-31EYD9397105575PROTOCTIST ORDERXD                 CADBYXL        BARKAATXB                     2001-07-07F2020-10-012022-12-31EYD9399900281PROTOCTIST ORDERXD                 BASHERXO       PARMAJITXC                    2002-06-19M2020-10-012022-12-31EYD9331926919PROTOCTIST ORDERXD                 JACK-LYALLXH   MO-CHARAXI                    2002-07-05F2020-10-012022-12-31EYD9340338122PROTOCTIST ORDERXD                 SCIPIOXH       WILFRIDXP                     2002-10-19M2020-10-012022-12-31EYD9329090895PROTOCTIST ORDERXD                 YARISXN        DILLINXP                      2002-11-04F2020-10-012022-12-31EYD9325719609PROTOCTIST ORDERXD                 JOHANNXB       SOURAVXL                      2002-11-25F2020-10-012022-12-31EYD9348175493PROTOCTIST ORDERXD                 SYCAMOREXC     SIMONIDESXD                   2002-12-23F2020-10-012022-12-31EYD9382771807PROTOCTIST ORDERXD                 HEINDRIKXI     KENETHXN                      2003-01-01F2020-10-012022-12-31EYD9303292543PROTOCTIST ORDERXD                 MUSSADAQXM     ORIANOXO                      2003-02-17F2020-10-012022-12-31END9329279733PROTOCTIST ORDERXD                 ALLEYNXO       IZAMXP                        2003-05-16M2020-10-012022-12-31END9307706834PROTOCTIST ORDERXD                 KEVENXG        DULLXJ                        2003-09-01M2020-10-012022-12-31END9318721811PROTOCTIST ORDERXD                 TENNYSONXF     CHUNXH                        2003-12-06M2020-10-012022-12-31END9874345713FEJO                               ERER                                         2004-01-01F2020-10-012022-12-31END9361354265PROTOCTIST ORDERXD                 DEDENISEOLUWAXAJYXD                          2004-01-25F2020-10-012022-12-31END9319815973PROTOCTIST ORDERXD                 EVYNXM         TOBIASXK                      2004-04-02F2020-10-012022-12-31END";
			
	private static final String R40_ERROR_NO_COVERAGE_FOUND = "        RPBSPCI000000010                                ERRORMSGRPBS0067NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP               98736722556337109";
	
	protected static DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
	
	@Autowired
	private MspContractsController mspContractsController;

	@Test
	public void testGetContractPeriods_success() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R32_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        GetContractPeriodsRequest request = new GetContractPeriodsRequest();
        request.setPhn("9873672255");
        
        ResponseEntity<GetContractPeriodsResponse> response = mspContractsController.getContractPeriods(request, createHttpServletRequest());
        
        GetContractPeriodsResponse getContractPeriodsResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.SUCCESS, getContractPeriodsResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", getContractPeriodsResponse.getMessage());

        assertEquals("9873672248", getContractPeriodsResponse.getPhn());
        
        List<BeneficiaryContractPeriod> beneficiarieBeneficiaryContractPeriods = getContractPeriodsResponse.getBeneficiaryContractPeriods();
        assertEquals(4, beneficiarieBeneficiaryContractPeriods.size());
       
        //The results consist of two groups of MSP Contract Periods, Beneficiaries PHN 9873672248 and PHN 9873672255.        
        
		/*
		 * 9873672248SPBIGDATASNAME                     SPBIGDATAFNAME                               1983-01-01F
		 * 98736722550000001S2022-02-010000-00-00 
		 * 98736722484044574C2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                         
		 */
        
        BeneficiaryContractPeriod beneficiaryContractPeriod1 = beneficiarieBeneficiaryContractPeriods.get(0);        
        assertEquals("9873672248", beneficiaryContractPeriod1.getPhn());
        assertEquals("SPBIGDATASNAME", beneficiaryContractPeriod1.getLastName());
        assertEquals("SPBIGDATAFNAME", beneficiaryContractPeriod1.getFirstName());
        assertEquals("19830101", beneficiaryContractPeriod1.getDateOfBirth());
        assertEquals("F", beneficiaryContractPeriod1.getGender());
        assertEquals("9873672255", beneficiaryContractPeriod1.getContractHolder());
        assertEquals("0000001", beneficiaryContractPeriod1.getGroupNumber());
        assertEquals("S", beneficiaryContractPeriod1.getRelationship());
        assertEquals("20220201", beneficiaryContractPeriod1.getEffectiveDate());
        assertEquals(null, beneficiaryContractPeriod1.getCancelDate());       
        assertEquals("", beneficiaryContractPeriod1.getCancelReason());
       
        BeneficiaryContractPeriod beneficiaryContractPeriod2 = beneficiarieBeneficiaryContractPeriods.get(1);        
        assertEquals("9873672248", beneficiaryContractPeriod2.getPhn());
        assertEquals("SPBIGDATASNAME", beneficiaryContractPeriod2.getLastName());
        assertEquals("SPBIGDATAFNAME", beneficiaryContractPeriod1.getFirstName());
        assertEquals("19830101", beneficiaryContractPeriod2.getDateOfBirth());
        assertEquals("F", beneficiaryContractPeriod2.getGender());
        assertEquals("9873672248", beneficiaryContractPeriod2.getContractHolder());
        assertEquals("4044574", beneficiaryContractPeriod2.getGroupNumber());
        assertEquals("C", beneficiaryContractPeriod2.getRelationship());
        assertEquals("20220201", beneficiaryContractPeriod2.getEffectiveDate());
        assertEquals(null, beneficiaryContractPeriod2.getCancelDate());       
        assertEquals("", beneficiaryContractPeriod2.getCancelReason());

        /*
		 * 9873672255BIGDATASNAME                       BIGDATAFNAME                                 1983-09-09M
		 * 98736722550000001C2022-02-010000-00-00 
		 * 98736722484044574S2022-02-012022-02-28E                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
         */
        BeneficiaryContractPeriod beneficiaryContractPeriod3 = beneficiarieBeneficiaryContractPeriods.get(2);        
        assertEquals("9873672255", beneficiaryContractPeriod3.getPhn());
        assertEquals("BIGDATASNAME", beneficiaryContractPeriod3.getLastName());
        assertEquals("BIGDATAFNAME", beneficiaryContractPeriod3.getFirstName());
        assertEquals("19830909", beneficiaryContractPeriod3.getDateOfBirth());
        assertEquals("M", beneficiaryContractPeriod3.getGender());
        assertEquals("9873672255", beneficiaryContractPeriod3.getContractHolder());
        assertEquals("0000001", beneficiaryContractPeriod3.getGroupNumber());
        assertEquals("C", beneficiaryContractPeriod3.getRelationship());
        assertEquals("20220201", beneficiaryContractPeriod3.getEffectiveDate());
        assertEquals(null, beneficiaryContractPeriod3.getCancelDate());       
        assertEquals("", beneficiaryContractPeriod3.getCancelReason());
       
        BeneficiaryContractPeriod beneficiaryContractPeriod4 = beneficiarieBeneficiaryContractPeriods.get(3);        
        assertEquals("9873672255", beneficiaryContractPeriod4.getPhn());
        assertEquals("BIGDATASNAME", beneficiaryContractPeriod4.getLastName());
        assertEquals("BIGDATAFNAME", beneficiaryContractPeriod4.getFirstName());
        assertEquals("19830909", beneficiaryContractPeriod4.getDateOfBirth());        
        assertEquals("M", beneficiaryContractPeriod4.getGender());
        assertEquals("9873672248", beneficiaryContractPeriod4.getContractHolder());
        assertEquals("4044574", beneficiaryContractPeriod4.getGroupNumber());
        assertEquals("S", beneficiaryContractPeriod4.getRelationship());
        assertEquals("20220201", beneficiaryContractPeriod4.getEffectiveDate());
        assertEquals("20220228", beneficiaryContractPeriod4.getCancelDate());       
        assertEquals("E", beneficiaryContractPeriod4.getCancelReason());
       
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.GET_CONTRACT_PERIODS);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 4);
	}
	
	@Test
	public void testGetContractPeriods_warning_moreThan20PersonsFound() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R32_WARNING_MORE_THAN_20_PERSONS_FOUND)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        GetContractPeriodsRequest request = new GetContractPeriodsRequest();
        request.setPhn("9873672255");
        
        ResponseEntity<GetContractPeriodsResponse> response = mspContractsController.getContractPeriods(request, createHttpServletRequest());
        
        GetContractPeriodsResponse getContractPeriodsResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.WARNING, getContractPeriodsResponse.getStatus());
        assertEquals("RPBS0086 MORE THAN 20 PERSONS FOUND - NOT ALL DISPLAYED", getContractPeriodsResponse.getMessage());

        assertEquals("9873672255", getContractPeriodsResponse.getPhn());
        
        List<BeneficiaryContractPeriod> beneficiarieBeneficiaryContractPeriods = getContractPeriodsResponse.getBeneficiaryContractPeriods();
        assertEquals(22, beneficiarieBeneficiaryContractPeriods.size());
       
        BeneficiaryContractPeriod beneficiaryContractPeriod1 = beneficiarieBeneficiaryContractPeriods.get(0);        
        assertEquals("9873672255", beneficiaryContractPeriod1.getPhn());
        assertEquals("BIGDATASNAME", beneficiaryContractPeriod1.getLastName());
        assertEquals("BIGDATAFNAME", beneficiaryContractPeriod1.getFirstName());
        assertEquals("19830909", beneficiaryContractPeriod1.getDateOfBirth());
        assertEquals("M", beneficiaryContractPeriod1.getGender());
        assertEquals("9873672255", beneficiaryContractPeriod1.getContractHolder());
        assertEquals("0000001", beneficiaryContractPeriod1.getGroupNumber());
        assertEquals("C", beneficiaryContractPeriod1.getRelationship());
        assertEquals("20220201", beneficiaryContractPeriod1.getEffectiveDate());
        assertEquals(null, beneficiaryContractPeriod1.getCancelDate());       
        assertEquals("", beneficiaryContractPeriod1.getCancelReason());
       
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));        
        
        assertTransactionCreated(TransactionType.GET_CONTRACT_PERIODS);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 22);
	}
	
	@Test
	public void testGetContractPeriods_error_phnNotFound() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R32_ERROR_PHN_NOT_FOUND)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        GetContractPeriodsRequest request = new GetContractPeriodsRequest();
        request.setPhn("9159869673");
        
        ResponseEntity<GetContractPeriodsResponse> response = mspContractsController.getContractPeriods(request, createHttpServletRequest());
        
        GetContractPeriodsResponse getContractPeriodsResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, getContractPeriodsResponse.getStatus());
        assertEquals("RPBS9145 PHN NOT FOUND", getContractPeriodsResponse.getMessage());

        assertEquals("9159869673", getContractPeriodsResponse.getPhn());
        
        List<BeneficiaryContractPeriod> beneficiarieBeneficiaryContractPeriods = getContractPeriodsResponse.getBeneficiaryContractPeriods();
        assertEquals(0, beneficiarieBeneficiaryContractPeriods.size());
       
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.GET_CONTRACT_PERIODS);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 1);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testContractInquiry_success() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R40_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ContractInquiryRequest request = new ContractInquiryRequest();
        request.setPhn("9340338122");
        request.setGroupNumber("6337109");
        
        ResponseEntity<ContractInquiryResponse> response = mspContractsController.inquireContract(request, createHttpServletRequest());
        
        ContractInquiryResponse contractInquiryResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.SUCCESS, contractInquiryResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", contractInquiryResponse.getMessage());

        assertEquals("9340338122", contractInquiryResponse.getPhn());
        List<ContractInquiryBeneficiary> contractInquiryBeneficiaries = contractInquiryResponse.getContractInquiryBeneficiaries();
        assertEquals(2, contractInquiryBeneficiaries.size());
        
        ContractInquiryBeneficiary contractInquiryBeneficiary1 = contractInquiryBeneficiaries.get(0);
        ContractInquiryBeneficiary contractInquiryBeneficiary2 = contractInquiryBeneficiaries.get(1);
        
        assertEquals("9340338122", contractInquiryBeneficiary1.getPhn());
        assertEquals("SCIPIOXH", contractInquiryBeneficiary1.getFirstName());
        assertEquals("WILFRIDXP", contractInquiryBeneficiary1.getSecondName());
        assertEquals("", contractInquiryBeneficiary1.getThirdName());
        assertEquals("20021019", contractInquiryBeneficiary1.getBirthDate());
        assertEquals("20230101", contractInquiryBeneficiary1.getEffectiveDate());
        assertEquals(null, contractInquiryBeneficiary1.getCancelDate());
        assertEquals("C", contractInquiryBeneficiary1.getRelationshipCode());
        assertEquals("N", contractInquiryBeneficiary1.getStudentStatus());
        assertEquals("M", contractInquiryBeneficiary1.getGender());
        assertEquals(" ", contractInquiryBeneficiary1.getCancelReason());
        
        assertEquals("9329090895", contractInquiryBeneficiary2.getPhn());
        assertEquals("YARISXN", contractInquiryBeneficiary2.getFirstName());
        assertEquals("DILLINXP", contractInquiryBeneficiary2.getSecondName());
        assertEquals("", contractInquiryBeneficiary2.getThirdName());
        assertEquals("20021104", contractInquiryBeneficiary2.getBirthDate());
        assertEquals("20230101", contractInquiryBeneficiary2.getEffectiveDate());
        assertEquals("20230331", contractInquiryBeneficiary2.getCancelDate());
        assertEquals("S", contractInquiryBeneficiary2.getRelationshipCode());
        assertEquals("N", contractInquiryBeneficiary2.getStudentStatus());
        assertEquals("F", contractInquiryBeneficiary2.getGender());
        assertEquals("E", contractInquiryBeneficiary2.getCancelReason()); 
        
     // Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        
        assertTransactionCreated(TransactionType.CONTRACT_INQUIRY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 2);
	}
	
	@Test
	public void testContractInquiry_error_phnNotFound() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R40_ERROR_PHN_NOT_FOUND)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ContractInquiryRequest request = new ContractInquiryRequest();
        request.setPhn("9159869673");
        request.setGroupNumber("6337109");
        
        ResponseEntity<ContractInquiryResponse> response = mspContractsController.inquireContract(request, createHttpServletRequest());
        
        ContractInquiryResponse contractInquiryResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, contractInquiryResponse.getStatus());
        assertEquals("RPBS9145 PHN NOT FOUND", contractInquiryResponse.getMessage());

        assertEquals("9159869673", contractInquiryResponse.getPhn());
        
        List<ContractInquiryBeneficiary> contractInquiryBeneficiaries = contractInquiryResponse.getContractInquiryBeneficiaries();
        assertEquals(0, contractInquiryBeneficiaries.size());
       
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertTransactionCreated(TransactionType.CONTRACT_INQUIRY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testContractInquiry_error_NoCoverageFound() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R40_ERROR_NO_COVERAGE_FOUND)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ContractInquiryRequest request = new ContractInquiryRequest();
        request.setPhn("9873672255");
        request.setGroupNumber("6337109");
        ResponseEntity<ContractInquiryResponse> response = mspContractsController.inquireContract(request, createHttpServletRequest());
        
        ContractInquiryResponse contractInquiryResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, contractInquiryResponse.getStatus());
        assertEquals("RPBS0067 NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP", contractInquiryResponse.getMessage());

        assertEquals("9873672255", contractInquiryResponse.getPhn());
        
        List<ContractInquiryBeneficiary> contractInquiryBeneficiaries = contractInquiryResponse.getContractInquiryBeneficiaries();
        assertEquals(0, contractInquiryBeneficiaries.size());
       
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE)); 
        assertTransactionCreated(TransactionType.CONTRACT_INQUIRY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testContractInquiry_warning_moreThan20PersonsFound() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R40_WARNING_MORE_THAN_20_PERSONS_FOUND)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        ContractInquiryRequest request = new ContractInquiryRequest();
        request.setPhn("9332912486");
        request.setGroupNumber("6243109");
        
        ResponseEntity<ContractInquiryResponse> response = mspContractsController.inquireContract(request, createHttpServletRequest());
        
        ContractInquiryResponse contractInquiryResponse= response.getBody();

		// Check the response
		assertEquals(StatusEnum.WARNING, contractInquiryResponse.getStatus());
		assertEquals("5951 WDSOU YF", contractInquiryResponse.getHomeAddressLine1());
		assertEquals("ZT 5", contractInquiryResponse.getHomeAddressLine2());
		assertEquals("CRESTON BC", contractInquiryResponse.getHomeAddressLine3());
		assertEquals("", contractInquiryResponse.getHomeAddressLine4());
		assertEquals("V4D7N7", contractInquiryResponse.getHomeAddressPostalCode());

		assertEquals("5951 WDSOU YF", contractInquiryResponse.getMailingAddressLine1());
		assertEquals("ZT 5", contractInquiryResponse.getMailingAddressLine2());
		assertEquals("CRESTON BC", contractInquiryResponse.getMailingAddressLine3());
		assertEquals("", contractInquiryResponse.getMailingAddressLine4());
		assertEquals("V4D7N7", contractInquiryResponse.getMailingAddressPostalCode());

		assertEquals("250 6301086", contractInquiryResponse.getTelephone());
		assertEquals("123456", contractInquiryResponse.getGroupMemberNumber());
		assertEquals("123456", contractInquiryResponse.getGroupMemberDepartmentNumber());
        
        assertEquals(20, contractInquiryResponse.getContractInquiryBeneficiaries().size());
        
        assertEquals("RPBS0059 MORE THAN 20 PERSONS. PLEASE CONTACT MSP", contractInquiryResponse.getMessage());
        
     // Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE)); 
        assertTransactionCreated(TransactionType.CONTRACT_INQUIRY);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 20);
	}
	
	@Test
	public void testUpdateContractAddress_success() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R38_SUCCESS)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        UpdateContractAddressRequest request = new UpdateContractAddressRequest();
        request.setPhn("9332912486");
        request.setGroupNumber("6243109");
        MemberAddress homeAddress = new MemberAddress();
        homeAddress.setAddressLine1("5951 WDSOU YF");
        homeAddress.setAddressLine2("ZT5");
        homeAddress.setAddressLine2("ZT6");
        homeAddress.setAddressLine2("CRESTON BC");
        request.setHomeAddress(homeAddress);
        
        MemberAddress mailingAddress = new MemberAddress();
        mailingAddress.setAddressLine1("5951 WDSOU YF");
        mailingAddress.setAddressLine2("ZT5");
        mailingAddress.setAddressLine2("ZT6");
        mailingAddress.setAddressLine2("CRESTON BC");
        request.setMailingAddress(mailingAddress);
        
        ResponseEntity<UpdateContractAddressResponse> response = mspContractsController.updateContractAddress(request, createHttpServletRequest());      
        UpdateContractAddressResponse updateContractAddressResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.SUCCESS, updateContractAddressResponse.getStatus());
        assertEquals("TRANSACTION SUCCESSFUL", updateContractAddressResponse.getMessage());

        assertEquals("9332912486", updateContractAddressResponse.getPhn());
       
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertTransactionCreated(TransactionType.UPDATE_CONTRACT_ADDRESS);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testUpdateContractAddress_error_addressAlreadyExist() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R38_ERROR_ADDRESSESS_ALREADY_EXIST)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        UpdateContractAddressRequest request = new UpdateContractAddressRequest();
        request.setPhn("9331926919");
        request.setGroupNumber("6337109");
        
        MemberAddress homeAddress = new MemberAddress();
        homeAddress.setAddressLine1("TEST");
        homeAddress.setAddressLine2("TEST");
        homeAddress.setAddressLine2("TEST");
        homeAddress.setAddressLine2("BC");
        homeAddress.setPostalCode("V8V8V8");
        request.setHomeAddress(homeAddress);
        
        MemberAddress mailingAddress = new MemberAddress();
        mailingAddress.setAddressLine1("TEST1");
        mailingAddress.setAddressLine2("TEST1");
        mailingAddress.setAddressLine2("TEST1");
        mailingAddress.setAddressLine2("BC");
        homeAddress.setPostalCode("V7V7V7");
        request.setMailingAddress(mailingAddress);
        
        ResponseEntity<UpdateContractAddressResponse> response = mspContractsController.updateContractAddress(request, createHttpServletRequest());
        
        UpdateContractAddressResponse updateContractAddressResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, updateContractAddressResponse.getStatus());
        assertEquals("RPBS0102 ADDRESSES ALREADY EXIST ON MSP. NO UPDATE DONE.", updateContractAddressResponse.getMessage());

        assertEquals("9331926919", updateContractAddressResponse.getPhn());
       
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertTransactionCreated(TransactionType.UPDATE_CONTRACT_ADDRESS);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
	}
	
	@Test
	public void testUpdateContractAddress_error_phnNotFound() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse()
        		.setBody(R38_ERROR_PHN_NOT_FOUND)
        	    .addHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN.toString()));
        
        UpdateContractAddressRequest request = new UpdateContractAddressRequest();
        request.setPhn("9159869673");
        request.setGroupNumber("6337109");
        
        ResponseEntity<UpdateContractAddressResponse> response = mspContractsController.updateContractAddress(request, createHttpServletRequest());
        
        UpdateContractAddressResponse updateContractAddressResponse = response.getBody();

		// Check the response
        assertEquals(StatusEnum.ERROR, updateContractAddressResponse.getStatus());
        assertEquals("RPBS9145 PHN NOT FOUND", updateContractAddressResponse.getMessage());

        assertEquals("9159869673", updateContractAddressResponse.getPhn());
       
		// Check the client request is sent as expected
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();        
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(MediaType.TEXT_PLAIN.toString(), recordedRequest.getHeader(CONTENT_TYPE));
        assertTransactionCreated(TransactionType.UPDATE_CONTRACT_ADDRESS);
        assertAffectedParyCount(AffectedPartyDirection.OUTBOUND, 2);
        assertAffectedParyCount(AffectedPartyDirection.INBOUND, 1);
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