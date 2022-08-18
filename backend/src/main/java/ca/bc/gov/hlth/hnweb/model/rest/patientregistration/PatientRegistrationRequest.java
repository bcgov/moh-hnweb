package ca.bc.gov.hlth.hnweb.model.rest.patientregistration;

public class PatientRegistrationRequest {

	private String phn;

	private String payee;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

}
