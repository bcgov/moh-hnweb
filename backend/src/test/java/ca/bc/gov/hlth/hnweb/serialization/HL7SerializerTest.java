package ca.bc.gov.hlth.hnweb.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.utils.TestUtil;

public class HL7SerializerTest {

	private static final String path = "src\\test\\resources\\GetDemographicsResponse.xml";
	
	@BeforeAll
    static void setUp() throws IOException {
   
        mockStatic = Mockito.mockStatic(SecurityUtil.class);
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "00000010", "hnweb-user"));
    }

	private static MockedStatic<SecurityUtil> mockStatic;

	@Test
	public void test_toXML() throws Exception {

		HL7Config hl7Config = new HL7Config();
		HL7Serializer hl7 = new HL7Serializer(hl7Config);

		MessageMetaData mmd = new MessageMetaData("testId");
		mmd.setDataEntererExt("train96");
		mmd.setOrganization("HOOPC");
		mmd.setSourceSystemOverride("BCHCIM");
		GetDemographicsRequest getDemoQuery = new GetDemographicsRequest();
		getDemoQuery.setPhn("9862716574");

		Object request = hl7.toXml(getDemoQuery, mmd);
		//assertNotNull(request);
				
		GetDemographicsResponse demographicsResponse = hl7.fromXml(TestUtil.convertXMLFileToString(path), GetDemographicsResponse.class);
		assertEquals("9862716574", demographicsResponse.getPerson().getPhn());
		assertEquals(3, demographicsResponse.getResultCount());
		mockStatic.close();

	}


}
