package ca.bc.gov.hlth.hnweb.util;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.v2.segment.HDR;
import ca.bc.gov.hlth.hnweb.model.v2.segment.QPD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.SFT;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIH;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIK;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Version;
import ca.uhn.hl7v2.model.v24.segment.IN1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.RCP;

/**
 * Contains utility methods related to HL7 V2 messages
 *
 */
public class V2MessageUtil {

	public static final String FIELD_SEPARATOR = "|";

	public static final String ENCODING_CHARACTERS = "^~\\&";
	
	public static final String DATE_FORMAT_DATE_ONLY = "yyyyMMdd";
	
	public static final String  DEFAULT_VERSION_ID = "2.4";

	private static final String VISA_ISSUE = "VISA_ISSUE";
    
	private static final String VISA_XPIRY = "VISA_XPIRY";
	
	private static final String QCP_QUERY_TAG = "1";
	
	private static final String QPD_COVERAGE_INQUIRY_ENDRSN = "ENDRSN^^HNET9909";
	private static final String QPD_COVERAGE_INQUIRY_CCARD = "CCARD^^HNET9909";
	private static final String QPD_COVERAGE_INQUIRY_PVC = "PVC^^HNET9909";
	private static final String QPD_COVERAGE_INQUIRY_EYE = "EYE^^HNET9909";
	private static final String QPD_COVERAGE_INQUIRY_PRS = "PRS^^HNET9909";
	
	private static final String QPD_PATIENT_ASSIGNING_AUTHORITY = "CANBC";
	private static final String QPD_PATIENT_ID_TYPE_CODE = "JHN";
	private static final String QPD_PATIENT_ASSIGNING_FACILITY = "MOH";		
	private static final String QPD_PAYOR_ORGANIZATION_ID = "00000754";

	private static final String RCP_QUERY_PRIORITY = "I";
	
	private static final String SFT_SOFTWARE_VERSION_NUMBER = "1.0";
	
	private static final String ORGANIZATION_FORMAT = "^^%s^^^CANBC^XX^MOH";

	public enum MessageType {
		R15, E45, R50; 	//HIBC
	}
	
	public enum SegmentType {
		MSH, PID, QPD, ADJ
	}
	
	public enum AddressType {
		H, M;
	}
	
