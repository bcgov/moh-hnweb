package ca.bc.gov.hlth.hnweb.converter.hl7v2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.segment.IN1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.util.Terser;

/**
 * Abstract base class providing common functionality to any V2 message converters.
 * @author wesley.kubo
 *
 */
public abstract class BaseV2Converter {
	private static final Logger logger = LoggerFactory.getLogger(BaseV2Converter.class);

	protected static final String PID_NAMESPACE_ID = "BC";
	
	protected static final String PID_ID_TYPE_CODE = "PH";
	
	protected static final String SUCCESS_MESSAGE = "TRANSACTION COMPLETED";

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
	
	protected void populateMSH(MSH msh, String messageId) throws HL7Exception {
		V2MessageUtil.setMshValues(msh, mshDefaults.getSendingApplication(), mshDefaults.getSendingFacility(), getReceivingApplication(), 
				mshDefaults.getReceivingFacility(), messageDateTime, userInfo.getUsername(), getMessageType(), StringUtils.substring(messageId, 0, 20), mshDefaults.getProcessingID());
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
	
	protected void populateIN1(IN1 in1, LocalDate planEffectiveDate, LocalDate planCancellationDate, String groupNumber, String groupMemebrNumber, String departmentNumber ) throws HL7Exception {
		V2MessageUtil.setIn1Values(in1, groupNumber, groupMemebrNumber, departmentNumber, dateOnlyFormatter.format(planEffectiveDate), dateOnlyFormatter.format(planCancellationDate));
	}

	protected void mapErrorValues(Terser terser, BaseResponse response) throws HL7Exception {
		/**
		 * When checking for an error the MSA segment needs to be checked as even a success message has an ERR segment e.g.
		 * 
		 * MSA|AA||HJMB001ISUCCESSFULLY COMPLETED
		 * ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED
		 * 
		 * So it needs to checked if there was actually an error.
		 * e.g.
		 * 
		 * MSA|AE|20211108170321|ELIG0001DATE OF SERVICE EXCEEDS SYSTEM LIMITS. MUST BE WITHIN THE LAST 18 MONTHS. CONTACT MSP.
		 * ERR|^^^ELIG0001&DATE OF SERVICE EXCEEDS SYSTEM LIMITS. MUST BE WITHIN THE LAST 18 MONTHS. CONTACT MSP.
		 */
		String msaAcknowledgementCode = terser.get("/.MSA-1-1");
		StatusEnum status = StringUtils.equals(msaAcknowledgementCode, "AA") ? StatusEnum.SUCCESS : StatusEnum.ERROR;

		String statusCode = terser.get("/.ERR-1-4-1");
		String statusText = terser.get("/.ERR-1-4-2");
		
		logger.debug("Acknowledgement code {} received in response. Status code: {}, Status message: {}", msaAcknowledgementCode, statusCode, statusText);

		response.setMessage(String.format("%s %s", statusCode, overrideSuccessMessageText(status, statusText)));
		response.setStatus(status);
	}

	private String overrideSuccessMessageText(StatusEnum status, String statusText) {
		return status == StatusEnum.SUCCESS ? SUCCESS_MESSAGE : statusText;
	}

}
