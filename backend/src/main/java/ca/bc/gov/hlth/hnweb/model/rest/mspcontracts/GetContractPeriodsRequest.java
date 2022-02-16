package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

public class GetContractPeriodsRequest {
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