	public enum TelePhoneUseCode {
		PRN, ORN, WPN, EMR;
	}

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
    								String messageControlID, String processingID) throws HL7Exception {
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
		msh.getMsh12_VersionID().parse(Version.V24.getVersion());
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
    public static void setPidValues(PID pid, String patientId, String patientIdAssigningAuthority, String patientIdIdentifierTypeCode, 
    		String alternatePatientIdPid, String dateTimeOfBirth, String administrativeSex) throws HL7Exception {
    	//e.g. PID||9999999999^^^BC^PH|||||19700303|M^M
    	if(!StringUtils.isEmpty(patientId)) {
    		pid.getPid2_PatientID().getCx1_ID().parse(patientId);
    		pid.getPid2_PatientID().getCx4_AssigningAuthority().parse(patientIdAssigningAuthority);
    		pid.getPid2_PatientID().getCx5_IdentifierTypeCode().parse(patientIdIdentifierTypeCode);
//    		Pid3_PatientIdentifierList Not Supported     
    		pid.getPid4_AlternatePatientIDPID(0).parse(alternatePatientIdPid);
//    		Pid5 PatientName Not Supported 
    	}
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
    public static void setZiaValues(ZIA zia, String bcResidencyDate, String surname, String firstGivenName, String secondGivenName,  String telephone, String immigrationOrVisaCode, String priorResidenceCode) throws HL7Exception {
    	//e.g. ZIA||20210101|||||||||||||HELP^RERE^^^^^L|898 RETER ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^H~123 UIYUI ST^^^^^^^^^^^^^^^^^^^VICTORIA^BC^V8V8V8^^M|^PRN^PH^^^250^8578974|||||||S|AB^M
    	String givenName  = firstGivenName +  secondGivenName;
    	String areaCode = null;
    	String phoneNumber = null;  	
    	
    	zia.getZia2_BCResidencyDate().parse(bcResidencyDate);
    	zia.getZia15_ExtendedPersonName().parse(surname);   	
    	zia.getZia15_ExtendedPersonName().getGivenName().parse(givenName);
    	zia.getZia15_ExtendedPersonName().getNameTypeCode().parse("L");;
    
    	if(StringUtils.isNotBlank(telephone)) {   		
    		areaCode = telephone.substring(0,3);
    		phoneNumber = telephone.substring(3);
        	
    		zia.getZia17_ExtendedTelephoneNumber().getAreaCityCode().parse(areaCode);
    		zia.getZia17_ExtendedTelephoneNumber().getTelecommunicationEquipmentType().parse("PH");
    	
    		zia.getZia17_ExtendedTelephoneNumber().getPhoneNumber().parse(phoneNumber);
    		zia.getZia17_ExtendedTelephoneNumber().getTelecommunicationUseCode().parse(TelePhoneUseCode.PRN.name());
    	}
    	
    	zia.getZia24_ImmigrationOrVisaCode().parse(immigrationOrVisaCode);
    	zia.getZia25_PriorResidenceCode().parse(priorResidenceCode);
    }
    
    public static void setZiaExtendedAddrees1(ZIA zia, String addressLine1, String addressLine2, String addressLine3, String city, String province, String postalCode) throws HL7Exception {

    	zia.getZia16_ExtendedAddress(0).getZAD1_AddressLine1().parse(addressLine1);
    	zia.getZia16_ExtendedAddress(0).getZAD2_AddressLine2().parse(addressLine2);  
    	zia.getZia16_ExtendedAddress(0).getZAD3_AddressLine3().parse(addressLine3);  
    	zia.getZia16_ExtendedAddress(0).getZAD20_City().parse(city);
    	zia.getZia16_ExtendedAddress(0).getZAD21_Province().parse(province);
    	zia.getZia16_ExtendedAddress(0).getZAD22_PostalCode().parse(postalCode);
    	zia.getExtendedAddress(0).getZAD24_AddressType().parse(AddressType.H.name());
    }
    
    public static void setZiaExtendedAddrees2(ZIA zia, String mailingAddressLine1,  String mailingAddressLine2,  String mailingAddressLine3, String city, String province, String postalCode) throws HL7Exception {

    	zia.getZia16_ExtendedAddress(1).getZAD1_AddressLine1().parse(mailingAddressLine1);
    	zia.getZia16_ExtendedAddress(1).getZAD2_AddressLine2().parse(mailingAddressLine2);
    	zia.getZia16_ExtendedAddress(1).getZAD3_AddressLine3().parse(mailingAddressLine3);
    	zia.getZia16_ExtendedAddress(1).getZAD20_City().parse(city);
    	zia.getZia16_ExtendedAddress(1).getZAD21_Province().parse(province);
    	zia.getZia16_ExtendedAddress(1).getZAD22_PostalCode().parse(postalCode);
    	zia.getExtendedAddress(1).getZAD24_AddressType().parse(AddressType.M.name());
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
	public static void setSftValues(SFT sft) throws HL7Exception {
		// e.g. SFT|1.0||testorg^^orgid^^^MOH|1.0|barebones||
		// For E45 only SFT-4 is supported
		sft.getSFT4_SoftwareVersionNumber().parse(SFT_SOFTWARE_VERSION_NUMBER);		
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
	public static void setQpdValues(QPD qpd, String messageQueryName,
			String phn, String submittingAndProviderOrganization, String dateOfBirth, String dateOfService,
			Boolean checkSubsidyInsuredService, Boolean checkLastEyeExam, Boolean checkPatientRestriction) throws HL7Exception {

		// e.g. QPD|E45^^HNET0003|1|^^00000001^^^CANBC^XX^MOH|^^00000001^^^CANBC^XX^MOH|^^00000754^^^CANBC^XX^MOH|9020198746^^^CANBC^JHN^MOH||19421112||||||19980601||PVC^^HNET9909||
		qpd.getQpd1_MessageQueryName().parse(messageQueryName);
		qpd.getQpd2_QueryTag().parse(QCP_QUERY_TAG);

		qpd.getQpd3_SubmittingOrganization().parse(String.format(ORGANIZATION_FORMAT, submittingAndProviderOrganization));
		qpd.getQpd4_ProviderOrganization().parse(String.format(ORGANIZATION_FORMAT, submittingAndProviderOrganization));
		qpd.getQpd5_PayorOrganization().parse(String.format(ORGANIZATION_FORMAT, QPD_PAYOR_ORGANIZATION_ID));

		qpd.getQpd6_PatientIdentifierList().getCx1_ID().parse(phn); 
		qpd.getQpd6_PatientIdentifierList().getCx4_AssigningAuthority().parse(QPD_PATIENT_ASSIGNING_AUTHORITY);
		qpd.getQpd6_PatientIdentifierList().getCx5_IdentifierTypeCode().parse(QPD_PATIENT_ID_TYPE_CODE);
		qpd.getQpd6_PatientIdentifierList().getCx6_AssigningFacility().parse(QPD_PATIENT_ASSIGNING_FACILITY);

		qpd.getQpd8_DateTimeOfBirth().parse(dateOfBirth);
		qpd.getQpd14_ServiceEffectiveDate().parse(dateOfService);

		// Build the repeating Coverage Inquiry Code field entries
		qpd.getQpd16_CoverageInquiryCode(0).parse(QPD_COVERAGE_INQUIRY_ENDRSN);
		qpd.getQpd16_CoverageInquiryCode(1).parse(QPD_COVERAGE_INQUIRY_CCARD);
		if (Boolean.TRUE.equals(checkSubsidyInsuredService)) {
			qpd.getQpd16_CoverageInquiryCode(2).parse(QPD_COVERAGE_INQUIRY_PVC);
		}
		if (Boolean.TRUE.equals(checkLastEyeExam)) {
			qpd.getQpd16_CoverageInquiryCode(qpd.getCoverageInquiryCodeReps()).parse(QPD_COVERAGE_INQUIRY_EYE);
		}
		if (Boolean.TRUE.equals(checkPatientRestriction)) {
			qpd.getQpd16_CoverageInquiryCode(qpd.getCoverageInquiryCodeReps()).parse(QPD_COVERAGE_INQUIRY_PRS);
		}
		
	}

	/**
	 * Populate RCP segment
	 * 
	 * @param rcp
	 * @param queryPriority
	 * @throws HL7Exception
	 */
	public static void setRcpValues(RCP rcp) throws HL7Exception {
		// e.g. RCP|I|
		rcp.getRcp1_QueryPriority().parse(RCP_QUERY_PRIORITY);
	}
	
	public static String correctMSH9(String v2, String messageType) {
		// XXX This is to fix the following parsing issue: Can't determine message structure from MSH-9: R15 HINT: there are only 1 of 3 components present]
    	return v2.replaceAll("\\|" + messageType + "\\|", "|" + messageType + "^^|");
	}

}
