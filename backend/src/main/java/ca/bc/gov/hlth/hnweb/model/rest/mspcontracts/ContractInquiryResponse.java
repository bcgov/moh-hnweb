package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class ContractInquiryResponse extends BaseResponse {
	private String phn;
	private String groupNumber;
	private String groupMemberNumber;
	private String groupMemberDepartmentNumber;

	private String homeAddressLine1;
	private String homeAddressLine2;
	private String homeAddressLine3;
	private String homeAddressPostalCode;

	private String mailingAddressLine1;
	private String mailingAddressLine2;
	private String mailingAddressLine3;
	private String mailingAddressPostalCode;

	private String telephone;

	private List<ContractInquiryBeneficiary> contractInquiryBeneficiaries = new ArrayList<>();

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

	public String getGroupMemberDepartmentNumber() {
		return groupMemberDepartmentNumber;
	}

	public void setGroupMemberDepartmentNumber(String groupMemberDepartmentNumber) {
		this.groupMemberDepartmentNumber = groupMemberDepartmentNumber;
	}

	public String getHomeAddressLine1() {
		return homeAddressLine1;
	}

	public void setHomeAddressLine1(String homeAddressLine1) {
		this.homeAddressLine1 = homeAddressLine1;
	}

	public String getHomeAddressLine2() {
		return homeAddressLine2;
	}

	public void setHomeAddressLine2(String homeAddressLine2) {
		this.homeAddressLine2 = homeAddressLine2;
	}

	public String getHomeAddressLine3() {
		return homeAddressLine3;
	}

	public void setHomeAddressLine3(String homeAddressLine3) {
		this.homeAddressLine3 = homeAddressLine3;
	}

	public String getHomeAddressPostalCode() {
		return homeAddressPostalCode;
	}

	public void setHomeAddressPostalCode(String homeAddressPostalCode) {
		this.homeAddressPostalCode = homeAddressPostalCode;
	}

	public String getMailingAddressLine1() {
		return mailingAddressLine1;
	}

	public void setMailingAddressLine1(String mailingAddressLine1) {
		this.mailingAddressLine1 = mailingAddressLine1;
	}

	public String getMailingAddressLine2() {
		return mailingAddressLine2;
	}

	public void setMailingAddressLine2(String mailingAddressLine2) {
		this.mailingAddressLine2 = mailingAddressLine2;
	}

	public String getMailingAddressLine3() {
		return mailingAddressLine3;
	}

	public void setMailingAddressLine3(String mailingAddressLine3) {
		this.mailingAddressLine3 = mailingAddressLine3;
	}

	public String getMailingAddressPostalCode() {
		return mailingAddressPostalCode;
	}

	public void setMailingAddressPostalCode(String mailingAddressPostalCode) {
		this.mailingAddressPostalCode = mailingAddressPostalCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<ContractInquiryBeneficiary> getContractInquiryBeneficiaries() {
		return contractInquiryBeneficiaries;
	}

	public void setContractInquiryBeneficiaries(List<ContractInquiryBeneficiary> contractInquiryBeneficiaries) {
		this.contractInquiryBeneficiaries = contractInquiryBeneficiaries;
	}

	@Override
	public String toString() {
		return "GetContractPeriodsRequest [phn=" + phn + "]";
	}

}
