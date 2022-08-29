package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

import java.time.LocalDate;
import java.util.List;

public class AuditReportRequest {

	private String userId;

	private List<String> organizations;

	private List<String> transactionTypes;

	private LocalDate startDate;

	private LocalDate endDate;
	
	private Integer first;
	
	private Integer rows;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<String> organizations) {
		this.organizations = organizations;
	}

	public List<String> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(List<String> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "AuditReportRequest [userId=" + userId + ", organization=" + organizations + ", type ="
				+ transactionTypes + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
