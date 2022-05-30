package ca.bc.gov.hlth.hnweb.model.rest.groupmember;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class ChangeEffectiveDateResponse extends BaseResponse {
	private String phn;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	@Override
	public String toString() {
		return "ChangeEffectiveDateResponse [phn=" + phn + ", status=" + status + ", message=" + message + "]";
	}

}
