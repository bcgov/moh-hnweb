package ca.bc.gov.hlth.hnweb.model.eligibility;

import java.time.LocalDate;

public class CheckEligibilityRequest {
	private String phn;
	private LocalDate eligibilityDate;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public LocalDate getEligibilityDate() {
		return eligibilityDate;
	}

	public void setEligibilityDate(LocalDate eligibilityDate) {
		this.eligibilityDate = eligibilityDate;
	}

	@Override
	public String toString() {
		return "CheckEligibilityRequest [phn=" + phn + ", eligibilityDate=" + eligibilityDate + "]";
	}

}
