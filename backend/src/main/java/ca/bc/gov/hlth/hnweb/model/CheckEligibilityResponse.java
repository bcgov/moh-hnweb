package ca.bc.gov.hlth.hnweb.model;

public class CheckEligibilityResponse {
	private String phn;
	private String beneficiaryOnDateChecked;
	private String coverageEndDate;
	private String coverageEndReason;
	private String exclusionPeriodEndDate;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getBeneficiaryOnDateChecked() {
		return beneficiaryOnDateChecked;
	}

	public void setBeneficiaryOnDateChecked(String beneficiaryOnDateChecked) {
		this.beneficiaryOnDateChecked = beneficiaryOnDateChecked;
	}

	public String getCoverageEndDate() {
		return coverageEndDate;
	}

	public void setCoverageEndDate(String coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	public String getCoverageEndReason() {
		return coverageEndReason;
	}

	public void setCoverageEndReason(String coverageEndReason) {
		this.coverageEndReason = coverageEndReason;
	}

	public String getExclusionPeriodEndDate() {
		return exclusionPeriodEndDate;
	}

	public void setExclusionPeriodEndDate(String exclusionPeriodEndDate) {
		this.exclusionPeriodEndDate = exclusionPeriodEndDate;
	}

	@Override
	public String toString() {
		return "CheckEligibilityResponse [phn=" + phn + ", beneficiaryOnDateChecked=" + beneficiaryOnDateChecked + ", coverageEndDate="
				+ coverageEndDate + ", coverageEndReason=" + coverageEndReason + ", exclusionPeriodEndDate=" + exclusionPeriodEndDate + "]";
	}

}
