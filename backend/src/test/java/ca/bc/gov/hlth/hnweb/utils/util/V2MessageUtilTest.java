package ca.bc.gov.hlth.hnweb.utils.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.model.v2.segment.HDR;
import ca.bc.gov.hlth.hnweb.model.v2.segment.QPD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.SFT;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIH;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIK;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.segment.IN1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.RCP;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.Parser;

@SpringBootTest
public class V2MessageUtilTest {
	
	@Autowired
	private Parser parser;	
	
	@Test
	public void testSetMshValues() throws HL7Exception {

		//Create a R50 message 
    	R50 r50Zo6 = new R50();
		
    	V2MessageUtil.setMshValues(r50Zo6.getMSH(), "HNWeb", "BC01000030", "RAIENROL-EMP", "BC00001013", "20200529114230", "10-ANother", "R50^Z06", "20200529114230", "D");		
		MSH msh = r50Zo6.getMSH();		
		String encoded = parser.doEncode(msh, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("MSH|^~\\&|HNWeb|BC01000030|RAIENROL-EMP|BC00001013|20200529114230|10-ANother|R50^Z06|20200529114230|D|2.4", encoded);
	}
	
	@Test
	public void testSetZhdValues() throws HL7Exception {

		//Create a R50 message 
    	R50 r50Zo6 = new R50();
		
    	V2MessageUtil.setZhdValues(r50Zo6.getZHD(), "20200529114230", "00000010", "HNAIADMINISTRATION", "2.4");		
    	ZHD zhd = r50Zo6.getZHD();		
		String encoded = parser.doEncode(zhd, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("ZHD|20200529114230|^^00000010|HNAIADMINISTRATION||||2.4", encoded);
	}
	
	@Test
	public void testSetPidValues() throws HL7Exception {

		//Create a R50 message 
    	R50 r50Zo6 = new R50();
		
    	V2MessageUtil.setPidValues(r50Zo6.getPID(), "9999999999", "BC", "PH", "", "19700303", "M");		
    	PID pid = r50Zo6.getPID();		
		String encoded = parser.doEncode(pid, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("PID||9999999999^^^BC^PH|||||19700303|M", encoded);
	}
	
	@Test
	public void testSetZIAValues() throws HL7Exception {

		//Create a R50 message 
    	R50 r50Zo6 = new R50();
    	ZIA zia = r50Zo6.getZIA();
    	V2MessageUtil.setZiaValues(zia, "20210101", "HELP", "RERE", "",				 
			      "2508578974", "S", "AB");	
    	V2MessageUtil.setZiaExtendedAddrees1(zia, "1541", "22A street","sw", "Victoria", "BC", "T6T6T6");
				
		String ziaEncoded = parser.doEncode(zia, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("ZIA||20210101|||||||||||||HELP^RERE^^^^^L|1541^22A street^sw^^^^^^^^^^^^^^^^^Victoria^BC^T6T6T6^^H|^PRN^PH^^^250^8578974|||||||S|AB", ziaEncoded);
	}
	
	@Test
	public void testSetIn1Values() throws HL7Exception {

		//Create a R50 message 
    	R50 r50Zo6 = new R50();
		
    	V2MessageUtil.setIn1Values(r50Zo6.getIN1(), "6337109", "789446", "123456", "20190501", "20201231");		
    	IN1 in1 = r50Zo6.getIN1();		
		String encoded = parser.doEncode(in1, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("IN1||||||||6337109||789446|^^123456|20190501|20201231", encoded);
	}
	
	@Test
	public void testSetZihValues() throws HL7Exception {

		//Create a R50 message 
    	R50 r50Zo6 = new R50();
		
    	V2MessageUtil.setZihValues(r50Zo6.getZIH(), "D");		
    	ZIH zih = r50Zo6.getZIH();		
		String encoded = parser.doEncode(zih, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("ZIH|||||||||||||||||||D", encoded);
	}
	
	@Test
	public void testSetZikValues() throws HL7Exception {

		//Create a R50 message 
    	R50 r50Zo6 = new R50();
		
    	V2MessageUtil.setZikValues(r50Zo6.getZIK(), "20210101", "20221231");		
    	ZIK zik = r50Zo6.getZIK();		
		String encoded = parser.doEncode(zik, EncodingCharacters.getInstance(r50Zo6));		
		assertEquals("ZIK||||VISA_ISSUE^20210101~VISA_XPIRY^20221231", encoded);
	}
	
	@Test
	public void testSetHdrValues() throws HL7Exception {

		//Create an E45 message  
    	E45 e45 = new E45();
		
    	V2MessageUtil.setHdrValues(e45.getHDR(), "TRAININGAdmin");		
    	HDR hdr = e45.getHDR();		
		String encoded = parser.doEncode(hdr, EncodingCharacters.getInstance(e45));		
		assertEquals("HDR|||TRAININGAdmin", encoded);
	}
	
	@Test
	public void testSetSftValues() throws HL7Exception {

		//Create an E45 message  
    	E45 e45 = new E45();
		
    	V2MessageUtil.setSftValues(e45.getSFT(), "1.0", "testorg", "101", "MOH", "1.0", "barebones");		
    	SFT sft = e45.getSFT();		
		String encoded = parser.doEncode(sft, EncodingCharacters.getInstance(e45));		
		assertEquals("SFT|1.0||testorg^^101^^^MOH|1.0|barebones", encoded);
	}	

	@Test
	public void testSetQpdValues() throws HL7Exception {

		//Create an E45 message  
    	E45 e45 = new E45();
		
    	V2MessageUtil.setQpdValues(e45.getQPD(), "E45^^HNET0003", "1", "9865827321", "19730131", "20210705", true, false, true);		
    	QPD qpd = e45.getQPD();		
		String encoded = parser.doEncode(qpd, EncodingCharacters.getInstance(e45));		
		assertEquals("QPD|E45^^HNET0003|1|^^00000001^^^CANBC^XX^MOH|^^00000001^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9865827321^^^CANBC^JHN^MOH||19730131||||||20210705||ENDRSN^^HNET9909~CCARD^^HNET9909~PVC^^HNET9909~PRS^^HNET9909", encoded);
	}

	@Test
	public void testSetRcpValues() throws HL7Exception {

		//Create an E45 message  
    	E45 e45 = new E45();
		
    	V2MessageUtil.setRcpValues(e45.getRCP(), "I");		
    	RCP rcp = e45.getRCP();		
		String encoded = parser.doEncode(rcp, EncodingCharacters.getInstance(e45));		
		assertEquals("RCP|I", encoded);
	}
	
	@Test
	public void testCorrectMSH9_E45_correctionRequired() {
		String e45 = "\"MSH|^~\\\\&|RAIELG-CNFRM|BC00001013|HNCLINET|moh_hnclient_dev|20211109142135|rajan.reddy|E45||D|2.4||";
		String e45Corrected = "\"MSH|^~\\\\&|RAIELG-CNFRM|BC00001013|HNCLINET|moh_hnclient_dev|20211109142135|rajan.reddy|E45^^||D|2.4||";
		
		assertEquals(e45Corrected, V2MessageUtil.correctMSH9(e45, "E45"));
	}
	
	@Test
	public void testCorrectMSH9_E45_noCorrectionRequired() {
		String e45 = "\"MSH|^~\\\\&|RAIELG-CNFRM|BC00001013|HNCLINET|moh_hnclient_dev|20211109142135|rajan.reddy|E45^^||D|2.4||";
		
		assertEquals(e45, V2MessageUtil.correctMSH9(e45, "E45"));
	}
	
	@Test
	public void testCorrectMSH9_R15_correctionRequired() {
		String r15 =  "MSH|^~\\&|RAICHK-BNF-CVST|BC00001013|HNWeb|moh_hnclient_dev|20211118181051|train96|R15|20210513182941|D|2.4||";
		String r15Corrected =  "MSH|^~\\&|RAICHK-BNF-CVST|BC00001013|HNWeb|moh_hnclient_dev|20211118181051|train96|R15^^|20210513182941|D|2.4||";
		
		assertEquals(r15Corrected, V2MessageUtil.correctMSH9(r15, "R15"));
	}
	
	@Test
	public void testCorrectMSH9_R15_noCorrectionRequired() {
		String r15 =  "MSH|^~\\&|RAICHK-BNF-CVST|BC00001013|HNWeb|moh_hnclient_dev|20211118181051|train96|R15^^|20210513182941|D|2.4||";
		
		assertEquals(r15, V2MessageUtil.correctMSH9(r15, "R15"));
	}
}
