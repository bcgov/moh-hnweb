package ca.bc.gov.hlth.hnweb.model;

import java.util.Date;

public class CheckEligibilityRequest {
	private String phn;
	private Date eligibilityDate;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public Date getEligibilityDate() {
		return eligibilityDate;
	}

	public void setEligibilityDate(Date eligibilityDate) {
		this.eligibilityDate = eligibilityDate;
	}

	@Override
	public String toString() {
		return "CheckEligibilityRequest [phn=" + phn + ", eligibilityDate=" + eligibilityDate + "]";
	}

}
