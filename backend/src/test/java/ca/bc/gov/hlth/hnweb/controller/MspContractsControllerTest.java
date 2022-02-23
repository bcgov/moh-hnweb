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
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.BeneficiaryContractPeriod;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsResponse;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class MspContractsControllerTest extends BaseControllerTest {
	
	private static final String R32_SUCCESS = "        RPBSPMC000000010                                RESPONSERPBS9014TRANSACTION SUCCESSFUL                                                  98736722489873672248SPBIGDATASNAME                     SPBIGDATAFNAME                               1983-01-01F98736722550000001S2022-02-010000-00-00 98736722484044574C2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                         9873672255BIGDATASNAME                       BIGDATAFNAME                                 1983-09-09M98736722550000001C2022-02-010000-00-00 98736722484044574S2022-02-012022-02-28E                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              ";
	
	private static final String R32_WARNING_MORE_THAN_20_PERSONS_FOUND = "        RPBSPMC000000010                                INFO    RPBS0086MORE THAN 20 PERSONS FOUND - NOT ALL DISPLAYED                          98736722559873672255BIGDATASNAME                       BIGDATAFNAME                                 1983-09-09M98736722550000001C2022-02-010000-00-00 98736722484044574S2022-02-012022-02-28E                                                                                                                                                                                                                                                                                                                        9873672248SPBIGDATASNAME                     SPBIGDATAFNAME                               1983-01-01F98736722550000001S2022-02-010000-00-00 98736722484044574C2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                         9873671593CHSIXTNBIGDSNAME                   CHSIXTNBIGFNAME                              2018-01-16M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671601CHFRTENBDSNAME                     CHFRTNBDFNAME                                2018-01-14M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671769CHTRTBIGDTSNAME                    CHTRTBIGDATFNAM                              2018-01-13M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671776CHTWTREBIGDTSNAME                  CHTWTREBIGDTFNA                              2018-01-23M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671783CHTWNTTWBISNAME                    CHETWNTTWEBFNAM                              2018-01-21M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671809CHTWTONBIGSNAME                    CHTWTONBIGFNAME                              2018-01-21M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671816CHTNTYBIGDTSNAME                   CHNINTYBIGDTFNA                              2018-01-20M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671823CHNINTNBIGDTSNAME                  CHNINTBIGDAFNAM                              2018-01-19F98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671848CHEITNBIGDTSNAME                   CHDITNBIDTAFNAM                              2018-01-18M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671941CHSETNBIGDTSNAME                   CHSEVTNBDTFNAME                              2018-01-17M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671959CHFIFTNBIGDTSNAME                  CHFIFTNBIGDAFNA                              2018-01-15M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671966CHTLEBIGDTSNAME                    CHTLEBIGDTFNAME                              2018-12-12M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671973CHELNBIGDASNAME                    CHELNBIGDFNAME                               2018-11-11M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873671998CHTENBIGSNAME                      CHTENBIGDFNAME                               2019-10-10M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873672001CHNINBIGDTSNAME                    CHNINBIGDATFNAM                              2019-09-09M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873672019CHETBIGDASNAME                     CHETBIGDTFNAME                               2018-08-08M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873672026CHSVNBIGDSNAME                     CHSVNBIGDFNAME                               2019-07-07M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                9873672033CHSIXBIGDTSNAME                    CHSIXBIGDTFNAME                              2018-06-06M98736722550000001D2022-02-010000-00-00                                                                                                                                                                                                                                                                                                                                                                ";
	
	private static final String R32_ERROR_PHN_NOT_FOUND = "        RPBSPMC000000010                                ERRORMSGRPBS9145PHN NOT FOUND                                                           9159869673		";
	
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
        
		/**
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

        /**
        * 
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


