package ca.bc.gov.hlth.hnweb.model.rest.maintenance;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class ReinstateOverAgeDependentResponse extends BaseResponse {

	private String phn;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String pid) {
		this.phn = pid;
	}

}