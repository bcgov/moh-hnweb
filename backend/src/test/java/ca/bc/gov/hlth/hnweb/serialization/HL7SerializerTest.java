package ca.bc.gov.hlth.hnweb.serialization;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesRequest;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResponse;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.utils.TestUtil;

public class HL7SerializerTest {

	private static final String path1 = "src/test/resources/FindCandidatesResponse_Multiples.xml";
	private static final String path = "src/test/resources/GetDemographicsResponse.xml";
	
	@BeforeAll
    static void setUp() throws IOException {
   
        mockStatic = Mockito.mockStatic(SecurityUtil.class);
        mockStatic.when(SecurityUtil::loadUserInfo).thenReturn(new UserInfo("unittest", "00000010", "Ministry of Health", "hnweb-user"));
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
		assertNotNull(request);
				
		GetDemographicsResponse demographicsResponse = hl7.fromXml(TestUtil.convertXMLFileToString(path), GetDemographicsResponse.class);
		assertEquals("9862716574", demographicsResponse.getPerson().getPhn());
		assertEquals(3, demographicsResponse.getResultCount());
		mockStatic.close();

	}
	
	@Test
	public void test_findCandidate() throws Exception {

		HL7Config hl7Config = new HL7Config();
		HL7Serializer hl7 = new HL7Serializer(hl7Config);

		MessageMetaData mmd = new MessageMetaData("testId");
		mmd.setDataEntererExt("train96");
		mmd.setOrganization("HOOPC");
		mmd.setSourceSystemOverride("BCHCIM");
		FindCandidatesRequest getDemoQuery = new FindCandidatesRequest();
		Name name = new Name();
		name.setSurname("Purple");
		name.setFirstGivenName("Barney");
		getDemoQuery.setName(name);
		getDemoQuery.setBirthDate("19400101");


		Object request = hl7.toXml(getDemoQuery, mmd);
		assertNotNull(request);
				
		FindCandidatesResponse demographicsResponse = hl7.fromXml(TestUtil.convertXMLFileToString(path1), FindCandidatesResponse.class);
		assertEquals("9999999999", demographicsResponse.getResults().get(0).getPerson().getPhn());
		assertEquals(3, demographicsResponse.getResultCount());
		assertEquals(LocalDate.of(1940, 06, 06), demographicsResponse.getResults().get(0).getPerson().getBirthDate());

	}


}
