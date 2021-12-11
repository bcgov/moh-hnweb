package ca.bc.gov.hlth.hnweb.model;

public class CheckEligibilityResponse extends BaseResponse {
	private String phn;
	private String beneficiaryOnDateChecked;
	private String coverageEndDate;
	private String coverageEndReason;
	private String exclusionPeriodEndDate;
	private String clientInstructions;

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

	public String getClientInstructions() {
		return clientInstructions;
	}

	public void setClientInstructions(String clientInstructions) {
		this.clientInstructions = clientInstructions;
	}

	@Override
	public String toString() {
		return "CheckEligibilityResponse [phn=" + phn + ", beneficiaryOnDateChecked=" + beneficiaryOnDateChecked + ", coverageEndDate="
				+ coverageEndDate + ", coverageEndReason=" + coverageEndReason + ", exclusionPeriodEndDate=" + exclusionPeriodEndDate
				+ ", clientInstructions=" + clientInstructions + ", status=" + status + ", message=" + message + "]";
	}

}
