package ca.bc.gov.hlth.hnweb.model.rest.enrollment;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class EnrollSubscriberResponse extends BaseResponse {

	private String phn;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String pid) {
		this.phn = pid;
	}

}