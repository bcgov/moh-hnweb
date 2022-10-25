package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class UpdateNumberAndDeptResponse extends BaseResponse {
	private String phn;
	private String groupNumber;
	private String groupMemberNumber;
	private String departmentNumber;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getGroupMemberNumber() {
		return groupMemberNumber;
	}

	public void setGroupMemberNumber(String groupMemberNumber) {
		this.groupMemberNumber = groupMemberNumber;
	}

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	@Override
	public String toString() {
		return "UpdateNumberAndDeptResponse [phn=" + phn + ", groupNumber=" + groupNumber + ", groupMemberNumber=" + groupMemberNumber
				+ ", departmentNumber=" + departmentNumber + "]";
	}

}
