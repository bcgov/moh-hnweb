package ca.bc.gov.hlth.hnweb.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;

import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.segment.IN1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;

public abstract class BaseConverter {
	private static final String PID_NAMESPACE_ID = "BC";
	private static final String PID_ID_TYPE_CODE = "PH";
	
	@Value("${v2.msh.processingID}")
	private String mshProcessingID;

	@Value("${v2.msh.receivingFacility}")
	private String mshReceivingFacility;
	
	@Value("${v2.msh.security}")
	private String mshSecurity;	
	
	@Value("${v2.msh.sendingApplication}")
	private String mshSendingApplication;
	
	@Value("${v2.msh.sendingFacility}")
	private String mshSendingFacility;

	@Value("${v2.zhd.businessOrganization}")
	private String zhdBusinessOrganization;
	
	@Value("${v2.zhd.businessUserGroup}")
	private String zhdBusinessUserGroup;

	protected static DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
	
	protected static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddkkmmss");
	
	protected String messageDateTime = LocalDateTime.now().format(dateTimeFormatter);
	
	protected abstract String getReceivingApplication();
	
	protected abstract String getMessageType();
	
	protected void populateMSH(MSH msh) throws HL7Exception {
		V2MessageUtil.setMshValues(msh, mshSendingApplication, mshSendingFacility, getReceivingApplication(), mshReceivingFacility, messageDateTime, mshSecurity, getMessageType(), messageDateTime, mshProcessingID);
	}
	
	protected void populateZHD(ZHD zhd) throws HL7Exception {
		V2MessageUtil.setZhdValues(zhd, messageDateTime, zhdBusinessOrganization, zhdBusinessUserGroup, V2MessageUtil.DEFAULT_VERSION_ID);
	}
	
	protected void populatePID(PID pid, String phn) throws HL7Exception {
		V2MessageUtil.setPidValues(pid, phn, PID_NAMESPACE_ID, PID_ID_TYPE_CODE, "", "", "");
	}
	
	protected void populateIN1(IN1 in1, LocalDate planEffectiveDate) throws HL7Exception {
		V2MessageUtil.setIn1Values(in1, null, null, null, dateOnlyFormatter.format(planEffectiveDate), null);
	}
}
