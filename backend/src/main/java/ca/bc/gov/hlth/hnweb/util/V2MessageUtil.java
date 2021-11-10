package ca.bc.gov.hlth.hnweb.util;

import java.util.Date;

import ca.bc.gov.hlth.hnweb.model.v2.segment.HDR;
import ca.bc.gov.hlth.hnweb.model.v2.segment.SFT;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIH;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIK;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CN;
import ca.uhn.hl7v2.model.v24.segment.IN1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.model.v24.segment.RCP;

/**
 * Contains utility methods related to HL7 V2 messages
 *
 */
public class V2MessageUtil {

	private static final String VISA_ISSUE = "VISA_ISSUE";
    private static final String VISA_XPIRY = "VISA_XPIRY";

    /**
     * Populate MSH segment
     * 
     * @param msh
     * @param sendingApplication
     * @param sendingFacility
     * @param receivingApplication
     * @param receivingFacility
     * @param dateTimeOfMessage
     * @param security
     * @param messageControlID
     * @param processingID
     * @param versionID
     * @param messageType 
     * @throws HL7Exception
     */
	public static void setMshValues(MSH msh, String sendingApplication, String sendingFacility, String receivingApplication, 
    								String receivingFacility, String dateTimeOfMessage, String security, String messageType,  
    								String messageControlID, String processingID, String versionID) throws HL7Exception {
    	//e.g. MSH|^~\&|HNWeb|BC01000030|RAIENROL-EMP|BC00001013|20200529114230|10-ANother|R50^Z06|20200529114230|D|2.4||^M

    	msh.getMsh3_SendingApplication().parse(sendingApplication);
		msh.getMsh4_SendingFacility().parse(sendingFacility);
		msh.getMsh5_ReceivingApplication().parse(receivingApplication);
		msh.getMsh6_ReceivingFacility().parse(receivingFacility);
		msh.getMsh7_DateTimeOfMessage().parse(dateTimeOfMessage);
		msh.getMsh8_Security().parse(security);
		msh.getMsh9_MessageType().parse(messageType);
		msh.getMsh10_MessageControlID().parse(messageControlID);
		msh.getMsh11_ProcessingID().parse(processingID);
		msh.getMsh12_VersionID().parse(versionID);
    }

	/**
	 * Populate ZHD segment
	 * 
	 * @param zhd
	 * @param eventDateTime
	 * @param organizationIdNumber
	 * @param userGroup
	 * @param softwareVersionNumber
	 * @throws HL7Exception
	 */
    public static void setZhdValues(ZHD zhd, String eventDateTime, String organizationIdNumber, 
    		String userGroup, String softwareVersionNumber) throws HL7Exception {
    	//e.g. ZHD|20200529114230|^^00000010|HNAIADMINISTRATION||||2.4^M
    	
    	zhd.getZHD1_EventDateTime().parse(eventDateTime);
    	zhd.getZHD2_Organization().getIDNumber().parse(organizationIdNumber);
    	zhd.getZHD3_UserGroup().parse(userGroup);
    	zhd.getZHD7_SoftwareVersionNumber().parse(softwareVersionNumber);
    }

    /**
     * Populate PID segment
	 * 
     * @param pid
     * @param patientIdId
     * @param patientIdAssigningAuthority
     * @param patientIdIdentifierTypeCode
     * @param alternatePatientIdPid
     * @param dateTimeOfBirth
     * @param administrativeSex
     * @throws HL7Exception
     */
    public static void setPidValues(PID pid, String patientIdId, String patientIdAssigningAuthority, String patientIdIdentifierTypeCode, 
    		String alternatePatientIdPid, String dateTimeOfBirth, String administrativeSex) throws HL7Exception {
    	//e.g. PID||9999999999^^^BC^PH|||||19700303|M^M
    	
    	pid.getPid2_PatientID().getCx1_ID().parse(patientIdId);
    	pid.getPid2_PatientID().getCx4_AssigningAuthority().parse(patientIdAssigningAuthority);
    	pid.getPid2_PatientID().getCx5_IdentifierTypeCode().parse(patientIdIdentifierTypeCode);
//    	Pid3_PatientIdentifierList Not Supported     
    	pid.getPid4_AlternatePatientIDPID(0).parse(alternatePatientIdPid);
//    	Pid5 PatientName Not Supported 
    	pid.getPid7_DateTimeOfBirth().parse(dateTimeOfBirth);
    	pid.getPid8_AdministrativeSex().parse(administrativeSex);
    }
    
