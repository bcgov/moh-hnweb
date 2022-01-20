package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import java.time.LocalDate;

public class AddGroupMemberRequest {
	private String groupNumber;
	private LocalDate effectiveDate;
	private String phn;
	private String groupMemberNumber;
	private String departmentNumber;
	private String phone;
	private MemberAddress homeAddress;
	private MemberAddress mailingAddress;

	private String spousePhn;
	private String dependentPhn1;
	private String dependentPhn2;
	private String dependentPhn3;
	private String dependentPhn4;
	private String dependentPhn5;
	private String dependentPhn6;
	private String dependentPhn7;

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public MemberAddress getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(MemberAddress homeAddress) {
		this.homeAddress = homeAddress;
	}

	public MemberAddress getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(MemberAddress mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public String getSpousePhn() {
		return spousePhn;
	}

	public void setSpousePhn(String spousePhn) {
		this.spousePhn = spousePhn;
	}

	public String getDependentPhn1() {
		return dependentPhn1;
	}

	public void setDependentPhn1(String dependentPhn1) {
		this.dependentPhn1 = dependentPhn1;
	}

	public String getDependentPhn2() {
		return dependentPhn2;
	}

	public void setDependentPhn2(String dependentPhn2) {
		this.dependentPhn2 = dependentPhn2;
	}

	public String getDependentPhn3() {
		return dependentPhn3;
	}

	public void setDependentPhn3(String dependentPhn3) {
		this.dependentPhn3 = dependentPhn3;
	}

	public String getDependentPhn4() {
		return dependentPhn4;
	}

	public void setDependentPhn4(String dependentPhn4) {
		this.dependentPhn4 = dependentPhn4;
	}

	public String getDependentPhn5() {
		return dependentPhn5;
	}

	public void setDependentPhn5(String dependentPhn5) {
		this.dependentPhn5 = dependentPhn5;
	}

	public String getDependentPhn6() {
		return dependentPhn6;
	}

	public void setDependentPhn6(String dependentPhn6) {
		this.dependentPhn6 = dependentPhn6;
	}

	public String getDependentPhn7() {
		return dependentPhn7;
	}

	public void setDependentPhn7(String dependentPhn7) {
		this.dependentPhn7 = dependentPhn7;
	}

	@Override
	public String toString() {
		return "AddGroupMemberRequest [groupNumber=" + groupNumber + ", effectiveDate=" + effectiveDate + ", phn=" + phn
				+ ", groupMemberNumber=" + groupMemberNumber + ", departmentNumber=" + departmentNumber + ", phone=" + phone
				+ ", homeAddress=" + homeAddress + ", mailingAddress=" + mailingAddress + ", spousePhn=" + spousePhn + ", dependentPhn1="
				+ dependentPhn1 + ", dependentPhn2=" + dependentPhn2 + ", dependentPhn3=" + dependentPhn3 + ", dependentPhn4="
				+ dependentPhn4 + ", dependentPhn5=" + dependentPhn5 + ", dependentPhn6=" + dependentPhn6 + ", dependentPhn7="
				+ dependentPhn7 + "]";
	}

}
