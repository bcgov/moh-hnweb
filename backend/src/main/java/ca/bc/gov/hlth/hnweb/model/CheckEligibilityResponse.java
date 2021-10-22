package ca.bc.gov.hlth.hnweb.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CheckEligibilityResponse {
	private String phn;
	private Boolean beneficiaryOnDateChecked;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date coverageEndDate;
	private String reason;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date exclusionPeriodEndDate;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public Boolean getBeneficiaryOnDateChecked() {
		return beneficiaryOnDateChecked;
	}

	public void setBeneficiaryOnDateChecked(Boolean beneficiaryOnDateChecked) {
		this.beneficiaryOnDateChecked = beneficiaryOnDateChecked;
	}

	public Date getCoverageEndDate() {
		return coverageEndDate;
	}

	public void setCoverageEndDate(Date coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getExclusionPeriodEndDate() {
		return exclusionPeriodEndDate;
	}

	public void setExclusionPeriodEndDate(Date exclusionPeriodEndDate) {
		this.exclusionPeriodEndDate = exclusionPeriodEndDate;
	}

	@Override
	public String toString() {
		return "CheckEligibilityResponse [phn=" + phn + ", beneficiaryOnDateChecked=" + beneficiaryOnDateChecked + ", coverageEndDate="
				+ coverageEndDate + ", reason=" + reason + ", exclusionPeriodEndDate=" + exclusionPeriodEndDate + "]";
	}

}