    /**
     * Populate ZIA segment
	 * 
     * @param zia
     * @param bcResidencyDate
     * @param extendedPersonName
     * @param extendedAddress1
     * @param extendedAddress2
     * @param extendedTelephoneNumber
     * @param immigrationOrVisaCode
     * @param priorResidenceCode
     * @throws HL7Exception
     */
    public static void setZiaValues(ZIA zia, String bcResidencyDate, String extendedPersonName, 
    							String extendedAddress1, String extendedAddress2, 
    							String extendedTelephoneNumber, String immigrationOrVisaCode, String priorResidenceCode) throws HL7Exception {
    	//e.g. ZIA||20210101|||||||||||||HELP^RERE^^^^^L|898 RETER ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^H~123 UIYUI ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^M|^PRN^PH^^^250^8578974|||||||S|AB^M
    	
    	zia.getZia2_BCResidencyDate().parse(bcResidencyDate);
    	zia.getZia15_ExtendedPersonName().parse(extendedPersonName);
    	zia.getZia16_ExtendedAddress(0).parse(extendedAddress1);
    	zia.getZia16_ExtendedAddress(1).parse(extendedAddress2);
    	zia.getZia17_ExtendedTelephoneNumber().parse(extendedTelephoneNumber);
    	zia.getZia24_ImmigrationOrVisaCode().parse(immigrationOrVisaCode);
    	zia.getZia25_PriorResidenceCode().parse(priorResidenceCode);
    }

    /**
     * Populate IN1 segment
	 * 
     * @param in1
     * @param groupNumber
     * @param insuredSGroupEmpID
     * @param insuredSGroupEmpNameIdNumber
     * @param planEffectiveDate
     * @param planExpirationDate
     * @throws HL7Exception
     */
    public static void setIn1Values(IN1 in1, String groupNumber, String insuredSGroupEmpID, String insuredSGroupEmpNameIdNumber, 
    								String planEffectiveDate, String planExpirationDate) throws HL7Exception {
    	//e.g. IN1||||||||6337109||789446|^^123456|20190501|20201231^M
    	
    	in1.getIn18_GroupNumber().parse(groupNumber);
    	in1.getIn110_InsuredSGroupEmpID(0).parse(insuredSGroupEmpID);
    	in1.getIn111_InsuredSGroupEmpName(0).getXon3_IDNumber().parse(insuredSGroupEmpNameIdNumber);
    	in1.getIn112_PlanEffectiveDate().parse(planEffectiveDate);
    	in1.getIn113_PlanExpirationDate().parse(planExpirationDate);
    }

    /**
     * Populate ZIH segment
	 * 
     * @param zih
     * @param payerCancelReason
     * @throws HL7Exception
     */
    public static void setZihValues(ZIH zih, String payerCancelReason) throws HL7Exception {
    	//e.g. ZIH|||||||||||||||||||D^M
    	
    	zih.getZih19_PayerCancelReason().parse(payerCancelReason);
    }

