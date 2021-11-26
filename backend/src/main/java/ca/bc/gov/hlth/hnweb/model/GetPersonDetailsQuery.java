package ca.bc.gov.hlth.hnweb.model;

public class GetPersonDetailsQuery {
	
	private String mrn;
	
	private String mrnSource;
	
	public String getMrn() {
		return mrn;
	}
	public void setMrn(String mrn) {
		this.mrn = mrn;
	}
	public String getMrnSource() {
		return mrnSource;
	}
	public void setMrnSource(String mrnSource) {
		this.mrnSource = mrnSource;
	}

}
