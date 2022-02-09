package ca.bc.gov.hlth.hnweb.persistence.entity;

public enum IdentifierType {
	PHN("PHN"),
	GROUP_NUMBER("Group Number"),
	GROUP_MEMBER_NUMBER("Group Member Number"),
	CONTRACT_NUMBER("Contract Number"),
	DEPARTMENT_NUMBER("Department Number");

	private String value;

	private IdentifierType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
