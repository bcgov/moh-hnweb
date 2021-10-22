package ca.bc.gov.hlth.hnweb.model;

/**
 * Response message for API call to enroll a subscriber 
 *
 */
public class EnrollSubscriberResponse {

	private String messageId;

	private String acknowledgementCode;
	
	private String acknowledgementMessage;
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getAcknowledgementCode() {
		return acknowledgementCode;
	}

	public void setAcknowledgementCode(String acknowledgementCode) {
		this.acknowledgementCode = acknowledgementCode;
	}

	public String getAcknowledgementMessage() {
		return acknowledgementMessage;
	}

	public void setAcknowledgementMessage(String acknowledgementMessage) {
		this.acknowledgementMessage = acknowledgementMessage;
	}
	
	
}
