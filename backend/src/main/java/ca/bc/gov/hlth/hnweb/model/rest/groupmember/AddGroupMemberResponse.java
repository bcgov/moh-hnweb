package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class AddGroupMemberResponse extends BaseResponse {
	private String phn;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	@Override
	public String toString() {
		return "AddGroupMemberResponse [phn=" + phn + ", status=" + status + ", message=" + message + "]";
	}

}
