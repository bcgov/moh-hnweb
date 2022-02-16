package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class GetContractPeriodsResponse extends BaseResponse {
	private String phn;
	
	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	@Override
	public String toString() {
		return "GetContractPeriodsRequest [phn=" + phn + "]";
	}

}
