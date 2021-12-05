package ca.bc.gov.hlth.hnweb.model.v3;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageMetaData {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	private String dataEntererRoot = "2.16.840.1.113883.3.51.1.1.7";
	private String dataEntererExt = "";
	private String messageIdRoot = "2.16.840.1.113883.3.51.1.1.1";
	private String messageIdExt;
	private String timestamp;
	private String expiryTime;
	private String sourceSystemOverride;
	private String organization;

	public MessageMetaData(String transactionId) {
		timestamp = sdf.format(new Date());
		Date expiry = new Date();
		expiry.setDate(expiry.getDate() + 1);
		expiryTime = sdf.format(expiry);
		messageIdExt = transactionId;
	}

	public MessageMetaData(String dataEntererExt, String sourceSystemOverride, String organization, String transactionId) {
		this(transactionId);
		this.dataEntererExt = dataEntererExt;
		this.sourceSystemOverride = sourceSystemOverride;
		this.organization = organization;
	}

	public void updateMessageId() {
		timestamp = sdf.format(new Date());
		messageIdExt = "HOOPC" + timestamp;
	}

	public String getDataEntererExt() {
		return dataEntererExt;
	}

	public void setDataEntererExt(String dataEntererExt) {
		this.dataEntererExt = dataEntererExt;
	}

	public String getDataEntererRoot() {
		return dataEntererRoot;
	}

	public void setDataEntererRoot(String dataEntererRoot) {
		this.dataEntererRoot = dataEntererRoot;
	}

	public String getMessageIdExt() {
		return messageIdExt;
	}

	public void setMessageIdExt(String messageIdExt) {
		this.messageIdExt = messageIdExt;
	}

	public String getMessageIdRoot() {
		return messageIdRoot;
	}

	public void setMessageIdRoot(String messageIdRoot) {
		this.messageIdRoot = messageIdRoot;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getSourceSystemOverride() {
		return sourceSystemOverride;
	}

	public void setSourceSystemOverride(String sourceSystemOverride) {
		this.sourceSystemOverride = sourceSystemOverride;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
}