package ca.bc.gov.hlth.hnweb.model.v3;

public class GetDemographicsRequest {

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

	// These getters and setters hide the mrn logic and naming from the converter
	public String getPhn() {
		return mrn;
	}

	public void setPhn(String phn) {
		this.mrn = phn;
		this.mrnSource = "MOH_CRS";
	}

}
