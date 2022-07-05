package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;


public class AuditReportRequest {

	private String userId;

	private String organization;

	private String transactionType;

	//@JsonFormat(pattern = "yyyy-MM-dd")
	//private LocalDate startDate;

	//@JsonFormat(pattern = "yyyy-MM-dd")
	//private LocalDate endDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/*
	 * public LocalDate getStartDate() { return startDate; }
	 * 
	 * public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
	 * 
	 * public LocalDate getEndDate() { return endDate; }
	 * 
	 * public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
	 */

}
