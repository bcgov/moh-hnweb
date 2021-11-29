package ca.bc.gov.hlth.hnweb.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ca.bc.gov.hlth.hnweb.model.Address;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.Name;
import ca.bc.gov.hlth.hnweb.model.Telecommunication;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIH;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIK;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.segment.IN1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;

/**
 * Abstract base class providing common functionality to any V2 message converters.
 * @author wesley.kubo
 *
 */
public abstract class BaseConverter {
	private static final String PID_NAMESPACE_ID = "BC";
	private static final String PID_ID_TYPE_CODE = "PH";
	private static final String ZIH_COVERAGE_CAN_REASON = "D";

	protected static DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
	
	protected static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddkkmmss");
	
	protected String messageDateTime = LocalDateTime.now().format(dateTimeFormatter);
	
	protected MSHDefaults mshDefaults;
	
	protected UserInfo userInfo;
	
	protected abstract String getReceivingApplication();
	
	protected abstract String getMessageType();

	public BaseConverter(MSHDefaults mshDefaults) {
		super();
		this.mshDefaults = mshDefaults;
		this.userInfo = SecurityUtil.loadUserInfo();
	}

	protected void populateMSH(MSH msh) throws HL7Exception {
		V2MessageUtil.setMshValues(msh, mshDefaults.getSendingApplication(), mshDefaults.getSendingFacility(), getReceivingApplication(), 
				mshDefaults.getReceivingFacility(), messageDateTime, userInfo.getUsername(), getMessageType(), messageDateTime, mshDefaults.getProcessingID());
	}
	
	protected void populateZHD(ZHD zhd) throws HL7Exception {
		V2MessageUtil.setZhdValues(zhd, messageDateTime, userInfo.getOrganization(), userInfo.getRole(), V2MessageUtil.DEFAULT_VERSION_ID);
	}
	
	protected void populatePID(PID pid, String phn) throws HL7Exception {
		V2MessageUtil.setPidValues(pid, phn, PID_NAMESPACE_ID, PID_ID_TYPE_CODE, "", "", "");
	}
	
	protected void populateIN1(IN1 in1, LocalDate planEffectiveDate) throws HL7Exception {
		V2MessageUtil.setIn1Values(in1, null, null, null, dateOnlyFormatter.format(planEffectiveDate), null);
	}
	
	protected void populateZIA(ZIA zia, LocalDate bcResidencyDate, String familyName, String name, String areaCode, String telephone, String immigrationOrVisaCode, String priorResidenceCode) throws HL7Exception {
		
		V2MessageUtil.setZiaValues(zia, dateOnlyFormatter.format(bcResidencyDate), familyName, name,  areaCode, telephone, immigrationOrVisaCode, priorResidenceCode);
	}
	
	protected void populateZIAExtendedAddress1(ZIA zia, String addressLine1, String addressLine2, String addressLine3, String addressLine4,String city, String province, String postalCode) throws HL7Exception {
		
		V2MessageUtil.setZiaExtendedAddrees1(zia, addressLine1, city, province, postalCode);
	}
	
	protected void populateZIAExtendedAddress2(ZIA zia, String addressLine1, String addressLine2, String addressLine3, String addressLine4,String city, String province, String postalCode) throws HL7Exception {
		
		V2MessageUtil.setZiaExtendedAddrees2(zia, addressLine1, city, province, postalCode);
	}
	
	protected void populateZIH(ZIH zih) throws HL7Exception {
		V2MessageUtil.setZihValues(zih, ZIH_COVERAGE_CAN_REASON);
	}
	
	protected void populateZIK(ZIK zik, LocalDate documentArgumentValue1, LocalDate documentArgumentValue2) throws HL7Exception {
		V2MessageUtil.setZikValues(zik, dateOnlyFormatter.format(documentArgumentValue1), dateOnlyFormatter.format(documentArgumentValue2));
	}
	
}
