package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class UpdateNumberAndDeptResponse extends BaseResponse {
	private String phn;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	@Override
	public String toString() {
		return "UpdateNumberAndDeptResponse [phn=" + phn + ", status=" + status + ", message=" + message + "]";
	}

}