    /**
     * Populate ZIK segment
	 * 
     * @param zik
     * @param documentArgumentValue1
     * @param documentArgumentValue2
     * @throws HL7Exception
     */
    public static void setZikValues(ZIK zik, String documentArgumentValue1, String documentArgumentValue2) throws HL7Exception {
		//e.g. ZIK||||VISA_ISSUE^20210101~VISA_XPIRY^20221231
    	
    	//Set the individual values and the two repetitions of the repeating ZRG type.
    	zik.getZik4_DocumentArgument(0).getArgumentName().parse(VISA_ISSUE);
    	zik.getZik4_DocumentArgument(0).getArgumentValue().parse(documentArgumentValue1);
    	zik.getZik4_DocumentArgument(1).getArgumentName().parse(VISA_XPIRY);
    	zik.getZik4_DocumentArgument(1).getArgumentValue().parse(documentArgumentValue2);
    }

    /**
     * Populate HDR segment
	 * 
     * @param hdr
     * 
     * @throws HL7Exception
     */
	public static void setHdrValues(HDR hdr, String businessUserGroup) throws HL7Exception {
		//e.g. HDR|||TRAININGAdmin
    	
    	hdr.getHDR3_BusinessUserGroup().parse(businessUserGroup);
    }

	/**
	 * Populate SFT segment
	 * 
	 * @param sft
	 * @param standardVersionNumber
	 * @param softwareVendorOrganizationOrganizationName
	 * @param softwareVendorOrganizationIDNumber
	 * @param softwareVendorOrganizationAssigningAuthority
	 * @param softwareVersionNumber
	 * @param softwareProductName
	 * @throws HL7Exception
	 */
	public static void setSftValues(SFT sft, String standardVersionNumber, 
			String softwareVendorOrganizationOrganizationName, String softwareVendorOrganizationIDNumber, String softwareVendorOrganizationAssigningAuthority, 
			String softwareVersionNumber, String softwareProductName) throws HL7Exception {
		// e.g. SFT|1.0||testorg^^orgid^^^MOH|1.0|barebones||
		
		sft.getSFT1_StandardVersionNumber().parse(standardVersionNumber);
		sft.getSFT3_SoftwareVendorOrganization().getXon1_OrganizationName().parse(softwareVendorOrganizationOrganizationName);
		sft.getSFT3_SoftwareVendorOrganization().getXon3_IDNumber().parse(softwareVendorOrganizationIDNumber);
		sft.getSFT3_SoftwareVendorOrganization().getXon6_AssigningAuthority().parse(softwareVendorOrganizationAssigningAuthority);
		sft.getSFT4_SoftwareVersionNumber().parse(softwareVersionNumber);
		sft.getSFT5_SoftwareProductName().parse(softwareProductName);		
	}

	/**
     * Populate QPD segment
	 * 
	 * @param qpd
	 * @param messageQueryName
	 * @param queryTag
	 * @param phn
	 * @param dateOfBirth
	 * @param dateOfService
	 * @param checkSubsidyInsuredService
	 * @param checkLastEyeExam
	 * @param checkPatientRestriction
	 * @throws HL7Exception
	 */
	public static void setQpdValues(QPD qpd, String messageQueryName, String queryTag,
			String phn, Date dateOfBirth, Date dateOfService,
			Boolean checkSubsidyInsuredService, Boolean checkLastEyeExam, Boolean checkPatientRestriction) throws HL7Exception {
		
		qpd.getQpd1_MessageQueryName().parse(messageQueryName);
		qpd.getQpd2_QueryTag().parse(queryTag);
		
		CN cn = new CN(qpd.getMessage());
		Varies qpd3_UserParametersInsuccessivefields = qpd.getQpd3_UserParametersInsuccessivefields();
		qpd3_UserParametersInsuccessivefields.setData(cn);
		
	}

	/**
	 * Populate RCP segment
	 * 
	 * @param rcp
	 * @param queryPriority
	 * @throws HL7Exception
	 */
	public static void setRcpValues(RCP rcp, String queryPriority) throws HL7Exception {
		//e.g. RCP|I|
		
		rcp.getRcp1_QueryPriority().parse(queryPriority);
	}

}
