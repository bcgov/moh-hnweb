package ca.bc.gov.hlth.hnweb.persistence.entity;

public enum TransactionEventType {

	ERROR("ERROR"),
	WARNING("WARNING"),
	MESSAGE_RECEIVED("Message Received"),
	MESSAGE_SENT("Message Sent"),
	TRANSACTION_COMPLETE("Transaction Complete"),
	TRANSACTION_START("Transaction Start"),
	UNAUTHORIZED("Unauthorized");

	private String value;

	private TransactionEventType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}

