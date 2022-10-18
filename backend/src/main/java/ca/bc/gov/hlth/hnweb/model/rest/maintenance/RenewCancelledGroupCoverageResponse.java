package ca.bc.gov.hlth.hnweb.model.rest.maintenance;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class RenewCancelledGroupCoverageResponse extends BaseResponse {
	private String phn;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	@Override
	public String toString() {
		return "RenewCancelledGroupCoverageResponse [phn=" + phn + ", status=" + status + ", message=" + message + "]";
	}

}
