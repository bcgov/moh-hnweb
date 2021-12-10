package ca.bc.gov.hlth.hnweb.converter.hl7v2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
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
public abstract class BaseV2Converter {
	protected static final String PID_NAMESPACE_ID = "BC";
	protected static final String PID_ID_TYPE_CODE = "PH";
	

	protected static DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
	
	protected static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddkkmmss");
	
	protected String messageDateTime = LocalDateTime.now().format(dateTimeFormatter);
	
	protected MSHDefaults mshDefaults;
	
	protected UserInfo userInfo;
	
	protected abstract String getReceivingApplication();
	
	protected abstract String getMessageType();

	public BaseV2Converter(MSHDefaults mshDefaults) {
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
	
	protected void populatePID(PID pid, String phn, LocalDate dateOfBirth, String gender) throws HL7Exception {
		V2MessageUtil.setPidValues(pid, phn, PID_NAMESPACE_ID, PID_ID_TYPE_CODE, "", dateOnlyFormatter.format(dateOfBirth), gender);
	}
	
	protected void populateIN1(IN1 in1, LocalDate planEffectiveDate) throws HL7Exception {
		V2MessageUtil.setIn1Values(in1, null, null, null, dateOnlyFormatter.format(planEffectiveDate), null);
	}
	
	protected void populateIN1(IN1 in1, LocalDate planEffectiveDate, LocalDate planCancellationDate, String groupNumber, String groupMemebrNumber, String immigrationCode ) throws HL7Exception {
		V2MessageUtil.setIn1Values(in1, groupNumber, groupMemebrNumber, immigrationCode, dateOnlyFormatter.format(planEffectiveDate), dateOnlyFormatter.format(planCancellationDate));
	}
	
	
	
}
