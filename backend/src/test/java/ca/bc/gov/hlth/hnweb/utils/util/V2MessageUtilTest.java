package ca.bc.gov.hlth.hnweb.utils.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIH;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIK;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.segment.IN1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.Parser;

@SpringBootTest
public class V2MessageUtilTest {
	
	@Autowired
	private Parser parser;	
	
	@Test
	public void testSetMshValues() throws HL7Exception, IOException {

		//Create a default R50 message with MSH-9 set to R50 Z03 
    	R50 r50Zo6 = new R50();
    	r50Zo6.initQuickstart("R50", "Z06", "P");
		
    	V2MessageUtil.setMshValues(r50Zo6.getMSH(), "HNWeb", "BC01000030", "RAIENROL-EMP", "BC00001013", "20200529114230", "10-ANother", "20200529114230", "D", "2.4");		
		MSH msh = r50Zo6.getMSH();		
		String encoded = parser.doEncode(msh, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("MSH|^~\\&|HNWeb|BC01000030|RAIENROL-EMP|BC00001013|20200529114230|10-ANother|R50^Z06|20200529114230|D|2.4", encoded);
	}
	
	@Test
	public void testSetZhdValues() throws HL7Exception, IOException {

		//Create a default R50 message with MSH-9 set to R50 Z03 
    	R50 r50Zo6 = new R50();
    	r50Zo6.initQuickstart("R50", "Z06", "P");
		
    	V2MessageUtil.setZhdValues(r50Zo6.getZHD(), "20200529114230", "00000010", "HNAIADMINISTRATION", "2.4");		
    	ZHD zhd = r50Zo6.getZHD();		
		String encoded = parser.doEncode(zhd, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("ZHD|20200529114230|^^00000010|HNAIADMINISTRATION||||2.4", encoded);
	}
	
	@Test
	public void testSetPidValues() throws HL7Exception, IOException {

		//Create a default R50 message with MSH-9 set to R50 Z03 
    	R50 r50Zo6 = new R50();
    	r50Zo6.initQuickstart("R50", "Z06", "P");
		
    	V2MessageUtil.setPidValues(r50Zo6.getPID(), "9999999999", "BC", "PH", "", "19700303", "M");		
    	PID pid = r50Zo6.getPID();		
		String encoded = parser.doEncode(pid, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("PID||9999999999^^^BC^PH|||||19700303|M", encoded);
	}
	
	@Test
	public void testSetZIAValues() throws HL7Exception, IOException {

		//Create a default R50 message with MSH-9 set to R50 Z03 
    	R50 r50Zo6 = new R50();
    	r50Zo6.initQuickstart("R50", "Z03", "P");
		
    	V2MessageUtil.setZiaValues(r50Zo6.getZIA(), "20210101", "HELP^RERE^^^^^L", 
				  "898 RETER ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^H", 
			      "123 UIYUI ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^M", 
			      "^PRN^PH^^^250^8578974", "S", "AB");		
		ZIA zia = r50Zo6.getZIA();		
		String ziaEncoded = parser.doEncode(zia, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("ZIA||20210101|||||||||||||HELP^RERE^^^^^L|898 RETER ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^H~123 UIYUI ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^M|^PRN^PH^^^250^8578974|||||||S|AB", ziaEncoded);
	}
	
	@Test
	public void testSetIn1Values() throws HL7Exception, IOException {

		//Create a default R50 message with MSH-9 set to R50 Z03 
    	R50 r50Zo6 = new R50();
    	r50Zo6.initQuickstart("R50", "Z06", "P");
		
    	V2MessageUtil.setIn1Values(r50Zo6.getIN1(), "6337109", "789446", "123456", "20190501", "20201231");		
    	IN1 in1 = r50Zo6.getIN1();		
		String encoded = parser.doEncode(in1, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("IN1||||||||6337109||789446|^^123456|20190501|20201231", encoded);
	}
	
	@Test
	public void testSetZihValues() throws HL7Exception, IOException {

		//Create a default R50 message with MSH-9 set to R50 Z03 
    	R50 r50Zo6 = new R50();
    	r50Zo6.initQuickstart("R50", "Z06", "P");
		
    	V2MessageUtil.setZihValues(r50Zo6.getZIH(), "D");		
    	ZIH zih = r50Zo6.getZIH();		
		String encoded = parser.doEncode(zih, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("ZIH|||||||||||||||||||D", encoded);
	}
	
	@Test
	public void testSetZikValues() throws HL7Exception, IOException {

		//Create a default R50 message with MSH-9 set to R50 Z03 
    	R50 r50Zo6 = new R50();
    	r50Zo6.initQuickstart("R50", "Z06", "P");
		
    	V2MessageUtil.setZikValues(r50Zo6.getZIK(), "20210101", "20221231");		
    	ZIK zik = r50Zo6.getZIK();		
		String encoded = parser.doEncode(zik, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("ZIK||||VISA_ISSUE^20210101~VISA_XPIRY^20221231", encoded);
	}
	
}
