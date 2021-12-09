package ca.bc.gov.hlth.hnweb.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;

public class HL7SerializerTest {

	private static final Logger log = LoggerFactory.getLogger(HL7SerializerTest.class);

	@Autowired
	EnrollmentService service;

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
		mockStatic = Mockito.mockStatic(SecurityUtil.class);
		mockStatic.when(SecurityUtil::loadUserInfo)
				.thenReturn(new UserInfo("unittest", "00000010", "hnweb-user"));

		GetDemographicsResponse demographicsResponse = hl7.fromXml(convertXMLFileToString(), GetDemographicsResponse.class);
		assertEquals("9862716574", demographicsResponse.getPerson().getPhn());
		assertEquals(3, demographicsResponse.getResultCount());
		mockStatic.close();

		log.info(request.toString());
	}

	private String convertXMLFileToString() throws IOException {
		// our XML file for this example
		File xmlFile = new File("src\\test\\resources\\GetDemographicsResponse.xml");

		// Let's get XML file as String using BufferedReader
		// FileReader uses platform's default character encoding
		// if you need to specify a different encoding,
		// use InputStreamReader
		Reader fileReader;

		fileReader = new FileReader(xmlFile);

		BufferedReader bufReader = new BufferedReader(fileReader);

		StringBuilder sb = new StringBuilder();
		String line = bufReader.readLine();
		while (line != null) {
			sb.append(line).append("\n");
			line = bufReader.readLine();
		}
		String xml2String = sb.toString();
		log.info("XML to String using BufferedReader : ");

		bufReader.close();

		return xml2String;
	}
}
