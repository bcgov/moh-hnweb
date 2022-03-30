package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

import ca.bc.gov.hlth.hnweb.model.rest.groupmember.MemberAddress;

public class UpdateContractAddressRequest {
	
	private String phn;	
	private String groupNumber;	
	private MemberAddress homeAddress;
	private MemberAddress mailingAddress;
	
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

}
