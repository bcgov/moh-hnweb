package ca.bc.gov.hlth.hnweb.security;

/**
 * Enum for supported Transaction Types.
 */
public enum TransactionType {
	MSP_COVERAGE_STATUS_CHECK("MSPCoverageCheck"), // E45
	CHECK_ELIGIBILITY("CheckEligibility"), // R15	
	PHN_INQUIRY("PHNInquiry"), // R41
	PHN_LOOKUP("PHNLookup"), // R42
	ENROLL_SUBSCRIBER("EnrollSubscriber"), // R50
	GET_PERSON_DETAILS("GetPersonDetails"),
	NAME_SEARCH("NameSearch"),
	ADD_GROUP_MEMBER("AddGroupMember"),
	ADD_DEPENDENT("AddDependent"),
	UPDATE_NUMBER_AND_OR_DEPT("UpdateNumberAndDept"),
	CANCEL_GROUP_MEMBER("CancelGroupMember"),
	CANCEL_DEPENDENT("CancelDependent"),
	GET_CONTRACT_PERIODS("GetContractPeriods"),
	CONTRACT_INQUIRY("ContractInquiry"),	//R40
	GET_CONTRACT_ADDRESS("GetContractAddress"), //R37
	UPDATE_CONTRACT_ADDRESS("UpdateContractAddress"), //R38
	CHANGE_EFFECTIVE_DATE("ChangeEffectiveDate"), //R46a
	
	UNKNOWN("Unknown");

	private String value;

	private TransactionType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
