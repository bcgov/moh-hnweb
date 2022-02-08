package ca.bc.gov.hlth.hnweb.security;

/**
 * Enum for supported Transaction Types.
 */
public enum TransactionType {
	MSP_COVERAGE_STATUS_CHECK("MSPCoverageCheck"), // E45
	CHECK_ELIGIBILITY("CheckEligibility"), // R15	
	PHN_INQUIRY("PhnInquiry"), // R41
	PHN_LOOKUP("PhnInquiry"), // R42
	ENROLL_SUBSCRIBER("EnrollSubscriber"), // R50
	GET_PERSON_DETAILS("GetPersonDetails"),
	NAME_SEARCH("NameSearch"),
	UNKNOWN("Unknown");

	private String value;

	private TransactionType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
