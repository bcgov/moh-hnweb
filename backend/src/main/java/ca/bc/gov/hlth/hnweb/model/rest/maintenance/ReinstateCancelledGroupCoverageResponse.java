package ca.bc.gov.hlth.hnweb.model.rest.maintenance;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class ReinstateCancelledGroupCoverageResponse extends BaseResponse {
	private String phn;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	@Override
	public String toString() {
		return "ReinstateCancelledGroupCoverageResponse [phn=" + phn + ", status=" + status + ", message=" + message + "]";
	}

}
