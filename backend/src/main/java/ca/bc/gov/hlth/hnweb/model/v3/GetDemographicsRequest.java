package ca.bc.gov.hlth.hnweb.model.v3;

public class GetDemographicsRequest {
	
	private String phn;
	
	private String mrnSource;
		
	public String getPhn() {
		return phn;
	}
	public void setPhn(String mrn) {
		this.phn = mrn;
	}
	public String getMrnSource() {
		return mrnSource;
	}
	public void setMrnSource(String mrnSource) {
		this.mrnSource = mrnSource;
	}
	
}
