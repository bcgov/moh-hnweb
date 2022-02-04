package ca.bc.gov.hlth.hnweb.security;

/**
 * Enum for supported Transaction Types.
 */
public enum TransactionType {
	CHECK_ELIGIBILITY("Check Eligibility");

	private String value;

	private TransactionType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
